package com.semika.learn;

import com.semika.learn.service.SampleJobService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuartzSchedulerApplication {

	private SampleJobService sampleJobService;

	public QuartzSchedulerApplication(SampleJobService sampleJobService) {
		this.sampleJobService = sampleJobService;
	}

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

	@Bean
	@ConditionalOnProperty(name="enable.sample.job.runner", havingValue = "true")
	public CommandLineRunner commandLineRunner() {
		return args -> {
			///for (String arg : args) {
			sampleJobService.executeSampleJob();
			System.exit(0); // Success
			//}
		};
	}

}
