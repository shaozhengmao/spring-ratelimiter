# spring-ratelimiter
TPS rate limit
使用方法：
一、定义自己的获取配置的实例
1、继承AbstractRatimelimitConfigurerProvider抽象类，实现updateConfig方法，获取配置信息
如果是注解的方式实现，默认是获取注解的配置，参考SimpleAbstractRatimelimitConfigurerProvider
2、关于超时机制，使用ExecutorServiceProvider来定义自己的java.util.concurrent.ExecutorService服务
参考SimpleExecutorServiceProvider 默认使用Executors.newCachedThreadPool
3、TimeLimiterExecutor的实现，可以直接extends AbstractTimeLimiterExecutor
4、可以定义超时或者超过限流的处理类  实现RateTimeLimiterInvoker接口，默认是SimpleRateTimeLimiterInvoker
二、配置 spring配置
<!-- 配置注解类  -->
<!-- 限流超时机制开始  -->
	 <bean id="rateTimeLimiterAspect" class="com.ailing.ratetimelimiter.RateTimeLimiterAspect">
		<property name="configurerProvider" ref="hsRatimelimitConfigurerProvider" />
		<property name="rateTimeServiceExecutor" ref="rateTimeServiceExecutorAdapter" />
	</bean>
	
	<bean id="rateTimeServiceExecutorAdapter" class="com.ailing.ratetimelimiter.adapter.RateTimeServiceExecutorAdapter">
		<property name="timeLimiterExecutor" ref="simpleTimeLimiterExecutor"></property>
		<property name="rateLimiterExecutor" ref="simpleRateLimiterExecutor"></property>
	</bean>
	
	<bean id="simpleTimeLimiterExecutor" class="com.ailing.ratetimelimiter.adapter.executor.SimpleTimeLimiterExecutor">
		<property name="executorServiceProvider" ref="simpleExecutorServiceProvider"></property>
	</bean> 
	
	<!-- 限流超时机制结束  -->
使用方式：
1、使用注解方式
@RateTimeLimit(serviceName = "mockService", limitRate = false, intervalTime = 2,refreshCfg=false)
public void rateLimitTest(String param);
