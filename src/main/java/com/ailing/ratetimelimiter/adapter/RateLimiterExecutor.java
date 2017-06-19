/*
 * Copyright (c) 2015
 * All rights reserved.
 * $Id: RateLimiterExecutor.java 1492119 2015-11-12 09:52:20Z mayuanchao $
 */
package com.ailing.ratetimelimiter.adapter;

import com.ailing.ratetimelimiter.config.RateLimitState;

import java.util.concurrent.TimeUnit;

/**
 *限流措施实现者接口
 * @FileName  RateLimiterExecutor.java
 * @Date  15-11-6 下午2:54
 * @author mayuanchao
 * @version 1.0
 */
public interface RateLimiterExecutor {
	boolean isLimitOpen(String serviceName);

	RateLimitState tryAcquire(String serviceName);

	RateLimitState tryAcquire(String serviceName, int permits);

	RateLimitState tryAcquire(String serviceName, long timeout, TimeUnit unit);

	RateLimitState tryAcquire(String serviceName, int permits, long timeout, TimeUnit unit);

	void acquire(String serviceName);

	void acquire(String serviceName, int permits);
}