/*
 * Copyright (c) 2015
 * All rights reserved.
 * $Id: RateTimeServiceController.java 1492815 2015-11-13 08:02:43Z mayuanchao $
 */
package com.ailing.ratetimelimiter.adapter.executor;

import com.ailing.ratetimelimiter.RateTimeCreatingBeanFactory;
import com.ailing.ratetimelimiter.adapter.RateLimiterExecutor;
import com.ailing.ratetimelimiter.adapter.RateTimeLimiterInvoker;
import com.ailing.ratetimelimiter.adapter.RateTimeServiceCallBack;
import com.ailing.ratetimelimiter.adapter.RateTimeServiceExecutor;
import com.ailing.ratetimelimiter.config.AspectRateTimeProvider;
import com.ailing.ratetimelimiter.config.RateLimitState;
import com.ailing.ratetimelimiter.config.RateTimeConfigurerFactory;
import com.ailing.ratetimelimiter.config.RateTimelimitConfigurerProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import static com.ailing.ratetimelimiter.util.PreconditionUtil.checkNotNull;
/**
 *该服务用于不适用注解的形式下，直接调用该服务来实现限流和超时机制
 * @FileName  RateTimeServiceController.java
 * @Date  15-11-11 下午9:53
 * @author mayuanchao
 * @version 1.0
 */
@Service
public class RateTimeServiceController {
	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private RateTimeCreatingBeanFactory rateTimeCreatingBeanFactory;
	private Class<?extends RateTimelimitConfigurerProvider> clazzConfigurer;

	/**
	 * 初始化配置提供者，调用接口前必须执行init() 初始化 RateTimelimitConfigurerProvider
	 * @param clazzConfigurer
	 */
	public void init(Class<?extends RateTimelimitConfigurerProvider> clazzConfigurer) {
		this.clazzConfigurer = clazzConfigurer;
	}

	/**
	 * 集成了超时机制和限流措施
	 * @param serviceName
	 * @param callBack
	 * @param aspectProvider
	 * @return
	 * @throws Exception
	 */
	public <T> T execute(String serviceName, RateTimeServiceCallBack<T> callBack,
		AspectRateTimeProvider aspectProvider, RateTimeLimiterInvoker invoker)
		throws Exception {
		initConfig(serviceName, callBack, aspectProvider, invoker);
		ApplicationContext context = rateTimeCreatingBeanFactory.getApplicationContext();
		RateTimeServiceExecutor ratimeServiceExecutor = (RateTimeServiceExecutor)context.getBean(RateTimeServiceExecutorAdapter.class);
		return ratimeServiceExecutor.execute(serviceName, callBack);
	}

	/**
	 * 单独针对限流接口
	 * @param serviceName
	 * @param callBack
	 * @param aspectProvider
	 * @return
	 * @throws Exception
	 */
	public <T> T rateLimiterExecute(String serviceName, RateTimeServiceCallBack<T> callBack,
		AspectRateTimeProvider aspectProvider, RateTimeLimiterInvoker invoker)
		throws Exception {
		RateLimiterExecutor rateLimiterExecutor = getRateLimiterExecutor(serviceName, callBack,
				aspectProvider, invoker);
		RateLimitState rateState = rateLimiterExecutor.tryAcquire(serviceName);

		if (logger.isDebugEnabled()) {
			logger.debug("current rate limiter state is " + rateState.name());
		}

		T retVal = null;

		//限流,超过了最大的限流池
		if (RateLimitState.NOT_ACQUIRE.equals(rateState)) {
			retVal = invoker.invokehandler(null);
		} else {
			retVal = callBack.invoker();
		}

		return retVal;
	}

	/**
	 * 获取限流措施执行者
	 * @param serviceName
	 * @param callBack
	 * @param aspectProvider
	 * @return
	 */
	public <T> RateLimiterExecutor getRateLimiterExecutor(String serviceName,
		RateTimeServiceCallBack<T> callBack, AspectRateTimeProvider aspectProvider,
		RateTimeLimiterInvoker invoker) {
		initConfig(serviceName, callBack, aspectProvider, invoker);

		return rateTimeCreatingBeanFactory.getRateLimiterExecutor(serviceName);
	}

	private <T> void initConfig(String serviceName, RateTimeServiceCallBack<T> callBack,
		AspectRateTimeProvider aspectProvider, RateTimeLimiterInvoker invoker) {
		checkNotNull(clazzConfigurer, "RateTimelimitConfigurerProvider Required");

		RateTimeConfigurerFactory configurerFactory = rateTimeCreatingBeanFactory.getConfigurerFactory();
		RateTimelimitConfigurerProvider configProvider = rateTimeCreatingBeanFactory.getConfigurerProvider(clazzConfigurer);

		if (invoker == null) {
			configurerFactory.config(serviceName, configProvider, aspectProvider, null);
		} else {
			configurerFactory.config(serviceName, configProvider, aspectProvider, invoker.getClass());
		}
	}
}