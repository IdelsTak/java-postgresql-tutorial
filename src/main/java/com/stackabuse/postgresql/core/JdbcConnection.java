/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stackabuse.postgresql.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hiram K. <https://github.com/IdelsTak>
 */
public class JdbcConnection {

    private static final Logger LOGGER = Logger.getLogger(JdbcConnection.class.getName());
    private static Optional<Connection> connection = Optional.empty();

    public static Optional<Connection> getConnection() {
        if (connection.isEmpty()) {
            String url = "jdbc:postgresql://localhost:5432/sampledb";
            String user = "postgres";
            String password = "aqua1-Was";

            try {
                connection = Optional.ofNullable(DriverManager.getConnection(url, user, password));
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        return connection;
    }
}
