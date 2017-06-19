/*
 * Copyright (c) 2015
 * All rights reserved.
 * $Id: AbstractTimeLimiterExecutor.java 1492119 2015-11-12 09:52:20Z mayuanchao $
 */
package com.ailing.ratetimelimiter.adapter.executor;

import com.ailing.ratetimelimiter.RateTimeCreatingBeanFactory;
import com.ailing.ratetimelimiter.adapter.ExecutorServiceProvider;
import com.ailing.ratetimelimiter.adapter.RateTimeLimiterInvoker;
import com.ailing.ratetimelimiter.adapter.RateTimeServiceCallBack;
import com.ailing.ratetimelimiter.adapter.TimeLimiterExecutor;
import com.ailing.ratetimelimiter.config.RateTimeConfigurer;
import com.ailing.ratetimelimiter.config.TimeConfig;
import com.ailing.ratetimelimiter.exception.RatimeLimiterException;
import com.ailing.ratetimelimiter.util.PreconditionUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.ailing.ratetimelimiter.util.PreconditionUtil.checkArgument;
import static com.ailing.ratetimelimiter.util.PreconditionUtil.checkNotNull;

/**
 *超时机制重新类
 * @FileName  AbstractTimeLimiterExecutor.java
 * @Date  15-11-11 下午5:58
 * @author mayuanchao
 * @version 1.0
 */
public abstract class AbstractTimeLimiterExecutor implements TimeLimiterExecutor {
	protected final Logger logger = Logger.getLogger(getClass());

	/**
	 * 配置工厂方法，用于创建配置信息
	 */
	@Autowired
	private RateTimeCreatingBeanFactory rateTimeCreatingBeanFactory;

	/**
	 * 判断是否开启超时机制
	 */
	public boolean isLimitOpen(String serviceName) {
		RateTimeConfigurer ratimeConfigurer = rateTimeCreatingBeanFactory.getConfigurerFactory().getRatimeConfigurer(
					serviceName);

		return ratimeConfigurer.isLimitTimer();
	}

	/**
	 * 超时控制实现方法
	 */
	public <T> T invokeByLimitTime(String serviceName, final RateTimeServiceCallBack<T> callBack) {
		//超时后处理类
		RateTimeLimiterInvoker invoker = rateTimeCreatingBeanFactory.createLimiterInvoker(
					serviceName);
		checkNotNull(invoker, "RatimeLimiterInvoker class Required");

		ExecutorServiceProvider executorServiceProvider = rateTimeCreatingBeanFactory
			.getExecutorServiceProvider(serviceName);

		//从配置工厂中获取配置信息
		RateTimeConfigurer ratimeConfigurer = rateTimeCreatingBeanFactory.getConfigurerFactory().getRatimeConfigurer(
					serviceName);
		TimeConfig timeConfig = ratimeConfigurer.getTimeConfig();
		long taskMaxLiveTime = timeConfig.getMaxLiveTime();
		T result = null;

		/**
		 * 调用超时方法 callWithTimeout taskMaxLiveTime单位是毫秒
		 */
		try {
			result = callWithTimeout(
						executorServiceProvider,
						new ServiceCallable<T>(callBack),
						taskMaxLiveTime,
						TimeUnit.MILLISECONDS,
						false);
		} catch (Exception e) {
			logger.error("invokeByLimitTime was error : " + e.getMessage());
			String errsmg = e.getMessage();
			result = invoker.invokehandler(errsmg);
		}

		return result;
	}

	public class ServiceCallable<T> implements Callable<T> {
		private RateTimeServiceCallBack<T> callBack;

		public ServiceCallable(RateTimeServiceCallBack<T> callBack) {
			this.callBack = callBack;
		}

		public T call() throws Exception {
			return callBack.invoker();
		}
	}

	public <T> T callWithTimeout(ExecutorServiceProvider executorServiceProvider,
		Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible)
		throws Exception {
		checkNotNull(callable);
		checkNotNull(timeoutUnit);
		checkArgument(timeoutDuration > 0, "timeout must be positive: %s", timeoutDuration);

		Future<T> future = executorServiceProvider.getExecutor().submit(callable);

		try {
			if (amInterruptible) {
				try {
					return future.get(timeoutDuration, timeoutUnit);
				} catch (InterruptedException e) {
					future.cancel(true);
					throw e;
				}
			} else {
				return getUninterruptibly(future, timeoutDuration, timeoutUnit);
			}
		} catch (ExecutionException e) {
			throw PreconditionUtil.throwCause(e, true);
		} catch (TimeoutException e) {
			future.cancel(true);
			throw new RatimeLimiterException(e);
		}
	}

	public static <V> V getUninterruptibly(Future<V> future, long timeout, TimeUnit unit)
		throws ExecutionException, TimeoutException {
		boolean interrupted = false;

		try {
			long remainingNanos = unit.toNanos(timeout);
			long end = System.nanoTime() + remainingNanos;

			while (true) {
				try {
					// Future treats negative timeouts just like zero.
					return future.get(remainingNanos, TimeUnit.NANOSECONDS);
				} catch (InterruptedException e) {
					interrupted = true;
					remainingNanos = end - System.nanoTime();
				}
			}
		} finally {
			if (interrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
}