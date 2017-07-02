/*
 * Copyright (c) 2015
 * All rights reserved.
 * $Id$
 */

package com.ailing.ratetimelimiter;

import org.springframework.stereotype.Service;

/**
 *
 * @FileName  RateLimiterMock.java
 * @Date  15-11-11 下午5:58 
 * @author mayuanchao
 * @version 1.0
 */
@Service("rateLimiterMock")
public class RateLimiterMock {
	
	@RateTimeLimit(serviceName = "test", limitRate = true , refreshCfg=false, permits = 4)
	public void rateLimitTest(String param) {

		/*try {
			TimeUnit.MILLISECONDS.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		System.err.println(param);

		// throw new RuntimeException("ee");
	}

	@RateTimeLimit(serviceName = "test1", limitRate = true, intervalTime = 3,refreshCfg=false)
	public void rateLimitTest1(String param) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		System.err.println(param);

		// throw new RuntimeException("ee");
	}

	@RateTimeLimit(serviceName = "test2", limitRate = true, permits = 1000)
	public void rateLimitTest2(String param) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		System.err.println(param);

		// throw new RuntimeException("ee");
	}
}