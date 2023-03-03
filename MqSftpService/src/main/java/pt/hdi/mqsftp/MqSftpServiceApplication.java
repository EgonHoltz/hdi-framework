package pt.hdi.mqsftp;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pt.hdi.mqsftp.sftp.bean.MQConnectionBean;
import pt.hdi.mqsftp.sftp.thread.ReadMQMessage;
import pt.hdi.mqsftp.sftp.thread.ReceiveDirMonitoring;
import pt.hdi.mqsftp.sftp.thread.SendDirMonitoring;

@SpringBootApplication
public class MqSftpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqSftpServiceApplication.class, args);

		System.out.println("Initializing the threadpool with monitoring processes");
		Executor exec = Executors.newFixedThreadPool(3);
		// Start threads to verify directories of send/rcv
		exec.execute(new ReceiveDirMonitoring());
		exec.execute(new SendDirMonitoring());
		// At this time, MQ reception is fixed. Later it will load as parameterized.
		// This will allow to create queue instantly and does not need to change the app
		MQConnectionBean conConf = new MQConnectionBean("localhost", "quser", "qpass");
		exec.execute(new ReadMQMessage("clients", conConf));
		
	}

}
