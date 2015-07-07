package com.ukreugene.dbclassloader.hsqldb;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ukreugene.dbclassloader.ByteArrayClassStorage;
import com.ukreugene.dbclassloader.domain.JavaClass;

class HSQLByteArrayStorage implements ByteArrayClassStorage, RowMapper<JavaClass> {

	private JdbcTemplate jdbcTemplate;

	public HSQLByteArrayStorage(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}

	public void save(JavaClass javaClass) {
		this.jdbcTemplate.update("insert into java_class values (?,?)", javaClass.getNameClass(), javaClass.getDataClass());
	}

	public JavaClass findByName(String nameClass) {
		return this.jdbcTemplate.queryForObject("select nameClass, class from java_class where nameClass = ?", this, nameClass);

	}

	public JavaClass mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Blob blob = rs.getBlob("class");
		String nameClass = rs.getString("nameClass");
		
		byte[] dataClass = null;
		
		if (blob != null) {
			InputStream inputStream = blob.getBinaryStream();
			try {
				dataClass = IOUtils.toByteArray(inputStream);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		}

		JavaClass javaClass = new JavaClass();
		javaClass.setNameClass(nameClass);
		javaClass.setDataClass(dataClass);
		
		
		return javaClass;
	}

}
