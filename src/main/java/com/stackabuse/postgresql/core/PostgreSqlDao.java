/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stackabuse.postgresql.core;

import com.stackabuse.postgresql.api.Customer;
import com.stackabuse.postgresql.spi.Dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hiram K. <https://github.com/IdelsTak>
 */
public class PostgreSqlDao implements Dao<Customer, Integer> {

    private static final Logger LOGGER = Logger.getLogger(PostgreSqlDao.class.getName());
    private final Optional<Connection> connection;

    public PostgreSqlDao() {
        this.connection = JdbcConnection.getConnection();
    }

    @Override
    public Optional<Customer> get(int id) {
        return connection.flatMap(conn -> {
            Optional<Customer> customer = Optional.empty();
            String sql = "SELECT * FROM customer WHERE customer_id = " + id;

            try (Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");

                    customer = Optional.of(new Customer(id, firstName, lastName, email));

                    LOGGER.log(Level.INFO, "Found {0} in database", customer.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return customer;
        });
    }

    @Override
    public Collection<Customer> getAll() {
        Collection<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("customer_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");

                    Customer customer = new Customer(id, firstName, lastName, email);

                    customers.add(customer);

                    LOGGER.log(Level.INFO, "Found {0} in database", customer);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return customers;
    }

    @Override
    public Optional<Integer> save(Customer customer) {
        String message = "The customer to be added should not be null";
        Customer nonNullCustomer = Objects.requireNonNull(customer, message);
        String sql = "INSERT INTO "
                + "customer(first_name, last_name, email) "
                + "VALUES(?, ?, ?)";

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, nonNullCustomer.getFirstName());
                statement.setString(2, nonNullCustomer.getLastName());
                statement.setString(3, nonNullCustomer.getEmail());

                int numberOfInsertedRows = statement.executeUpdate();

                //Retrieve the auto-generated id
                if (numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }

                LOGGER.log(Level.INFO, "{0} created successfully? {1}",
                        new Object[]{nonNullCustomer, numberOfInsertedRows > 0});
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return generatedId;
        });
    }

    @Override
    public void update(Customer customer) {
        String message = "The customer to be updated should not be null";
        Customer nonNullCustomer = Objects.requireNonNull(customer, message);
        String sql = "UPDATE customer "
                + "SET "
                + "first_name = ?, "
                + "last_name = ?, "
                + "email = ? "
                + "WHERE "
                + "customer_id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, nonNullCustomer.getFirstName());
                statement.setString(2, nonNullCustomer.getLastName());
                statement.setString(3, nonNullCustomer.getEmail());
                statement.setInt(4, nonNullCustomer.getId());

                int numberOfUpdatedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the customer updated successfully? {0}",
                        numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void delete(Customer customer) {
        String message = "The customer to be deleted should not be null";
        Customer nonNullCustomer = Objects.requireNonNull(customer, message);
        String sql = "DELETE FROM customer WHERE customer_id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullCustomer.getId());

                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the customer deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

}
