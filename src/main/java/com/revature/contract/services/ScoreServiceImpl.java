package com.revature.contract.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.revature.contract.data.AssociateDAO;
import com.revature.contract.data.ScoreDAO;
import com.revature.contract.models.Associate;
import com.revature.contract.models.Score;

public class ScoreServiceImpl implements ScoreService {
	private AssociateDAO associateDao;
	private ScoreDAO scoreDao;
	
	public ScoreServiceImpl(AssociateDAO associateDao, ScoreDAO scoreDao) {
		this.associateDao = associateDao;
		this.scoreDao = scoreDao;
	}
	
	
	@Override
	public Associate submitScore(Score score, int associateId) {
		Optional<Associate> associateOpt = associateDao.findById(associateId);
		if (associateOpt.isPresent()) {
			scoreDao.save(score, associateId);
			associateOpt = associateDao.findById(associateId);
			return associateOpt.get();
		}
		return null;
	}

	@Override
	public Map<String, Double> viewScoresByAssociateId(int associateId) {
		Optional<Associate> associateOpt = associateDao.findById(associateId);
		if (associateOpt.isPresent()) {
			Associate associate = associateOpt.get();
			Map<String,Double> scores = new HashMap<>();
			scores.put("overall", associate.calculateOverallScore());
			
			List<String> themes = associate.getRubrics().stream()
					.map(rubric -> rubric.getRubricTheme().getTheme())
					.distinct()
					.collect(Collectors.toList());
			List<Integer> weeks = associate.getActualScores().stream()
					.map(score -> score.getWeek())
					.distinct()
					.collect(Collectors.toList());
			
			for (String theme : themes) {
				scores.put(theme, associate.calculateScoreByRubricTheme(theme));
			}
			
			for (Integer week : weeks) {
				scores.put("week " + week, associate.calculateScoreByWeek(week));
			}
			
			return scores;
		}
		return null;
	}

}
