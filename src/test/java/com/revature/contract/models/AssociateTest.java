package com.revature.contract.models;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AssociateTest {
	private static Associate passingAssociate;
	private static Associate midAssociate;
	private static Associate failingAssociate;
	private static String[] rubricNames = {"abc", "def", "ghi", "jkl", "mno"};
	
	@BeforeAll
	public static void setUpAssociate() {
		passingAssociate = new Associate();
		midAssociate = new Associate();
		failingAssociate = new Associate();
		List<Rubric> rubrics = new LinkedList<>();
		
		int count=1;
		for (int i=1;i<=5;i++) {
			RubricTheme theme = new RubricTheme();
			theme.setId(i);
			theme.setTheme(rubricNames[i-1]);
			for (int j=1;j<=5;j++) {
				Rubric rubric = new Rubric();
				rubric.setId(count++);
				rubric.setScore(j);
				rubric.setDescription("asdfghjkl");
				rubric.setRubricTheme(theme);
				rubrics.add(rubric);
			}
		}
		passingAssociate.setRubrics(rubrics);
		midAssociate.setRubrics(rubrics);
		failingAssociate.setRubrics(rubrics);
		
		List<Score> passingScores = new LinkedList<>();
		List<Score> midScores = new LinkedList<>();
		List<Score> failingScores = new LinkedList<>();
		String[] whichScores = {"pass", "mid", "fail"};
		
		for (String scoreType : whichScores) {
			for (int i=1;i<=5;i++) {
				Score score = new Score();
				score.setId(i);
				// week will be either 0 or 1
				score.setWeek(i%2);
				score.setRubricTheme(rubricNames[i-1]);
				
				switch (scoreType) {
				case "pass":
					score.setValue(5);
					passingScores.add(score);
					break;
				case "mid":
					score.setValue((i%2==0)?4:2);
					midScores.add(score);
					break;
				case "fail":
					score.setValue(0);
					failingScores.add(score);
					break;
				}
			}
		}
		passingAssociate.setActualScores(passingScores);
		midAssociate.setActualScores(midScores);
		failingAssociate.setActualScores(failingScores);
	}
	
	@Test
	public void calculateOverallScorePassing() {
		double expected = 100.0;
		double actual = passingAssociate.calculateOverallScore();
		assertEquals(expected,actual);
	}
	
	@Test
	public void calculateOverallScoreMid() {
		double expected = 56.0;
		double actual = midAssociate.calculateOverallScore();
		assertEquals(expected,actual);
	}
	
	@Test
	public void calculateOverallScoreFailing() {
		double expected = 0.0;
		double actual = failingAssociate.calculateOverallScore();
		assertEquals(expected,actual);
	}
	
	@Test
	public void calculateScoreByWeekPassing() {
		double expected = 100.0;
		double actual = passingAssociate.calculateScoreByWeek(0);
		assertEquals(expected,actual);
	}
	
	@Test
	public void calculateScoreByWeekMid() {
		double expected = 80.0;
		double actual = midAssociate.calculateScoreByWeek(0);
		assertEquals(expected,actual);
	}
	
	@Test
	public void calculateScoreByWeekFailing() {
		double expected = 0.0;
		double actual = failingAssociate.calculateScoreByWeek(0);
		assertEquals(expected,actual);
	}
	
	@Test
	public void calculateScoreByThemePassing() {
		double expected = 100.0;
		double actual = passingAssociate.calculateScoreByRubricTheme(rubricNames[0]);
		assertEquals(expected,actual);
	}
	
	@Test
	public void calculateScoreByThemeMid() {
		double expected = 40.0;
		double actual = midAssociate.calculateScoreByRubricTheme(rubricNames[0]);
		assertEquals(expected,actual);
	}
	
	@Test
	public void calculateScoreByThemeFailing() {
		double expected = 0.0;
		double actual = failingAssociate.calculateScoreByRubricTheme(rubricNames[0]);
		assertEquals(expected,actual);
	}
}
