package com.ukreugene.dbclassloader;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ukreugene.dbclassloader.domain.JavaClass;

public class Main {

	private static final String NAME_CLASS = "StringUtils";

	public static void main(String[] args) throws Exception {
		//System.setProperty("spring.profiles.active", "hsql");
		// System.setProperty("spring.profiles.active", "hibernate");
		// System.setProperty("spring.profiles.active", "sybase_hibernate");
		System.setProperty("spring.profiles.active", "jpa");
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.ukreugene.dbclassloader");
		
		URL resource = Main.class.getResource("/StringUtils.class");
		
		System.out.println(FilenameUtils.getName(resource.getFile()));
		
		
		byte[] sourceClassBytes = IOUtils.toByteArray(resource);
		//System.out.println(sourceClassBytes.length);
		
		
		JavaClass javaClass = new JavaClass();
		javaClass.setNameClass(NAME_CLASS);
		javaClass.setDataClass(sourceClassBytes);
		
		// -----------------------------------------------
		
		ByteArrayClassStorage storage = context.getBean(ByteArrayClassStorage.class);
		
		storage.save(javaClass);
		
		JavaClass restoreClass = storage.findByName(NAME_CLASS);
		
		if (restoreClass != null && restoreClass.getDataClass() != null && restoreClass.getDataClass().length > 0) {
		
			final byte[] dataClass = restoreClass.getDataClass();
			System.out.println(dataClass.length);			
			
			// ---------------------- load class to JVM
			CustomByteArrayClassLoader classLoader = context.getBean(CustomByteArrayClassLoader.class);
			
			Class<?> loadedClass = classLoader.loadClass(dataClass);
			
			String className = null;
			
			if (loadedClass != null) {
				className = loadedClass.getName();
				System.out.println("Class has been loaded: '" + className + "'");
			}
			
			// -------------------- decompile byte code
			if (className != null) {			
				 
				CustomDecompiler decomp = context.getBean(CustomDecompiler.class);
				Writer writer = new StringWriter(); 
				decomp.decompile(className.replace('.', '/'), restoreClass.getDataClass(), writer);
				System.out.println(writer.toString());
			}
		}
	}
}
