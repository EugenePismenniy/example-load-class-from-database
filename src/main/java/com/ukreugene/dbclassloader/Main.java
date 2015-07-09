package com.ukreugene.dbclassloader;

import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.strobel.assembler.metadata.ArrayTypeLoader;
import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import com.ukreugene.dbclassloader.domain.JavaClass;

public class Main {

	private static final String NAME_CLASS = "StringUtils";

	public static void main(String[] args) throws Exception {
		//System.setProperty("spring.profiles.active", "hsql");
		System.setProperty("spring.profiles.active", "hibernate");
		// System.setProperty("spring.profiles.active", "sybase_hibernate");
		
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
				 
				DecompilerSettings settings = DecompilerSettings.javaDefaults();
				settings.setTypeLoader(new ArrayTypeLoader(dataClass));
				
				PlainTextOutput output = new PlainTextOutput();
				
				//Decompiler.decompile("org/apache/commons/lang3/StringUtils", output, settings);
				Decompiler.decompile(className.replace('.', '/'), output, settings);
				
				System.out.println(output.toString());
			}
		}
	}
}
