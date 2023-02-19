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

public class ReceiveDirMonitoring implements Runnable {

	@Override
	public void run() {
		Path fpath = Paths.get("/home/egonh/tmp/sftp/recv");
		if (!Files.exists(fpath)) {
			System.out.println("Directory does not exists: /home/egonh/tmp/sftp/recv");
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
		} finally {
			Executor exec = Executors.newFixedThreadPool(1);
			exec.execute(new ReceiveDirMonitoring());
		}
	}
}
