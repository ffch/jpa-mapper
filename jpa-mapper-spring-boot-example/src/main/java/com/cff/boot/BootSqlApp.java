package com.cff.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = "com.cff.boot")
public class BootSqlApp {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(BootSqlApp.class, args);
	}
}
