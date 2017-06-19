package com.ailing.ratetimelimiter.adapter.executor;

import com.ailing.ratetimelimiter.adapter.ExecutorServiceProvider;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 简单的ExecutorService提供类，可自己实现
 * @author mayuanchao
 */
@Component
public class SimpleExecutorServiceProvider implements ExecutorServiceProvider {
	private final Logger logger = Logger.getLogger(SimpleExecutorServiceProvider.class);
	
	private ExecutorService  executor;
	
	public SimpleExecutorServiceProvider(){
		executor = Executors.newCachedThreadPool(new NamedThreadFactory("LimitTimePoolProvider"));
	}
	
	public ExecutorService getExecutor() {
		return executor;
	}

	class ExecutionRejectedHandler implements RejectedExecutionHandler {
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			logger.warn("ExecutorServiceProvider Task[" + r.toString() + "] rejected by executor :" + executor.toString());
			throw new RejectedExecutionException();
		}
	}
	
	class NamedThreadFactory implements ThreadFactory {
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		public NamedThreadFactory(String theadGroupName) {
	        SecurityManager s = System.getSecurityManager();
	        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
	        namePrefix = theadGroupName + "-thread-";
	    }

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}
}
