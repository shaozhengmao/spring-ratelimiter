/*
 * Copyright (c) 2015
 * All rights reserved.
 * $Id: RateTimelimitConfigurerProvider.java 1492119 2015-11-12 09:52:20Z mayuanchao $
 */
package com.ailing.ratetimelimiter.config;

import com.ailing.ratetimelimiter.YdtRateLimiter;
import com.ailing.ratetimelimiter.adapter.RateTimeLimiterInvoker;

/**
 *原始配置信息提供者
 * @FileName  RatimelimitConfigurerProvider.java
 * @Date  15-11-11 下午5:58
 * @author mayuanchao
 * @version 1.0
 */
public interface RateTimelimitConfigurerProvider {
	RateTimeConfigurer create(String serviceName, AspectRateTimeProvider aspectProvider,Class<? extends RateTimeLimiterInvoker> invoker);

	void updateConfig(RateTimeConfigurer rateTimeConfigurer);
	
	YdtRateLimiter getYdtRateLimiter();
}