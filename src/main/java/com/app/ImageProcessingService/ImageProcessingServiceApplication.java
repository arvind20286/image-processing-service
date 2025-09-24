package com.app.ImageProcessingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageProcessingServiceApplication {

	public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
		SpringApplication.run(ImageProcessingServiceApplication.class, args);
	}

}
