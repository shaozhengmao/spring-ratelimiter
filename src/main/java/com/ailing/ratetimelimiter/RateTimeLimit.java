/*
 * Copyright (c) 2016-2017 by Colley
 * All rights reserved.
 */
package com.ailing.ratetimelimiter;

import com.ailing.ratetimelimiter.adapter.RateTimeLimiterInvoker;
import com.ailing.ratetimelimiter.config.RateTimelimitConfigurerProvider;
import com.ailing.ratetimelimiter.config.SimpleRateTimeLimiterInvoker;
import com.ailing.ratetimelimiter.config.SimpleRatimelimitConfigurerProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateTimeLimit {
    /**
     * 限流TPS
     * @return
     */
    double permits() default Integer.MAX_VALUE;
    /**
     * 服务名称
     * @return
     */
    String serviceName();
    /**
     *限流延迟 单位分钟
     * @return
     */
    int rateDelay() default 10;
    /**
     * 限流时间段
     * @return
     */
    String rateCronExpr() default "";
    /**
     * 是否打开限流措施
     * @return
     */
    boolean limitRate() default false;
    /**
     * 更新配置间隔时间 单位是秒
     * @return
     */
    long intervalTime() default 15 * 60;
    /**
     * 是否定时刷新配置
     * @return
     */
    boolean refreshCfg() default true;
    /**
     * 限流或者开启超时机制处理类
     * @return
     */
    Class<?extends RateTimeLimiterInvoker> invoker() default SimpleRateTimeLimiterInvoker.class;
    /**
     * 原始数据提供类
     * @return
     */
    Class<?extends RateTimelimitConfigurerProvider> configurer() default SimpleRatimelimitConfigurerProvider.class;
}
