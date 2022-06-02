package com.revature.contract.services;

import java.util.Collection;

import com.revature.contract.models.Associate;
import com.revature.contract.models.Rubric;
import com.revature.contract.models.RubricTheme;

public interface UserService {
	public Associate createUser(Associate associate);
	public Collection<RubricTheme> getRubricThemes();
	public Associate submitRubric(Rubric rubric, int associateId);
	public Associate logIn(String firstName, String lastName, String secretCode);
}
