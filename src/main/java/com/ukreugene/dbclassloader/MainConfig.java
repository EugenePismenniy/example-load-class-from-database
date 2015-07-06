package com.ukreugene.dbclassloader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ukreugene.dbclassloader.impl.SimpleByteArrayClassLoader;

@Configuration
public class MainConfig {
	
	@Bean
	public CustomByteArrayClassLoader classLoader() {
		return new SimpleByteArrayClassLoader();
	}

}
