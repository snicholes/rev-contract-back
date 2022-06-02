package com.revature.contract.data.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.revature.contract.data.AssociateDAO;
import com.revature.contract.data.DAOFactory;
import com.revature.contract.data.RubricDAO;
import com.revature.contract.data.ScoreDAO;
import com.revature.contract.models.Associate;
import com.revature.contract.models.Rubric;
import com.revature.contract.models.Score;
import com.revature.contract.utils.ConnectionUtil;

public class AssociatePostgres implements AssociateDAO {
	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();

	@Override
	public Associate save(Associate t) {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			conn.setAutoCommit(false);

			String sql = "";
			// if the user with that id is present in the DB then
			// we are updating rather than inserting
			boolean updating = findById(t.getId()).isPresent();
			if (updating) {
				// update
				sql = "update associate set first_name=?, last_name=? " + "where id=?";
			} else {
				// insert
				sql = "insert into associate (id, first_name, last_name, secret_code) " + "values (default, ?, ?, ?)";
			}

			String[] keys = { "id" };
			PreparedStatement pStmt = conn.prepareStatement(sql, keys);
			pStmt.setString(1, t.getFirstName());
			pStmt.setString(2, t.getLastName());
			if (updating)
				pStmt.setInt(3, t.getId());
			else
				pStmt.setString(3, generateCode());

			int rowsUpdated = pStmt.executeUpdate();

			if (updating) {
				if (rowsUpdated == 1) {
					conn.commit();
				} else {
					conn.rollback();
				}
			} else {
				ResultSet generatedKeys = pStmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					int newId = generatedKeys.getInt(1);
					t.setId(newId);
				}
				
			}
		} catch (SQLException e) {
			try {
				e.printStackTrace();
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return t;
	}

	@Override
	public Optional<Associate> findById(int id) {
		Associate associate = new Associate();

		try (Connection conn = connUtil.getConnection()) {
			String sql = "select id, first_name, last_name from associate where id=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, id);

			ResultSet resultSet = pStmt.executeQuery();
			if (resultSet.next()) {
				associate.setId(id);
				associate.setFirstName(resultSet.getString("first_name"));
				associate.setLastName(resultSet.getString("last_name"));
			} else {
				return Optional.empty();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		associate.setRubrics((List<Rubric>) DAOFactory.getRubricDAO().findByAssociateId(id));
		associate.setActualScores((List<Score>) DAOFactory.getScoreDAO().findByAssociateId(id));

		return Optional.of(associate);
	}

	@Override
	public Collection<Associate> findAll() {
		List<Associate> associates = new LinkedList<>();
		try (Connection conn = connUtil.getConnection()) {
			String sql = "select id, first_name, last_name from associate";
			Statement stmt = conn.createStatement();

			ResultSet resultSet = stmt.executeQuery(sql);
			while (resultSet.next()) {
				Associate associate = new Associate();
				associate.setId(resultSet.getInt("id"));
				associate.setFirstName(resultSet.getString("first_name"));
				associate.setLastName(resultSet.getString("last_name"));
				associate.setRubrics((List<Rubric>) DAOFactory.getRubricDAO().findByAssociateId(associate.getId()));
				associate.setActualScores((List<Score>) DAOFactory.getScoreDAO().findByAssociateId(associate.getId()));
				associates.add(associate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return associates;
	}

	@Override
	public Associate delete(Associate t) {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			conn.setAutoCommit(false);
 
			if (t.getRubrics().size()>0) {
				RubricDAO rubricDao = DAOFactory.getRubricDAO();
				for (Rubric r : t.getRubrics()) {
					rubricDao.delete(r);
				}
			}
			if (t.getActualScores().size()>0) {
				ScoreDAO scoreDao = DAOFactory.getScoreDAO();
				for (Score s : t.getActualScores()) {
					scoreDao.delete(s);
				}
			}
			String sql = "delete from associate where id = ?";

			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, t.getId());

			int rowsUpdated = pStmt.executeUpdate();

			if (rowsUpdated == 1) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			try {
				e.printStackTrace();
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return t;
	}

	@Override
	public Optional<Associate> findBySecretCode(String code) {
		Associate associate = new Associate();

		try (Connection conn = connUtil.getConnection()) {
			String sql = "select id, first_name, last_name from associate where secret_code=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, code);

			ResultSet resultSet = pStmt.executeQuery();
			if (resultSet.next()) {
				associate.setId(resultSet.getInt("id"));
				associate.setFirstName(resultSet.getString("first_name"));
				associate.setLastName(resultSet.getString("last_name"));
			} else {
				return Optional.empty();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		associate.setRubrics((List<Rubric>) DAOFactory
				.getRubricDAO().findByAssociateId(associate.getId()));
		associate.setActualScores((List<Score>) DAOFactory
				.getScoreDAO().findByAssociateId(associate.getId()));

		return Optional.of(associate);
	}

	private String generateCode() {
		String code = "";

		Random rand = new Random();

		for (int i = 0; i < 6; i++) {
			code += String.valueOf(rand.nextInt(10) * Math.pow(10, i));
		}

		return code;
	}
}
