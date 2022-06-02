package com.revature.contract.models;

import java.util.Objects;

public class RubricTheme {
	private int id;
	private String theme;
	private String description;
	
	public RubricTheme() {
		id = 0;
		theme = "";
		description = "";
	}

	@Override
	public String toString() {
		return "RubricTheme [id=" + id + ", theme=" + theme + ", description=" + description + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, theme);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RubricTheme other = (RubricTheme) obj;
		return Objects.equals(description, other.description) && id == other.id && Objects.equals(theme, other.theme);
	}
}
