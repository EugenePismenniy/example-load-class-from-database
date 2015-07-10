package com.ukreugene.dbclassloader.jpa;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ukreugene.dbclassloader.ByteArrayClassStorage;
import com.ukreugene.dbclassloader.domain.JavaClass;
import com.ukreugene.dbclassloader.jpa.dao.JavaClassRepository;

/** 
 * @author "evgeniy.pismenny"
 * */
@Configuration
@Profile("jpa")
@EnableJpaRepositories({"com.ukreugene.dbclassloader.jpa.dao"})
@EnableTransactionManagement
public class JpaConfig {

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabase ds = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
		return ds;
	}
	
	@Bean
	@Autowired
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		
		em.setDataSource(dataSource);
		em.setJpaProperties(hibernateProperties());
		em.setPackagesToScan("com.ukreugene.dbclassloader.domain");
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setShowSql(true);
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setDatabase(Database.HSQL);
		
		em.setJpaVendorAdapter(vendorAdapter);
		
		return em;
	}
	
	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);	
	}
	
	Properties hibernateProperties() {
		Properties p = new Properties();
		p.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		p.put("hibernate.hbm2ddl.auto", "create");
		return p;
	}
	
	@Bean
	@Autowired
	public ByteArrayClassStorage byteArrayStorage(final JavaClassRepository repository) {
		return new ByteArrayClassStorage() {
			
			public void save(JavaClass javaClass) {
				repository.save(javaClass);
			}
			
			public JavaClass findByName(String arrayName) {
				return repository.findOne(arrayName);
			}
		};
	}
}
