package com.smartkaya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * KaYa
 * @author LiangChen　2018/4/30
 * @version 0.0.1
 */
@EnableAutoConfiguration
@SpringBootApplication
public class KayaSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(KayaSpringApplication.class, args);
	}
}
