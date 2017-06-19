/*
 * Copyright (c) 2016-2017 by Colley
 * All rights reserved.
 */
package com.ailing.ratetimelimiter;

import org.junit.Test;

import javax.annotation.Resource;


/**
 *
 * @FileName  RateLimiterTest.java
 * @Date  15-11-5 下午3:20
 * @author mayuanchao
 * @version 1.0
 */
public class RateLimiterTest extends BaseTest {
    @Resource(name = "rateLimiterMock")
    private RateLimiterMock rateLimiterMock;

    @Test
    public void testRate() {
        rateLimiterMock.rateLimitTest("ok1~~~~~~~~~~");
        rateLimiterMock.rateLimitTest("ok2~~~~~~~~~~");
        rateLimiterMock.rateLimitTest("ok3~~~~~~~~~~");
        rateLimiterMock.rateLimitTest("ok4~~~~~~~~~~");
        rateLimiterMock.rateLimitTest("ok5~~~~~~~~~~");
        rateLimiterMock.rateLimitTest("ok6~~~~~~~~~~");
        rateLimiterMock.rateLimitTest("ok7~~~~~~~~~~");
    }

    public void testRate1() {
        rateLimiterMock.rateLimitTest1("ok1~~~~~~~~~~");
        rateLimiterMock.rateLimitTest2("ok2~~~~~~~~~~");
    }
}
