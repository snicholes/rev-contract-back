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

import com.revature.contract.data.postgres.AssociatePostgres;
import com.revature.contract.models.Associate;
import com.revature.contract.utils.ConnectionType;
import com.revature.contract.utils.ConnectionUtil;

public class AssociateDAOTest {
	private AssociatePostgres associateDao;
	
	@BeforeEach
	public void setUp() {
		associateDao = new AssociatePostgres(ConnectionType.TEST);
		
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
		Associate associate = new Associate();
		associate = associateDao.save(associate);
		assertNotNull(associate);
		assertTrue(associate.getId() != 0);
	}
	
	@Test
	public void updateSuccess() {
		Associate associate = new Associate();
		associate.setId(1);
		associate.setFirstName("Patricia");
		associate.setLastName("Skethson");
		associate = associateDao.save(associate);
		assertNotNull(associate);
		assertTrue(associate.getId() == 1 && associate.getLastName().equals("Skethson"));
	}
	
	@Test
	public void updateNothingChanged() {
		Associate associate = new Associate();
		associate.setId(2);
		associate.setFirstName("Viki");
		associate.setLastName("Cooksley");
		associate = associateDao.save(associate);
		assertNotNull(associate);
		assertTrue(associate.getId() == 2);
	}
	
	// findById
	@Test
	public void findByIdExists() {
		Optional<Associate> associate = associateDao.findById(1);
		assertTrue(associate.isPresent());
		assertFalse(associate.get().getRubrics().isEmpty());
		assertFalse(associate.get().getActualScores().isEmpty());
	}
	
	@Test
	public void findByIdDoesNotExist() {
		Optional<Associate> associate = associateDao.findById(0);
		assertFalse(associate.isPresent());
	}
	
	// findAll
	@Test
	public void findAllAssociates() {
		Collection<Associate> associates = associateDao.findAll();
		assertTrue(associates.size() > 1 && associates.size() < 5);
	}
	
	// delete
	@Test
	public void deleteSuccess() {
		Associate associate = associateDao.findById(3).get();
		associate = associateDao.delete(associate);
		assertNotNull(associate);
	}
	
	@Test
	public void deleteDoesNotExist() {
		Associate associate = new Associate();
		assertNull(associateDao.delete(associate));
	}
	
	// findBySecretCode
	@Test
	public void findByCodeSuccess() {
		String code = "403446";
		Optional<Associate> associate = associateDao.findBySecretCode(code);
		assertTrue(associate.isPresent() && "Patricia".equals(associate.get().getFirstName()));
	}
	
	@Test
	public void findByCodeDoesNotExist() {
		String code = "aaaaaa";
		Optional<Associate> associate = associateDao.findBySecretCode(code);
		assertFalse(associate.isPresent());
	}
}
