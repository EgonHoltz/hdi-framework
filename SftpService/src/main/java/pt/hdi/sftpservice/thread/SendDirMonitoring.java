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
import org.springframework.data.util.Optionals;
import pt.hdi.sftpservice.model.Configuration;
import pt.hdi.sftpservice.service.ConfigurationService;
import pt.hdi.sftpservice.service.SftpService;

public class SendDirMonitoring implements Runnable {

	private ApplicationContext ctx;
	
	private Environment env;
	
	private SftpService sftpService;
	
	private ConfigurationService confService;
	
	private String sendPath;
	
	public SendDirMonitoring(ApplicationContext appCtx) {
		this.ctx = appCtx;
		env = ctx.getEnvironment();
		sftpService = ctx.getBean(SftpService.class);
		confService = ctx.getBean(ConfigurationService.class);
	}
	
	public void run() {
		System.out.println("Started SendDirMonitoring");
		sendPath = env.getProperty("spring.sftp.sendpath");
		Path fpath = Paths.get(sendPath);
		if (!Files.exists(fpath)) {
			System.out.println("Directory does not exists: "+sendPath);
			return;
		}
		
		try(WatchService ws = FileSystems.getDefault().newWatchService();) {
			fpath.register(ws, StandardWatchEventKinds.ENTRY_CREATE);
			while(true) {
				WatchKey wk = ws.poll(10, TimeUnit.SECONDS);
				
				if (wk != null) {
					for (WatchEvent<?> event: wk.pollEvents()) {
						if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
							Path fileP = fpath.resolve((Path) event.context());
							System.out.println("new file found: " + fileP);
						}
					}
					wk.reset();
				}
			}
		} catch (IOException e) {
			System.err.println("Problems with IO: "+e.getStackTrace());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Problems with WatchEvent: "+e.getStackTrace());
			e.printStackTrace();
		} finally {
			System.out.println("Finished SendDirMonitoring, removing all files");
			File exclDir = new File(sendPath);
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
			exec.execute(new SendDirMonitoring(ctx));
		}
	}
	

}
