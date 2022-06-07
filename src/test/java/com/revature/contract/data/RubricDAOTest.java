package com.revature.contract.data;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revature.contract.data.postgres.RubricPostgres;
import com.revature.contract.models.Rubric;
import com.revature.contract.models.RubricTheme;
import com.revature.contract.utils.ConnectionType;
import com.revature.contract.utils.ConnectionUtil;

public class RubricDAOTest {
	private RubricPostgres rubricDao;
	
	@BeforeEach
	public void setUp() {
		rubricDao = new RubricPostgres(ConnectionType.TEST);
		
		try (Connection conn = ConnectionUtil.getConnectionUtil(ConnectionType.TEST).getConnection()) {
			RunScript.execute(conn, new FileReader("src/test/resources/setup.sql"));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// save
	@Test
	public void createSuccess() {
		RubricTheme theme = new RubricTheme();
		theme.setId(1);
		theme.setTheme("Product Marketing");
		theme.setDescription("incentivize back-end bandwidth");
		
		Rubric rubric = new Rubric();
		rubric.setRubricTheme(theme);
		
		rubric = rubricDao.save(rubric, 1);
		assertNotNull(rubric);
		assertTrue(rubric.getId() != 0);
	}
	
	@Test
	public void updateSuccess() {
		RubricTheme theme = new RubricTheme();
		theme.setId(1);
		theme.setTheme("Product Marketing");
		theme.setDescription("incentivize back-end bandwidth");
		
		Rubric rubric = new Rubric();
		rubric.setId(1);
		rubric.setRubricTheme(theme);
		rubric.setScore(1);
		rubric.setDescription("new description");
		rubric = rubricDao.save(rubric,1);
		assertNotNull(rubric);
		assertTrue(rubric.getId() == 1 && "new description".equals(rubric.getDescription()));
	}
	
	@Test
	public void updateNothingChanged() {
		RubricTheme theme = new RubricTheme();
		theme.setId(1);
		theme.setTheme("Product Marketing");
		theme.setDescription("incentivize back-end bandwidth");
		
		Rubric rubric = new Rubric();
		rubric.setId(2);
		rubric.setRubricTheme(theme);
		rubric.setScore(2);
		rubric.setDescription("Maecenas rhoncus aliquam lacus. Morbi quis tortor id defaulta ultrices aliquet.");

		rubric = rubricDao.save(rubric,1);
		assertNotNull(rubric);
		assertTrue(rubric.getId() == 2);
	}
	
	// findById
	@Test
	public void findByIdExists() {
		Optional<Rubric> rubric = rubricDao.findById(1);
		assertTrue(rubric.isPresent());
	}
	
	@Test
	public void findByIdDoesNotExist() {
		Optional<Rubric> rubric = rubricDao.findById(0);
		assertFalse(rubric.isPresent());
	}
	
	// findAll
	@Test
	public void findAllRubrics() {
		Collection<Rubric> rubrics = rubricDao.findAll();
		assertFalse(rubrics.isEmpty());
	}
	
	// findByAssociateId
	@Test
	public void findRubricByAssociateIdExists() {
		Collection<Rubric> rubrics = rubricDao.findByAssociateId(1);
		assertFalse(rubrics.isEmpty());
	}
	
	@Test
	public void findRubricsByAssociateIdDoesNotExist() {
		Collection<Rubric> rubrics = rubricDao.findByAssociateId(0);
		assertNotNull(rubrics);
		assertTrue(rubrics.isEmpty());
	}
	
	// findThemeById
	@Test
	public void findRubricThemeByIdExists() {
		RubricTheme theme = rubricDao.findThemeById(1);
		assertNotNull(theme);
	}
	
	@Test
	public void findRubricThemeByIdDoesNotExist() {
		RubricTheme theme = rubricDao.findThemeById(0);
		assertNull(theme);
	}
	
	// findThemeByName
	@Test
	public void findRubricThemeByNameExists() {
		RubricTheme theme = rubricDao.findThemeByName("Product Marketing");
		assertNotNull(theme);
	}
	
	@Test
	public void findRubricThemeByNameDoesNotExist() {
		RubricTheme theme = rubricDao.findThemeByName("test");
		assertNull(theme);
	}
	
	// findAllThemes
	@Test
	public void findAllRubricThemes() {
		Collection<RubricTheme> themes = rubricDao.findAllThemes();
		assertNotNull(themes);
		assertFalse(themes.isEmpty());
	}
	
	// delete
	@Test
	public void deleteRubricExists() {
		RubricTheme theme = new RubricTheme();
		theme.setId(1);
		theme.setTheme("Product Marketing");
		theme.setDescription("incentivize back-end bandwidth");
		
		Rubric rubric = new Rubric();
		rubric.setId(3);
		rubric.setRubricTheme(theme);
		rubric.setDescription("Maecenas leo odio, condimentum id, luctus nec, molestie sed, "
				+ "justo. Pellentesque viverra pede ac diam.");
		rubric.setScore(3);
		
		rubric = rubricDao.delete(rubric);
		assertNotNull(rubric);
	}
	
	@Test
	public void deleteRubricDoesNotExist() {
		Rubric rubric = new Rubric();
		rubric = rubricDao.delete(rubric);
		assertNull(rubric);
	}
}
