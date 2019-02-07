package com.SpringBootJpa.demo.dao;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.SpringBootJpa.demo.entities.Officer;

public interface OfficerDAO {
	
	Officer save(Officer officer);
    Optional<Officer> findById(Integer id);
    List<Officer> findAll();
    long count();
    void delete(Officer officer);
    boolean existsById(Integer id);
	

}
