package com.ukreugene.dbclassloader.hibernate;

import org.hibernate.*;

import com.ukreugene.dbclassloader.ByteArrayClassStorage;
import com.ukreugene.dbclassloader.domain.JavaClass;

class HibernateByteArrayClassStorage implements ByteArrayClassStorage {

	private SessionFactory sessionFactory;

	public HibernateByteArrayClassStorage(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(JavaClass javaClass) {
		
		Session session = sessionFactory.openSession();
		try {
			Transaction tx = session.beginTransaction();
			session.persist(javaClass);
			tx.commit();
		} finally {
			session.close();
		}
	}

	public JavaClass findByName(String arrayName) {
		
		Session session = sessionFactory.openSession();
		try {
			Query query = session.createQuery("from JavaClass a where a.nameClass = :nameClass").setString("nameClass", arrayName);
			return (JavaClass) query.uniqueResult();
			
		} finally {
			session.close();
		}
	}
}
