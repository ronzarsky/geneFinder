package com.smile.geneFinder.geneFinder;

import com.smile.geneFinder.geneFinder.business.GeneFinderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GeneFinderApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext =
				SpringApplication.run(GeneFinderApplication.class, args);
		GeneFinderService geneFinderService =
				(GeneFinderService) applicationContext.getBean("geneFinderService", GeneFinderService.class);
		geneFinderService.initService();
	}

}
