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

import com.revature.contract.data.RubricDAO;
import com.revature.contract.models.Rubric;
import com.revature.contract.models.RubricTheme;
import com.revature.contract.utils.ConnectionType;
import com.revature.contract.utils.ConnectionUtil;

public class RubricPostgres implements RubricDAO {
	private ConnectionUtil connUtil;

	public RubricPostgres(ConnectionType connType) {
		connUtil = ConnectionUtil.getConnectionUtil(connType);
	}
	
	@Override
	@Deprecated
	public Rubric save(Rubric r) {
		throw new RuntimeException("Associate ID required");
	}
	
	@Override
	public Rubric save(Rubric r, int associateId) {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			conn.setAutoCommit(false);

			String sql = "";
			// if the rubric with that id is present in the DB then
			// we are updating rather than inserting
			boolean updating = findById(r.getId()).isPresent();
			if (updating) {
				// update
				sql = "update rubric_value set "
						+ "description=? " + "where id=?";
			} else {
				// insert
				sql = "insert into rubric_value "
						+ "(id, associate_id, rubric_id, score, description) "
						+ "values (default, ?, ?, ?, ?)";
			}

			String[] keys = { "id" };
			PreparedStatement pStmt = conn.prepareStatement(sql, keys);
			if (updating) {
				pStmt.setString(1, r.getDescription());
				pStmt.setInt(2, r.getId());
			} else {
				pStmt.setInt(1, associateId);
				pStmt.setInt(2, r.getRubricTheme().getId());
				pStmt.setInt(3, r.getScore());
				pStmt.setString(4, r.getDescription());
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
					r.setId(newId);
					conn.commit();
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
		return r;
	}

	@Override
	public Optional<Rubric> findById(int id) {
		Rubric rubric = new Rubric();

		try (Connection conn = connUtil.getConnection()) {
			String sql = "select "
					+ "id, "
					+ "rubric_id, score, "
					+ "rubric_value.description "
					+ "from rubric_value "
					+ "where id=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, id);

			ResultSet resultSet = pStmt.executeQuery();
			if (resultSet.next()) {
				rubric.setId(id);
				RubricTheme rubricTheme = findThemeById(resultSet.getInt("rubric_id"));
				rubric.setRubricTheme(rubricTheme);
				rubric.setScore(resultSet.getInt("score"));
				rubric.setDescription(resultSet.getString("description"));
			} else {
				return Optional.empty();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Optional.of(rubric);
	}

	@Override
	public Collection<Rubric> findAll() {
		List<Rubric> rubrics = new LinkedList<>();
		
		try (Connection conn = connUtil.getConnection()) {
			String sql = "select "
					+ "id, "
					+ "rubric_id, score, "
					+ "rubric_value.description "
					+ "from rubric_value";
			Statement stmt = conn.createStatement();

			ResultSet resultSet = stmt.executeQuery(sql);
			while (resultSet.next()) {
				Rubric rubric = new Rubric();
				rubric.setId(resultSet.getInt("id"));
				RubricTheme rubricTheme = findThemeById(resultSet.getInt("rubric_id"));
				rubric.setRubricTheme(rubricTheme);
				rubric.setScore(resultSet.getInt("score"));
				rubric.setDescription(resultSet.getString("description"));
				rubrics.add(rubric);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rubrics;
	}

	@Override
	public Rubric delete(Rubric t) {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			conn.setAutoCommit(false);
 
			String sql = "delete from rubric_value where id = ?";

			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, t.getId());

			int rowsUpdated = pStmt.executeUpdate();

			if (rowsUpdated == 1) {
				conn.commit();
			} else {
				conn.rollback();
				return null;
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
	public Collection<Rubric> findByAssociateId(int associateId) {
		List<Rubric> rubrics = new LinkedList<>();
		
		try (Connection conn = connUtil.getConnection()) {
			String sql = "select "
					+ "id, "
					+ "rubric_id, score, "
					+ "description "
					+ "from rubric_value "
					+ "where associate_id=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, associateId);

			ResultSet resultSet = pStmt.executeQuery();
			while (resultSet.next()) {
				Rubric rubric = new Rubric();
				rubric.setId(resultSet.getInt("id"));
				RubricTheme rubricTheme = findThemeById(resultSet.getInt("rubric_id"));
				rubric.setRubricTheme(rubricTheme);
				rubric.setScore(resultSet.getInt("score"));
				rubric.setDescription(resultSet.getString("description"));
				rubrics.add(rubric);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rubrics;
	}

	@Override
	public Collection<RubricTheme> findAllThemes() {
		List<RubricTheme> rubricThemes = new LinkedList<>();
		
		try (Connection conn = connUtil.getConnection()) {
			String sql = "select id, theme, description from rubric";
			Statement stmt = conn.createStatement();

			ResultSet resultSet = stmt.executeQuery(sql);
			while (resultSet.next()) {
				RubricTheme theme = new RubricTheme();
				theme.setId(resultSet.getInt("id"));
				theme.setTheme(resultSet.getString("theme"));
				theme.setDescription(resultSet.getString("description"));
				rubricThemes.add(theme);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rubricThemes;
	}

	@Override
	public RubricTheme findThemeById(int themeId) {
		RubricTheme theme = null;
		
		try (Connection conn = connUtil.getConnection()) {
			String sql = "select id, theme, description from rubric where id=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, themeId);

			ResultSet resultSet = pStmt.executeQuery();
			if (resultSet.next()) {
				theme = new RubricTheme();
				theme.setId(resultSet.getInt("id"));
				theme.setTheme(resultSet.getString("theme"));
				theme.setDescription(resultSet.getString("description"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return theme;
	}

	@Override
	public RubricTheme findThemeByName(String name) {
		RubricTheme theme = null;
		
		try (Connection conn = connUtil.getConnection()) {
			String sql = "select id, theme, description from rubric where theme=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, name);

			ResultSet resultSet = pStmt.executeQuery();
			if (resultSet.next()) {
				theme = new RubricTheme();
				theme.setId(resultSet.getInt("id"));
				theme.setTheme(resultSet.getString("theme"));
				theme.setDescription(resultSet.getString("description"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return theme;
	}

	@Override
	public Collection<Rubric> saveAll(Collection<Rubric> rubrics, int associateId) {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			conn.setAutoCommit(false);

			StringBuilder sql = new StringBuilder("insert into rubric_value ("
					+ "id,"
					+ "associate_id,"
					+ "rubric_id,"
					+ "score,"
					+ "description"
					+ ") values ");
			for (Rubric rubric : rubrics) {
				sql.append("(default,");
				sql.append(associateId + ",");
				sql.append(rubric.getRubricTheme().getId() + ",");
				sql.append(rubric.getScore() + ",");
				sql.append((rubric.getDescription().length()>0)?(rubric.getDescription()):("''"));
				sql.append("),");
			}
			sql.replace(sql.length()-1, sql.length(), ";");

			String[] keys = { "id" };
			PreparedStatement pStmt = conn.prepareStatement(sql.toString(), keys);

			int rowsUpdated = pStmt.executeUpdate();

			ResultSet generatedKeys = pStmt.getGeneratedKeys();
			for (Rubric rubric : rubrics) {
				if (generatedKeys.next()) {
					int newId = generatedKeys.getInt(1);
					rubric.setId(newId);
				}
			}
			conn.commit();
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
		return rubrics;
	}

}
