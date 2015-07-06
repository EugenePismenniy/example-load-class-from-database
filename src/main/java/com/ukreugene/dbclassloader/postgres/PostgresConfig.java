package com.ukreugene.dbclassloader.postgres;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("postgres")
public class PostgresConfig {

	@Bean
	public DataSource dataSource() {
		return null;
	}	
}
