package pt.hdi.sftpservice.thread;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClientException;

public class ReceiveDirMonitoring implements Runnable{

	private ApplicationContext ctx;
		
	private Environment env;
	
	public ReceiveDirMonitoring(ApplicationContext appCtx) {
		this.ctx = appCtx;
		env = ctx.getEnvironment();
	}
	
	@Override
	public void run() {
		System.out.println("Started ReceiveDirMonitoring");
		String recvPath = env.getProperty("spring.sftp.recvpath");
		Path fpath = Paths.get(recvPath);
		if (!Files.exists(fpath)) {
			System.out.println("Directory does not exists: "+recvPath);
			return;
		}

		try(WatchService ws = FileSystems.getDefault().newWatchService();) {
			fpath.register(ws, StandardWatchEventKinds.ENTRY_CREATE);
			
			while(true) {
				WatchKey wk = ws.poll(6, TimeUnit.SECONDS);
				
				if (wk != null) {
					for (WatchEvent<?> event: wk.pollEvents()) {
						if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
							Path fileP = fpath.resolve((Path) event.context());
							System.out.println("new file found: " + fileP);
							Thread thread = new Thread( new FileProcessor(fileP, ctx) );
							thread.start();
						}
					}
					wk.reset();
				}
			}	
		} catch (IOException e) {
			System.err.println("Problems with IO: "+e.getStackTrace());
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.err.println("Problems with WatchEvent: "+e.getStackTrace());
		} catch (RestClientException e) {
			System.err.println("Problems with RestClientException: "+e.getStackTrace());
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("File found has unaccepted format name: " +e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Generic exception, please verify: "+e.getStackTrace());
			e.printStackTrace();
		}
		finally {
			System.out.println("Finished ReceiveDirMonitoring, removing all files");
			File exclDir = new File(recvPath);
			if (exclDir.exists() && exclDir.isDirectory()) {
				File[] files = exclDir.listFiles();
				if (files != null && files.length > 0) {
					for(File file : files) {
						if (file.isFile()) {
							file.delete();
						}
					}
				}
			}
			Executor exec = Executors.newFixedThreadPool(1);
			exec.execute(new ReceiveDirMonitoring(ctx));
		}
	}
}
