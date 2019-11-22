package com.foxminded.university.dao.layers;

import com.foxminded.university.domain.Group;
import java.util.List;
import java.util.Set;

/**
 * Group DAO
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public interface GroupDAO {

    /**
     * Inserts passed groups.
     *
     * @param groups - groups
     * @return - added\didn't add - boolean
     */
    boolean insert(Set<Group> groups);

    /**
     * Finds all groups related to the passed amount of students.
     *
     * @param amountStudents - amount of students
     * @return - list of groups
     */
    List<Group> getGroupsByStudentCount(int amountStudents);
}
