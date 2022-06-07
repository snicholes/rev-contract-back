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

import com.revature.contract.data.DAOFactory;
import com.revature.contract.data.ScoreDAO;
import com.revature.contract.models.Score;
import com.revature.contract.utils.ConnectionType;
import com.revature.contract.utils.ConnectionUtil;

public class ScorePostgres implements ScoreDAO {
	private ConnectionUtil connUtil;

	public ScorePostgres(ConnectionType connType) {
		connUtil = ConnectionUtil.getConnectionUtil(connType);
	}
	
	@Override
	@Deprecated
	public Score save(Score t) {
		throw new RuntimeException("Associate ID required");
	}
	
	@Override
	public Score save(Score s, int associateId) {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			conn.setAutoCommit(false);

			String sql = "";
			// if the score with that id is present in the DB then
			// we are updating rather than inserting
			boolean updating = findById(s.getId()).isPresent();
			if (updating) {
				// update
				sql = "update score set "
						+ "note=? " + "where id=?";
			} else {
				// insert
				sql = "insert into score "
						+ "(id, week, note, rubric_id, associate_id, score_value) "
						+ "values (default, ?, ?, ?, ?, ?)";
			}

			String[] keys = { "id" };
			PreparedStatement pStmt = conn.prepareStatement(sql, keys);
			if (updating) pStmt.setString(1, s.getNote());
			else {
				pStmt.setInt(1, s.getWeek());
				pStmt.setString(2, s.getNote());
				pStmt.setInt(3, DAOFactory.getRubricDAO().findThemeByName(s.getRubricTheme()).getId());
				pStmt.setInt(4, associateId);
				pStmt.setInt(5, s.getValue());
			}

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
					s.setId(newId);
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
		return s;
	}

	@Override
	public Optional<Score> findById(int id) {
		Score score = new Score();
		
		try (Connection conn = connUtil.getConnection()) {
			String sql = "select "
					+ "score.id, "
					+ "week, "
					+ "note, "
					+ "rubric_id, "
					+ "associate_id, "
					+ "score_value, "
					+ "theme "
					+ "from score join rubric "
					+ "on score.rubric_id=rubric.id "
					+ "where score.id=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, id);

			ResultSet resultSet = pStmt.executeQuery();
			if (resultSet.next()) {
				score.setId(id);
				score.setRubricTheme(resultSet.getString("theme"));
				score.setNote(resultSet.getString("note"));
				score.setWeek(resultSet.getInt("week"));
				score.setValue(resultSet.getInt("value"));
			} else {
				return Optional.empty();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Optional.of(score);
	}

	@Override
	public Collection<Score> findAll() {
		List<Score> scores = new LinkedList<>();
		try (Connection conn = connUtil.getConnection()) {
			String sql = "select "
					+ "score.id, "
					+ "week, "
					+ "note, "
					+ "rubric_id, "
					+ "associate_id, "
					+ "score_value, "
					+ "theme "
					+ "from score join rubric "
					+ "on score.rubric_id=rubric.id";
			Statement stmt = conn.createStatement();

			ResultSet resultSet = stmt.executeQuery(sql);
			while (resultSet.next()) {
				Score score = new Score();
				score.setId(resultSet.getInt("id"));
				score.setRubricTheme(resultSet.getString("theme"));
				score.setNote(resultSet.getString("note"));
				score.setWeek(resultSet.getInt("week"));
				score.setValue(resultSet.getInt("value"));
				scores.add(score);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return scores;
	}

	@Override
	public Score delete(Score t) {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			conn.setAutoCommit(false);
 
			String sql = "delete from score where id = ?";

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
	public Collection<Score> findByAssociateId(int id) {
		List<Score> scores = new LinkedList<>();
		try (Connection conn = connUtil.getConnection()) {
			String sql = "select "
					+ "score.id, "
					+ "week, "
					+ "note, "
					+ "rubric_id, "
					+ "associate_id, "
					+ "score_value, "
					+ "theme "
					+ "from score join rubric "
					+ "on score.rubric_id=rubric.id "
					+ "where associate_id=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, id);

			ResultSet resultSet = pStmt.executeQuery();
			while (resultSet.next()) {
				Score score = new Score();
				score.setId(resultSet.getInt("id"));
				score.setRubricTheme(resultSet.getString("theme"));
				score.setNote(resultSet.getString("note"));
				score.setWeek(resultSet.getInt("week"));
				score.setValue(resultSet.getInt("value"));
				scores.add(score);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return scores;
	}

}
