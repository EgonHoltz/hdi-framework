package pt.hdi.mqsftp.sftp.thread;

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

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

public class SendDirMonitoring implements Runnable {

	private ApplicationContext ctx;
	
	public SendDirMonitoring(ApplicationContext appCtx) {
		this.ctx = appCtx;
	}
	
	public void run() {
		Environment env = ctx.getEnvironment();
		System.out.println("Started SendDirMonitoring");
		String sendPath = env.getProperty("spring.sftp.sendpath");
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
		} finally {
			System.out.println("Finished SendDirMonitoring");
			//Executor exec = Executors.newFixedThreadPool(1);
			//exec.execute(new SendDirMonitoring());
		}
	}
	

}
