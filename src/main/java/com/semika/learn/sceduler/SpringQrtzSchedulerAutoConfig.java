package com.semika.learn.sceduler;

import com.semika.learn.config.AutoWiringSpringBeanJobFactory;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
@EnableAutoConfiguration
@ConditionalOnProperty(name = "enable.quartz.job", havingValue = "true")
public class SpringQrtzSchedulerAutoConfig {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        logger.info("Hello world from Spring...");
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        logger.debug("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    @ConditionalOnProperty(name="enable.simplejob", havingValue = "true")
    public SchedulerFactoryBean scheduler(@Qualifier("trigger") Trigger trigger,
                                          @Qualifier("jobDetail") JobDetail job, DataSource quartzDataSource) {

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));

        logger.debug("Setting the Scheduler up");
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setJobDetails(job);
        schedulerFactory.setTriggers(trigger);

        // Comment the following line to use the default Quartz job store.
        schedulerFactory.setDataSource(quartzDataSource);

        return schedulerFactory;
    }

    @Bean
    @ConditionalOnProperty(name="enable.kinesis.producer", havingValue = "true")
    public SchedulerFactoryBean kinesisProducerScheduler(@Qualifier("kinesisProducerTrigger") Trigger kinesisProducerTrigger,
                                                         @Qualifier("kinesisProducerJobDetail") JobDetail kinesisProducerJobDetail, DataSource quartzDataSource) {

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));

        logger.debug("Setting the Scheduler up");
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setJobDetails(kinesisProducerJobDetail);
        schedulerFactory.setTriggers(kinesisProducerTrigger);

        // Comment the following line to use the default Quartz job store.
        schedulerFactory.setDataSource(quartzDataSource);

        return schedulerFactory;
    }

    @Bean
    @ConditionalOnProperty(name="enable.kinesis.consumer", havingValue = "true")
    public SchedulerFactoryBean kinesisConsumerScheduler(@Qualifier("kinesisConsumerTrigger") Trigger kinesisProducerTrigger,
                                                         @Qualifier("kinesisConsumerJobDetail") JobDetail kinesisProducerJobDetail, DataSource quartzDataSource) {

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));

        logger.debug("Setting the Scheduler up");
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setJobDetails(kinesisProducerJobDetail);
        schedulerFactory.setTriggers(kinesisProducerTrigger);

        // Comment the following line to use the default Quartz job store.
        schedulerFactory.setDataSource(quartzDataSource);

        return schedulerFactory;
    }

    @Bean(name="jobDetail")
    public JobDetailFactoryBean jobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(SampleJob.class);
        jobDetailFactory.setName("Qrtz_Job_Detail");
        jobDetailFactory.setDescription("Invoke Sample Job service...");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean(name="kinesisProducerJobDetail")
    public JobDetailFactoryBean kinesisProducerJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(KinesisProducerJob.class);
        jobDetailFactory.setName("KINESIS_PRODUCER_JOB");
        jobDetailFactory.setDescription("KINESIS_PRODUCER_JOB");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean(name="kinesisConsumerJobDetail")
    public JobDetailFactoryBean kinesisConsumerJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(KinesisConsumerJob.class);
        jobDetailFactory.setName("KINESIS_CONSUMER_JOB");
        jobDetailFactory.setDescription("KINESIS_CONSUMER_JOB");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean(name="kinesisConsumerTrigger")
    public SimpleTriggerFactoryBean kinesisConsumerTrigger(@Qualifier("kinesisConsumerJobDetail") JobDetail kinesisProducerJobDetail) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(kinesisProducerJobDetail);

        int frequencyInSec = 300; //seconds
        logger.info("Configuring KINESIS_CONSUMER_JOB trigger to fire every {} seconds", frequencyInSec);

        trigger.setRepeatInterval(frequencyInSec * 1000);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.setName("KINESIS_CONSUMER_JOB_TRIGGER");
        return trigger;
    }

    @Bean(name="kinesisProducerTrigger")
    public SimpleTriggerFactoryBean kinesisProducerTrigger(@Qualifier("kinesisProducerJobDetail") JobDetail kinesisProducerJobDetail) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(kinesisProducerJobDetail);

        int frequencyInSec = 60; //seconds
        logger.info("Configuring KINESIS_PRODUCER_JOB trigger to fire every {} seconds", frequencyInSec);

        trigger.setRepeatInterval(frequencyInSec * 1000);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.setName("KINESIS_PRODUCER_JOB_TRIGGER");
        return trigger;
    }

    @Bean(name="trigger")
    public SimpleTriggerFactoryBean trigger(@Qualifier("jobDetail") JobDetail job) {

        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(job);

        int frequencyInSec = 60;
        logger.info("Configuring trigger to fire every {} seconds", frequencyInSec);

        trigger.setRepeatInterval(frequencyInSec * 1000);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.setName("Qrtz_Trigger");
        return trigger;
    }

    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource quartzDataSource() {
        return DataSourceBuilder.create().build();
    }

}
