package com.bbtree;

import com.bbtree.redis.RedisUtil;
import com.bbtree.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class EmchatServiceApplication {

	public static void main(String[] args) {
		final ApplicationContext applicationContext = SpringApplication.run(EmchatServiceApplication.class, args);
	}

}
