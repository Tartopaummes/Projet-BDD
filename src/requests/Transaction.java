package requests;

import java.sql.*;

public class Transaction {
    protected Connection connection;

    /**
     * Constructs a Transaction object with the specified database connection.
     *
     * @param connection the database connection to be used for this transaction
     */
    public Transaction(Connection connection) {
        this.connection = connection;
    }

    /**
     * Sets the auto-commit mode for the current database connection.
     *
     * @param mode the auto-commit mode to set. It should be either Constants.AUTOCCOMMIT.ON or Constants.AUTOCCOMMIT.OFF.
     * @throws SQLException if the connection is null or if a database access error occurs.
     */
    public void setAutoCommit(Constants.AUTOCCOMMIT mode) throws SQLException {
        if (connection != null) {
            if (mode == Constants.AUTOCCOMMIT.ON) {
                connection.setAutoCommit(true);
            } else {
                connection.setAutoCommit(false);
            }
        } else {
            throw new SQLException("La connexion est null. Impossible de changer le mode auto-commit.");
        }
    }

    /**
     * Begins a new transaction by setting the auto-commit mode of the connection to false.
     * 
     * @throws SQLException if the connection is null or if a database access error occurs.
     */
    public void begin() throws SQLException {
        if (connection != null) {
            connection.setAutoCommit(false);
        } else {
            throw new SQLException("La connexion est null. Impossible de démarrer la transaction.");
        }
    }

    /**
     * Commits the current transaction. If the connection is not null, the transaction is committed
     * and the auto-commit mode is reset to true. If the connection is null, an SQLException is thrown.
     *
     * @throws SQLException if the connection is null or if a database access error occurs
     */
    public void commit() throws SQLException {
        if (connection != null) {
            connection.commit();
        } else {
            throw new SQLException("La connexion est null. Impossible de valider la transaction.");
        }
    }

    /**
     * Rolls back the current transaction to the last check-point. If the connection is not null, it will
     * rollback the transaction and set the auto-commit mode back to true.
     * If the connection is null, it will throw an SQLException.
     *
     * @throws SQLException if the connection is null or if a database access error occurs.
     */
    public void rollback() throws SQLException {
        if (connection != null) {
            connection.rollback();
            connection.setAutoCommit(true); // Retour à l'état par défaut
        } else {
            throw new SQLException("La connexion est null. Impossible d'annuler la transaction.");
        }
    }

    /**
     * Rolls back the current transaction to the specified savepoint.
     *
     * @param savepoint the Savepoint object to which the transaction should be rolled back
     * @throws SQLException if a database access error occurs or the connection is null
     */
    public void rollback(Savepoint savepoint) throws SQLException {
        if (connection != null) {
            connection.rollback(savepoint);
        } else {
            throw new SQLException("La connexion est null. Impossible d'annuler la transaction.");
        }
    }

    /**
     * Closes the database connection if it is not null.
     *
     * @throws SQLException if the connection is null or an error occurs while closing the connection.
     */
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        } else {
            throw new SQLException("La connexion est null. Impossible de fermer la connexion.");
        }
    }


    
}
