package com.ukreugene.dbclassloader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class Main {

	private static final String NAME_CLASS = "StringUtils";

	public static void main(String[] args) throws Exception {

		ApplicationContext context = new AnnotationConfigApplicationContext(
				"com.ukreugene.dbclassloader");

		JdbcTemplate jt = context.getBean(JdbcTemplate.class);

		// ----------------------------- read class file

		byte[] sourceClassBytes;
		InputStream inputStream = Main.class
				.getResourceAsStream("/StringUtils.class");
		try {
			sourceClassBytes = IOUtils.toByteArray(inputStream);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}

		// ------------------------- save class file to db

		jt.update("insert into java_class values (?,?)", NAME_CLASS,
				sourceClassBytes);

		// ------------------------- restore class from db

		byte[] classData = jt.queryForObject(
				"select class from java_class where nameClass = ?",
				new RowMapper<byte[]>() {
					public byte[] mapRow(ResultSet rs, int rowNum)
							throws SQLException {

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

				}, NAME_CLASS);

		// --------------------- load class and try invole method -------------------------------

		if (classData != null) {
			IOClassLoader loader = new IOClassLoader();
			Class<?> clazz = loader.loadFromInputStream(classData);

			System.out.println(clazz.getName());

			Method[] methods = clazz.getMethods();
			for (Method method : methods) {

				if ("trimToEmpty".equals(method.getName())) {

					String testString = "    test   ";

					System.out.println("test string: '" + testString + "'");

					Object res = method.invoke(null, testString);

					System.out.println("string after trim: '" + res + "'");
				}

			}
		}
	}

	static class IOClassLoader extends URLClassLoader {
		public IOClassLoader() {
			super(new URL[] {});
		}

		public Class<?> loadFromInputStream(byte[] classData)
				throws IOException {
			return super.defineClass(null, classData, 0, classData.length);
		}
	}

}
