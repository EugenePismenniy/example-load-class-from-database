package com.ukreugene.dbclassloader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class Main {

	private static final String NAME_CLASS = "StringUtils";

	public static void main(String[] args) throws Exception {
		System.setProperty("spring.profiles.active", "hsql");
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.ukreugene.dbclassloader");
		
		URL resource = Main.class.getResource("/StringUtils.class"); 
		byte[] sourceClassBytes = IOUtils.toByteArray(resource);
		System.out.println(sourceClassBytes.length);
		
		ByteArrayStorage storage = context.getBean(ByteArrayStorage.class);
		
		storage.save(NAME_CLASS, sourceClassBytes);
		
		byte[] restoreClassBytes = storage.findByName(NAME_CLASS);
		System.out.println(restoreClassBytes.length);
		
		CustomByteArrayClassLoader classLoader = context.getBean(CustomByteArrayClassLoader.class);
		
		Class<?> loadedClass = classLoader.loadClass(restoreClassBytes);
		
		if (loadedClass != null) {
			System.out.println("Class has been loaded: '" + loadedClass.getName() + "'");
		}
	}
}
