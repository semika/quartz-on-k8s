package com.semika.learn;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuartzSchedulerApplication {

	public static void main(String[] args) {

		SpringApplication.run(QuartzSchedulerApplication.class, args);

//		try {
//			// Grab the Scheduler instance from the Factory
//			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//
//			// and start it off
//			scheduler.start();
//
//			scheduler.shutdown();
//
//		} catch (SchedulerException se) {
//			se.printStackTrace();
//		}
	}

}
