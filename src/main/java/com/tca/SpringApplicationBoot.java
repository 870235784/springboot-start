package com.tca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@ServletComponentScan("com.tca.servlet")
public class SpringApplicationBoot {
	public static void main(String[] args) {
		SpringApplication.run(SpringApplicationBoot.class, args);
	}
}
