package com.ukreugene.dbclassloader;

import com.ukreugene.dbclassloader.domain.JavaClass;

public interface ByteArrayClassStorage {
	
	public void save(JavaClass javaClass);
	public JavaClass findByName(String arrayName);

}
