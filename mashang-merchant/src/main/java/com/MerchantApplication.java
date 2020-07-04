package com;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.zengtengpeng.annotation.EnableCache;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.mashang")
@EnableCreateCacheAnnotation
@EnableCache(value = {})
public class MerchantApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(MerchantApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	}

}
