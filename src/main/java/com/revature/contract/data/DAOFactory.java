package com.revature.contract.data;

import com.revature.contract.data.postgres.AssociatePostgres;
import com.revature.contract.data.postgres.RubricPostgres;
import com.revature.contract.data.postgres.ScorePostgres;
import com.revature.contract.utils.ConnectionType;

public class DAOFactory {
	private static ConnectionType connType = ConnectionType.DEV;
	
	public static AssociateDAO getAssociateDAO() {
		return new AssociatePostgres(connType);
	}
	public static RubricDAO getRubricDAO() {
		return new RubricPostgres(connType);
	}
	public static ScoreDAO getScoreDAO() {
		return new ScorePostgres(connType);
	}
}
