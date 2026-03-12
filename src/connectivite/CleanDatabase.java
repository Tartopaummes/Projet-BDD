package connectivite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CleanDatabase {
    public static void main(String[] args) {
        System.out.println("Checking and establishing database connection...");

        Connection connection = null;
        try {
            // Obtenir la connexion à la base de données
            connection = DBConnection.getConnection();
            System.out.println("Connection established successfully!");

            // Nettoyage de la base de données
            cleanDatabase(connection);

        } catch (SQLException e) {
            System.err.println("Failed to establish or use the database connection.");
            e.printStackTrace();
        } finally {
            // Toujours fermer la connexion après le nettoyage
            if (connection != null) {
                DBConnection.closeConnection(connection); // Passer la connexion en paramètre
            }
        }
    }

    private static void cleanDatabase(Connection connection) {
        String cleanCommand = """
                BEGIN
                    FOR t IN (SELECT TABLE_NAME FROM USER_TABLES) LOOP
                        EXECUTE IMMEDIATE 'DROP TABLE ' || t.TABLE_NAME || ' CASCADE CONSTRAINTS';
                    END LOOP;
                END;
                """;

        try (Statement statement = connection.createStatement()) {
            // Exécution de la commande PL/SQL pour nettoyer la base
            statement.execute(cleanCommand);
            System.out.println("Database cleaned successfully: all tables have been dropped.");
        } catch (SQLException e) {
            System.err.println("Error while cleaning the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}