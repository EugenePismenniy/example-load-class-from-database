package com.ukreugene.dbclassloader.impl;

import java.net.URL;
import java.net.URLClassLoader;

import com.ukreugene.dbclassloader.CustomByteArrayClassLoader;

public class SimpleByteArrayClassLoader extends URLClassLoader implements CustomByteArrayClassLoader {

	public SimpleByteArrayClassLoader() {
		super(new URL[]{});
	}
	
	public Class<?> loadClass(byte[] classData) {
		return super.defineClass(null, classData, 0, classData.length);
	}
}
