package com.ukreugene.dbclassloader.hsqldb;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.ukreugene.dbclassloader.ByteArrayStorage;

@Configuration
@Profile("hsql")
public class HSQLConfig {
	
	@Bean
	public DataSource dataSource() {
		EmbeddedDatabase ds = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).addScript("classpath:create.sql").build();
		return ds;
	}
	
	@Bean
	@Autowired
	public ByteArrayStorage byteArrayStorage(DataSource ds) {
		return new HSQLByteArrayStorage(ds);
	}
}
