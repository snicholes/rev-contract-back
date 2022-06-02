package com.revature.contract.services;

import java.util.Map;

import com.revature.contract.models.Associate;
import com.revature.contract.models.Score;

public interface ScoreService {
	public Associate submitScore(Score score, int associateId);
	public Map<String, Double> viewScoresByAssociateId(int associateId);
}
