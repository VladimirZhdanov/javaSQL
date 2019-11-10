package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.GroupDAO;
import com.foxminded.university.domain.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO layer for the groups table.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class GroupSQL implements GroupDAO {
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
    }

    /**
     * Finds all groups related to the passed amount of students.
     *
     * @param amountStudents - amount of students
     * @return - list of groups
     */
    @Override
    public List<Group> findGroups(int amountStudents) {
        List<Group> students = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM groups "
                     + "WHERE group_name IN ("
                     + "  SELECT groups.group_name "
                     + "  FROM groups "
                     + "         LEFT JOIN students "
                     + "                   ON students.group_id = groups.group_id "
                     + "  GROUP BY groups.group_id "
                     + "  HAVING COUNT(*) <= ?)")) {
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
