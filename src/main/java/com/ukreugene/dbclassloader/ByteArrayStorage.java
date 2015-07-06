package com.ukreugene.dbclassloader;

public interface ByteArrayStorage {
	
	public void save(String arrayName, byte[] byteArray);
	public byte[] findByName(String arrayName);

}
