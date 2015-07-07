package com.ukreugene.dbclassloader.sybase;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import com.ukreugene.dbclassloader.ByteArrayClassStorage;
import com.ukreugene.dbclassloader.domain.JavaClass;
import com.ukreugene.dbclassloader.hibernate.HibernateByteArrayClassStorage;

@Configuration
@Profile("sybase_hibernate")
@PropertySource("file:${user.home}//Рабочий стол//ServerToServer_scor//test.properties")
public class SybaseConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource() throws ClassNotFoundException {
		// jdbc.url=jdbc:jtds:sybase://<host>:<port>;charset=cp1251
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		return new DriverManagerDataSource(env.getProperty("jdbc.url"), env.getProperty("jdbc.user"), env.getProperty("jdbc.password")); 
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
		p.put("hibernate.dialect", "org.hibernate.dialect.SybaseDialect");
		p.put("hibernate.hbm2ddl.auto", "create");
		return p;
	}	

	@Bean
	@Autowired
	public ByteArrayClassStorage byteArrayStorage(SessionFactory sessionFactory) {
		return new HibernateByteArrayClassStorage(sessionFactory);
	}
	
}
