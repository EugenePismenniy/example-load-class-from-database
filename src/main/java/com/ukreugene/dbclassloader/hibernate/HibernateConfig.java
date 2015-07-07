package com.ukreugene.dbclassloader.hibernate;

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
		EmbeddedDatabase ds = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).addScript("classpath:create.sql").build();
		return ds;
	}

	@SuppressWarnings("deprecation")
	@Bean
	@Autowired
	public SessionFactory sessionFactory(DataSource ds) {
		return new LocalSessionFactoryBuilder(ds)
		.addAnnotatedClass(JavaClass.class).buildSessionFactory();
	}
	
	@Bean
	@Autowired
	public ByteArrayClassStorage byteArrayStorage(SessionFactory sessionFactory) {
		return new HibernateByteArrayClassStorage(sessionFactory);
	}
	
}
