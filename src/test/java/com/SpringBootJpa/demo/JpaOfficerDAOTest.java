package com.SpringBootJpa.demo;

import javax.transaction.Transactional;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.SpringBootJpa.demo.dao.OfficerDAO;
import com.SpringBootJpa.demo.entities.Rank;
import com.SpringBootJpa.demo.entities.Officer;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class JpaOfficerDAOTest {
	
	
	    @Autowired 
	    private OfficerDAO dao;

	    @Autowired
	    private JdbcTemplate template;

	    @Test
	    public void testSave() throws Exception {
	        Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");
	        officer = dao.save(officer);
	        assertNotNull(officer.getId());
	    }

	    @Test
	    public void findOneThatExists() throws Exception {
	        template.query("select id from officers", (rs, num) -> rs.getInt("id"))
	                .forEach(id -> {
	                    Optional<Officer> officer = dao.findById(id);
	                    assertTrue(officer.isPresent());
	                    assertEquals(id, officer.get().getId());
	                });
	    }

	    @Test
	    public void findOneThatDoesNotExist() throws Exception {
	        Optional<Officer> officer = dao.findById(999);
	        assertFalse(officer.isPresent());
	    }

	    @Test
	    public void findAll() throws Exception {
	        List<String> dbNames = dao.findAll().stream()
	                                  .map(Officer::getLast)
	                                  .collect(Collectors.toList());
	        assertThat(dbNames, containsInAnyOrder("Kirk", "Picard", "Sisko", "Janeway", "Archer"));
	    }

	    @Test
	    public void count() throws Exception {
	        assertEquals(5, dao.count());
	    }

	    @Test
	    public void delete() throws Exception {
	        template.query("select id from officers", (rs, num) -> rs.getInt("id"))
	                .forEach(id -> {
	                    Optional<Officer> officer = dao.findById(id);
	                    assertTrue(officer.isPresent());
	                    dao.delete(officer.get());
	                });
	        assertEquals(0, dao.count());
	    }

	    @Test
	    public void existsById() throws Exception {
	        template.query("select id from officers", (rs, num) -> rs.getInt("id"))
	                .forEach(id -> assertTrue(String.format("%d should exist", id),
	                        dao.existsById(id)));
	    }

	    @Test
	    public void doesNotExist() {
	        List<Integer> ids = template.query("select id from officers",
	                                           (rs, num) -> rs.getInt("id"));
	        assertThat(ids, not(contains(999)));
	        assertFalse(dao.existsById(999));
	    }
	}
	
	


