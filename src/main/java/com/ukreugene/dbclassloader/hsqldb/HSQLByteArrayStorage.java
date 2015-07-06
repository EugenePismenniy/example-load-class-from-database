package com.ukreugene.dbclassloader.hsqldb;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ukreugene.dbclassloader.ByteArrayStorage;

class HSQLByteArrayStorage implements ByteArrayStorage, RowMapper<byte[]> {

	private JdbcTemplate jdbcTemplate;

	public HSQLByteArrayStorage(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}

	public void save(String arrayName, byte[] byteArray) {
		this.jdbcTemplate.update("insert into java_class values (?,?)", arrayName, byteArray);
	}

	public byte[] findByName(String arrayName) {
		return this.jdbcTemplate.queryForObject("select class from java_class where nameClass = ?", this, arrayName);

	}

	public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
		Blob blob = rs.getBlob("class");

		if (blob != null) {
			InputStream inputStream = blob.getBinaryStream();
			try {
				return IOUtils.toByteArray(inputStream);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		}

		return new byte[] {};
	}

}
