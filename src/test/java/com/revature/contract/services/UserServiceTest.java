package com.revature.contract.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.contract.data.AssociateDAO;
import com.revature.contract.data.RubricDAO;
import com.revature.contract.models.Associate;
import com.revature.contract.models.Rubric;
import com.revature.contract.models.RubricTheme;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	private AssociateDAO associateDao;
	@Mock
	private RubricDAO rubricDao;
	@InjectMocks
	private UserServiceImpl userServ;
	
	private static List<RubricTheme> mockThemes;
	
	@BeforeAll
	public static void setUp() {
		mockThemes = new LinkedList<>();
		
		RubricTheme theme = new RubricTheme();
		mockThemes.add(theme);
	}
	
	// create user
	@Test
	public void createUserSuccess() {
		Associate mockAssociate = new Associate();
		when(associateDao.save(mockAssociate)).thenReturn(mockAssociate);
		when(rubricDao.findAllThemes()).thenReturn(mockThemes);
		mockAssociate = userServ.createUser(mockAssociate);
		assertFalse(mockAssociate.getRubrics().isEmpty());
	}
	
	@Test
	public void createUserFail() {
		Associate mockAssociate = new Associate();
		when(associateDao.save(mockAssociate)).thenReturn(null);
		mockAssociate = userServ.createUser(mockAssociate);
		assertNull(mockAssociate);
	}
	
	// get rubric themes
	@Test
	public void getRubricThemes() {
		when(rubricDao.findAllThemes()).thenReturn(Collections.emptyList());
		assertNotNull(userServ.getRubricThemes());
	}
	
	// log in
	@Test
	public void logInSuccess() {
		Associate mockAssociate = new Associate();
		mockAssociate.setFirstName("abc");
		mockAssociate.setLastName("def");
		when(associateDao.findBySecretCode("abcdefgh")).thenReturn(Optional.of(mockAssociate));
		
		assertNotNull(userServ.logIn("abc", "def", "abcdefgh"));
	}
	
	@Test
	public void logInSecretCodeFail() {
		when(associateDao.findBySecretCode("test")).thenReturn(Optional.empty());
		assertNull(userServ.logIn("abc", "def", "test"));
	}
	
	@Test
	public void logInFirstNameFail() {
		Associate mockAssociate = new Associate();
		mockAssociate.setFirstName("abc");
		mockAssociate.setLastName("def");
		when(associateDao.findBySecretCode("abcdefgh")).thenReturn(Optional.of(mockAssociate));
		
		assertNull(userServ.logIn("test", "def", "abcdefgh"));
	}
	
	@Test
	public void logInLastNameFail() {
		Associate mockAssociate = new Associate();
		mockAssociate.setFirstName("abc");
		mockAssociate.setLastName("def");
		when(associateDao.findBySecretCode("abcdefgh")).thenReturn(Optional.of(mockAssociate));
		
		assertNull(userServ.logIn("abc", "test", "abcdefgh"));
	}
	
	// submit rubric
	@Test
	public void submitRubricSuccess() {
		Associate mockAssociate = new Associate();
		Rubric mockRubric = new Rubric();
		when(associateDao.findById(0)).thenReturn(Optional.of(mockAssociate));
		when(rubricDao.save(mockRubric, 0)).thenReturn(mockRubric);
		
		assertNotNull(userServ.submitRubric(mockRubric, 0));
	}
	
	@Test
	public void submitRubricAssociateNotExisting() {
		when(associateDao.findById(0)).thenReturn(Optional.empty());
		
		Rubric mockRubric = new Rubric();
		assertNull(userServ.submitRubric(mockRubric, 0));
		verify(rubricDao, never()).save(any(Rubric.class));
	}
}