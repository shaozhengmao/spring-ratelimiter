/*
 * Copyright (c) 2015
 * All rights reserved.
 * $Id: SimpleTimeLimiterExecutor.java 1492119 2015-11-12 09:52:20Z mayuanchao $
 */

package com.github.ailing.ratetimelimiter.adapter.executor;

import com.github.ailing.ratetimelimiter.adapter.ExecutorServiceProvider;

/**
 * 简单的超时机制实现类，可以自己实现
 * @FileName  SimpleTimeLimiterExecutor.java
 * @Date  15-11-11 下午6:08 
 * @author mayuanchao
 * @version 1.0
 */

public class SimpleTimeLimiterExecutor extends AbstractTimeLimiterExecutor<Object> {
	
	@Override
	public void setExecutorServiceProvider(ExecutorServiceProvider executor) {
		this.executor = executor;
	}
}