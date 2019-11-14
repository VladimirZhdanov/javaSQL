package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.ibatis.jdbc.ScriptRunner;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

/**
 * Class to execute a Query from file.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class ExecuterQuery {

    /**
     * Constructor of the class.
     */
    public ExecuterQuery() {
    }

    /**
     * Executes a query depends on data source from file.
     *
     * @param dataSource - data source
     * @param fileName - file name of query
     */
    public void execute(DataSource dataSource, String fileName) {
        try {
            ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
            runner.setAutoCommit(true);
            runner.setStopOnError(true);
            runner.runScript(getResourceAsReader(fileName));
            runner.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
