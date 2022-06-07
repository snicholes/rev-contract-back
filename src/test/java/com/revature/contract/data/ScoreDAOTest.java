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

import com.revature.contract.data.postgres.ScorePostgres;
import com.revature.contract.models.Score;
import com.revature.contract.utils.ConnectionType;
import com.revature.contract.utils.ConnectionUtil;

public class ScoreDAOTest {
	private ScorePostgres scoreDao;
	
	@BeforeEach
	public void setUp() {
		scoreDao = new ScorePostgres(ConnectionType.TEST);
		
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
		Score score = new Score();
		score.setRubricTheme("Product Marketing");
		score = scoreDao.save(score, 1);
		assertNotNull(score);
		assertTrue(score.getId() != 0);
	}
	
	@Test
	public void updateSuccess() {
		Score score = new Score();
		score.setId(1);
		score.setWeek(2);
		score.setRubricTheme("Theology");
		score.setNote("Peruvian Dodder Improved");
		score.setValue(4);
		score = scoreDao.save(score, 3);
		assertNotNull(score);
		assertTrue(score.getId() == 1 && "Peruvian Dodder Improved".equals(score.getNote()));
	}
	
	@Test
	public void updateFieldCannotBeUpdated() {
		Score score = new Score();
		score.setId(1);
		score.setWeek(2);
		score.setRubricTheme("Theology");
		score.setNote("Peruvian Dodder");
		score.setValue(5);
		score = scoreDao.save(score, 3);
		assertNotNull(score);
		assertTrue(score.getId() == 1 && score.getValue() == 5);
	}
	
	@Test
	public void updateNothingChanged() {
		//(default, 3, 'Bog White Violet', 5, 2, 4),
		
		Score score = new Score();
		score.setId(2);
		score.setWeek(3);
		score.setNote("Bog White Violet");
		score.setRubricTheme("Strategic HR");
		score.setValue(4);

		score = scoreDao.save(score, 2);
		assertNotNull(score);
		assertTrue(score.getId() == 2);
	}
	
	// findById
	@Test
	public void findScoreByIdExists() {
		Optional<Score> score = scoreDao.findById(1);
		assertTrue(score.isPresent());
	}
	
	@Test
	public void findScoreByIdDoesNotExist() {
		Optional<Score> score = scoreDao.findById(0);
		assertFalse(score.isPresent());
	}
	
	// findAll
	@Test
	public void findAllScores() {
		Collection<Score> scores = scoreDao.findAll();
		assertFalse(scores.isEmpty());
	}
	
	// findByAssociateId
	@Test
	public void findScoresByAssociateIdExists() {
		Collection<Score> scores = scoreDao.findByAssociateId(1);
		assertFalse(scores.isEmpty());
	}
	
	@Test
	public void findScoresByAssociateIdDoesNotExist() {
		Collection<Score> scores = scoreDao.findByAssociateId(0);
		assertNotNull(scores);
		assertTrue(scores.isEmpty());
	}
	
	// delete
	@Test
	public void deleteScoreExists() {
		Score score = new Score();
		score.setId(3);
		
		score = scoreDao.delete(score);
		assertNotNull(score);
	}
	
	@Test
	public void deleteScoreDoesNotExist() {
		Score score = new Score();
		score = scoreDao.delete(score);
		assertNull(score);
	}
}
