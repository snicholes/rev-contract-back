package com.revature.contract.data;

import java.util.Collection;

import com.revature.contract.models.Score;

public interface ScoreDAO extends GenericDAO<Score> {
	Score save(Score s, int associateId);
	Collection<Score> findByAssociateId(int id);
}
