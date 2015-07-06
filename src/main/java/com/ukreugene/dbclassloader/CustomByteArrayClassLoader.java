package com.ukreugene.dbclassloader;

public interface CustomByteArrayClassLoader {

	Class<?> loadClass(byte[] classData);

}