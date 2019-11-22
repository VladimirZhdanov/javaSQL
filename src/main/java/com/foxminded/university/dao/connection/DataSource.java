package com.foxminded.university.dao.connection;

import com.foxminded.university.exceptions.DAOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * Connection pool.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class DataSource {
    private String url;
    private String user;
    private String password;
    private String driverName;
    private BasicDataSource source;

    /**
     * Constructor of the class
     *
     * @param config - config
     */
    public DataSource(Config config) {
        if (config == null) {
            throw new DAOException("Null was passed");
        }
        source = new BasicDataSource();
        url = config.getUrl();
        user = config.getUser();
        password = config.getPassword();
        driverName = config.getDriverName();
        init();
    }

    /**
     * Initialisation class fields.
     */
    private void init() {
        if (url == null && user == null && password == null && driverName == null &&  source == null) {
            throw new DAOException("Fields had not been initialised");
        }
        source.setDriverClassName(driverName);
        source.setUrl(url);
        source.setUsername(user);
        source.setPassword(password);
        source.setMinIdle(5);
        source.setMaxIdle(10);
        source.setMaxOpenPreparedStatements(100);
    }

    /**
     * Closes a connection.
     */
    public void closeConnection() {
        try {
            source.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets connections.
     *
     * @return - Connection object
     * @throws SQLException - SQLException
     */
    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }
}
