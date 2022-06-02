package com.revature.contract.data;

import com.revature.contract.data.postgres.AssociatePostgres;
import com.revature.contract.data.postgres.RubricPostgres;
import com.revature.contract.data.postgres.ScorePostgres;

public class DAOFactory {
	public static AssociateDAO getAssociateDAO() {
		return new AssociatePostgres();
	}
	public static RubricDAO getRubricDAO() {
		return new RubricPostgres();
	}
	public static ScoreDAO getScoreDAO() {
		return new ScorePostgres();
	}
}
