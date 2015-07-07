package com.ukreugene.dbclassloader;

import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ukreugene.dbclassloader.domain.JavaClass;

public class Main {

	private static final String NAME_CLASS = "StringUtils";

	public static void main(String[] args) throws Exception {
		//System.setProperty("spring.profiles.active", "hsql");
		System.setProperty("spring.profiles.active", "hibernate");
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.ukreugene.dbclassloader");
		
		URL resource = Main.class.getResource("/StringUtils.class"); 
		byte[] sourceClassBytes = IOUtils.toByteArray(resource);
		System.out.println(sourceClassBytes.length);
		
		
		JavaClass javaClass = new JavaClass();
		javaClass.setNameClass(NAME_CLASS);
		javaClass.setDataClass(sourceClassBytes);
		
		// -----------------------------------------------
		
		ByteArrayClassStorage storage = context.getBean(ByteArrayClassStorage.class);
		
		storage.save(javaClass);
		
		JavaClass restoreClass = storage.findByName(NAME_CLASS);
		
		if (restoreClass != null && restoreClass.getDataClass() != null && restoreClass.getDataClass().length > 0) {
		
			byte[] dataClass = restoreClass.getDataClass();
			System.out.println(dataClass.length);
			
			CustomByteArrayClassLoader classLoader = context.getBean(CustomByteArrayClassLoader.class);
			
			Class<?> loadedClass = classLoader.loadClass(dataClass);
			
			if (loadedClass != null) {
				System.out.println("Class has been loaded: '" + loadedClass.getName() + "'");
			}
		}
	}
}
