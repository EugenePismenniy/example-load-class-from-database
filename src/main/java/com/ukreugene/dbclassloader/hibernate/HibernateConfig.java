package com.ukreugene.dbclassloader.hibernate;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.*;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import com.ukreugene.dbclassloader.ByteArrayClassStorage;
import com.ukreugene.dbclassloader.domain.JavaClass;

@Configuration
@Profile("hibernate")
public class HibernateConfig {

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabase ds = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
		return ds;
	}

	@SuppressWarnings("deprecation")
	@Bean
	@Autowired
	public SessionFactory sessionFactory(DataSource ds, Properties hibernateProperties) {
		return new LocalSessionFactoryBuilder(ds)
		.addAnnotatedClass(JavaClass.class)
		.addProperties(hibernateProperties)
		.buildSessionFactory();
	}
	
	@Bean
	public Properties hibernateProperties() {
		Properties p = new Properties();
		p.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		p.put("hibernate.hbm2ddl.auto", "create");
		return p;
	}	
	
	@Bean
	@Autowired
	public ByteArrayClassStorage byteArrayStorage(SessionFactory sessionFactory) {
		return new HibernateByteArrayClassStorage(sessionFactory);
	}
	
}
