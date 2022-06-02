package com.revature.contract.models;

import java.util.Objects;

public class Rubric {
	private int id;
	private RubricTheme rubricTheme;
	private int score;
	private String description;
	
	
	public Rubric() {
		id = 0;
		rubricTheme = new RubricTheme();
		score = 0;
		description = "";
	}
	
	@Override
	public String toString() {
		return "Rubric [id=" + id + ", rubricTheme=" + rubricTheme + ", score=" + score + ", description=" + description
				+ "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RubricTheme getRubricTheme() {
		return rubricTheme;
	}

	public void setRubricTheme(RubricTheme rubricTheme) {
		this.rubricTheme = rubricTheme;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, rubricTheme, score);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rubric other = (Rubric) obj;
		return Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(rubricTheme, other.rubricTheme) && score == other.score;
	}
}
