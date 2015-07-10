package com.ukreugene.dbclassloader.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ukreugene.dbclassloader.domain.JavaClass;

@Repository
public interface JavaClassRepository extends JpaRepository<JavaClass, String> {

}
