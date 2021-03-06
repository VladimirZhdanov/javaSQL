package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.GroupDAO;
import com.foxminded.university.domain.Group;
import com.foxminded.university.exceptions.DAOException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static java.sql.Statement.NO_GENERATED_KEYS;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * DAO layer for the groups table.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class GroupSQL implements GroupDAO {

    private static final String NULL_WAS_PASSED = "Null was passed";

    private Properties properties;
    /**
     * Connection pool and connection fabric.
     */
    private DataSource dataSource;

    /**
     * Constructor of the class
     *
     * @param dataSource - connection pool and connection fabric
     */
    public GroupSQL(DataSource dataSource) {
        this.dataSource = dataSource;
        properties = new Properties();
        init();
    }

    /**
     * Initialisation properties.
     */
    private void init() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("queriesDAO.properties")) {
            if (is == null) {
                throw new DAOException("Null was passed.");
            }
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds all groups related to the passed amount of students.
     *
     * @param amountStudents - amount of students
     * @return - list of groups
     */
    @Override
    public List<Group> getGroupsByStudentCount(int amountStudents) {
        List<Group> students = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(properties.getProperty("findGroups"))) {
            statement.setInt(1, amountStudents);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(extractGroup(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Inserts passed groups.
     *
     * @param groups - groups
     */
    @Override
    public boolean insert(Set<Group> groups) {
        int result = 0;

        if (groups == null) {
            throw new DAOException(NULL_WAS_PASSED);
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(properties.getProperty("insertGroups"),
                     RETURN_GENERATED_KEYS)) {
            for (Group group : groups) {
                statement.setString(1, group.getName());
                result = statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        group.setId(id);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    /**
     * Extracts a group from a ResultSet.
     *
     * @param rs - ResultSet
     * @return - Group
     * @throws SQLException - SQLException
     */
    private Group extractGroup(ResultSet rs) throws SQLException {
        Group group = new Group();
        group.setId(rs.getInt("group_id"));
        group.setName(rs.getString("group_name"));
        return group;
    }
}
