package com.revature.contract.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.contract.data.AssociateDAO;
import com.revature.contract.data.ScoreDAO;
import com.revature.contract.models.Associate;
import com.revature.contract.models.Rubric;
import com.revature.contract.models.Score;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceTest {
	@Mock
	public AssociateDAO associateDao;
	@Mock
	public ScoreDAO scoreDao;
	@InjectMocks
	public ScoreServiceImpl scoreServ;
	
	public Associate mockAssociate;
	
	@BeforeEach
	public void setupAssociate() {
		mockAssociate = new Associate();
		mockAssociate.setRubrics(List.of(new Rubric()));
		
		List<Score> scores = new LinkedList<>();
		scores.add(new Score());
		mockAssociate.setActualScores(scores);
	}
	
	@Test
	public void submitScoreSuccess() {
		Score mockScore = new Score();
		
		Associate mockAssociateWithScore = mockAssociate;
		List<Score> scores = mockAssociateWithScore.getActualScores();
		scores.add(mockScore);
		mockAssociateWithScore.setActualScores(scores);
		
		when(associateDao.findById(0))
			.thenReturn(Optional.of(mockAssociate))
			.thenReturn(Optional.of(mockAssociateWithScore));
		when(scoreDao.save(mockScore,0)).thenReturn(mockScore);
		
		assertEquals(mockAssociateWithScore, scoreServ.submitScore(mockScore, 0));
	}
	
	@Test
	public void submitScoreUserDoesNotExist() {
		when(associateDao.findById(0)).thenReturn(Optional.empty());
		
		assertNull(scoreServ.submitScore(new Score(), 0));
		
		verify(scoreDao, never()).save(new Score(), 0);
	}
	
	// TODO test for submitScoreSaveFailed
	
	@Test
	public void viewScoresByAssociateIdSuccess() {
		when(associateDao.findById(0)).thenReturn(Optional.of(mockAssociate));
		
		Map<String, Double> actual = scoreServ.viewScoresByAssociateId(0);
		
		assertTrue(actual.containsKey("overall"));
		assertTrue(actual.containsKey(""));
		assertTrue(actual.containsKey("week 0"));
	}
}
