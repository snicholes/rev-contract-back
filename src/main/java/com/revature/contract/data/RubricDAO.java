package com.revature.contract.data;

import java.util.Collection;

import com.revature.contract.models.Rubric;
import com.revature.contract.models.RubricTheme;

public interface RubricDAO extends GenericDAO<Rubric> {
	Rubric save(Rubric r, int associateId);
	Collection<Rubric> findByAssociateId(int associateId);
	public Collection<RubricTheme> findAllThemes();
	public RubricTheme findThemeById(int themeId);
	public RubricTheme findThemeByName(String name);
}
