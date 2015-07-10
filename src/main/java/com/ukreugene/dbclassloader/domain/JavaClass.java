package com.ukreugene.dbclassloader.domain;

import javax.persistence.*;

@Entity
@Table(name = "java_class")
public class JavaClass {

	@Id
	@Column(name = "nameClass", nullable = false)	
	private String nameClass;
	
	@Lob
	@Column(name = "class", nullable = false )	
	private byte[] dataClass;

	public String getNameClass() {
		return nameClass;
	}

	public void setNameClass(String nameClass) {
		this.nameClass = nameClass;
	}

	public byte[] getDataClass() {
		return dataClass;
	}

	public void setDataClass(byte[] dataClass) {
		this.dataClass = dataClass;
	}

}
