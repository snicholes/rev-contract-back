package com.revature.contract.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Associate {
	private int id;
	private String firstName;
	private String lastName;
	private List<Rubric> rubrics;
	private List<Score> actualScores;
	
	public Associate() {
		id = 0;
		firstName = "";
		lastName = "";
		rubrics = new ArrayList<>();
		actualScores = new ArrayList<>();
	}
	
	public double calculateOverallScore() {
		double calcScore = 0.0;
		for (Score indivScore : actualScores) {
			calcScore += indivScore.getValue();
		}
		calcScore = (calcScore*20)/actualScores.size();
		return calcScore;
	}
	
	public double calculateScoreByWeek(int week) {
		double calcScore = 0.0;
		int count = 0;
		for (Score indivScore : actualScores) {
			if (indivScore.getWeek()==week) {
				count++;
				calcScore += indivScore.getValue();
			}
		}
		calcScore = (calcScore*20)/count;
		return calcScore;
	}
	
	public double calculateScoreByRubricTheme(String theme) {
		double calcScore = 0.0;
		int count = 0;
		for (Score indivScore : actualScores) {
			if (indivScore.getRubricTheme().equals(theme)) {
				count++;
				calcScore += indivScore.getValue();
			}
		}
		calcScore = (calcScore*20)/count;
		return calcScore;
	}
	
	public void addRubricValue(Rubric rubric) {
		rubrics.add(rubric);
	}
	
	public void editRubricValue(Rubric rubric) {
		for (Rubric r : rubrics) {
			if (r.getRubricTheme().equals(rubric.getRubricTheme())
					&& r.getScore()==rubric.getScore()) {
				r.setDescription(rubric.getDescription());
			}
		}
	}

	@Override
	public String toString() {
		return "Associate [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", rubrics=" + rubrics
				+ ", actualScores=" + actualScores + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Rubric> getRubrics() {
		return rubrics;
	}

	public void setRubrics(List<Rubric> rubrics) {
		this.rubrics = rubrics;
	}

	public List<Score> getActualScores() {
		return actualScores;
	}

	public void setActualScores(List<Score> actualScores) {
		this.actualScores = actualScores;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actualScores, firstName, id, lastName, rubrics);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Associate other = (Associate) obj;
		return Objects.equals(actualScores, other.actualScores) && Objects.equals(firstName, other.firstName)
				&& id == other.id && Objects.equals(lastName, other.lastName) && Objects.equals(rubrics, other.rubrics);
	}
}
