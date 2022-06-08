package com.revature.contract.services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.revature.contract.data.AssociateDAO;
import com.revature.contract.data.RubricDAO;
import com.revature.contract.models.Associate;
import com.revature.contract.models.Rubric;
import com.revature.contract.models.RubricTheme;

public class UserServiceImpl implements UserService {
	private AssociateDAO associateDao;
	private RubricDAO rubricDao;
	
	public UserServiceImpl(AssociateDAO associateDao, RubricDAO rubricDao) {
		this.associateDao = associateDao;
		this.rubricDao = rubricDao;
	}

	@Override
	public Associate createUser(Associate associate) {
		associate = associateDao.save(associate);
		if (associate != null) {
			associate = initRubrics(associate);
		}
		return associate;
	}

	@Override
	public Collection<RubricTheme> getRubricThemes() {
		return rubricDao.findAllThemes();
	}

	@Override
	public Associate submitRubric(Rubric rubric, int associateId) {
		if (associateDao.findById(associateId).isPresent()) {
			rubricDao.save(rubric, associateId);
			return associateDao.findById(associateId).get();
		}
		
		return null;
	}

	@Override
	public Associate logIn(String firstName, String lastName, String secretCode) {
		Optional<Associate> associateOpt = associateDao.findBySecretCode(secretCode);
		if (associateOpt.isPresent()) {
			Associate associate = associateOpt.get();
			if (associate.getFirstName().equals(firstName) 
					&& associate.getLastName().equals(lastName)) {
				return associate;
			}
		}
		return null;
	}
	
	private Associate initRubrics(Associate associate) {
		List<RubricTheme> themes = (List<RubricTheme>) rubricDao.findAllThemes();
		
		List<Rubric> newRubrics = new LinkedList<>();
		for (RubricTheme theme : themes) {
			for (int score = 0; score<6; score++) {
				Rubric rubric = new Rubric();
				rubric.setRubricTheme(theme);
				rubric.setScore(score);
				associate.addRubricValue(rubric);
				newRubrics.add(rubric);
			}
		}
		rubricDao.saveAll(newRubrics, associate.getId());
		return associate;
	}

}
