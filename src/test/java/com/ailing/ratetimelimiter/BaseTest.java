/*
 * Copyright (c) 2015
 * All rights reserved.
 * $Id$
 */
package com.ailing.ratetimelimiter;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @FileName  BaseTest.java
 * @Date  15-11-6 下午3:31
 * @author mayuanchao
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-test.xml"})
public class BaseTest {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Before
	public void before(){
		logger.info("before test...");
	}

	@After
	public void after(){
		logger.info("after test...");
	}
}