package pt.hdi.mqservice.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ThreadsInitializator implements ApplicationListener<ContextRefreshedEvent> {

	private ApplicationContext ctx;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent eventCtx) {
		ctx = eventCtx.getApplicationContext();
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.execute(new MQMonitoring(ctx));
	}
	
}
