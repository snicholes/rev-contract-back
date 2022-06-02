package com.revature.contract.models;

import java.util.Objects;

public class Score {
	private int id;
	private int value;
	private String note;
	private int week;
	private String rubricTheme;
	
	public Score() {
		id = 0;
		value = 0;
		note = "";
		week = 0;
		rubricTheme = "";
	}

	@Override
	public String toString() {
		return "Score [id=" + id + ", value=" + value + ", note=" + note + ", week=" + week + ", rubricTheme="
				+ rubricTheme + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public String getRubricTheme() {
		return rubricTheme;
	}

	public void setRubricTheme(String rubricTheme) {
		this.rubricTheme = rubricTheme;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, note, rubricTheme, value, week);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Score other = (Score) obj;
		return id == other.id && Objects.equals(note, other.note) && Objects.equals(rubricTheme, other.rubricTheme)
				&& value == other.value && week == other.week;
	}
}
