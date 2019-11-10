package com.foxminded.university.dao.layers;

import com.foxminded.university.domain.Group;
import java.util.List;

/**
 * Group DAO
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public interface GroupDAO {

    /**
     * Finds all groups related to the passed amount of students.
     *
     * @param amountStudents - amount of students
     * @return - list of groups
     */
    List<Group> findGroups(int amountStudents);
}
