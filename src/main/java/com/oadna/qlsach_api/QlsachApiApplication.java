package com.oadna.qlsach_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class QlsachApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(QlsachApiApplication.class, args);
	}

}
