package connectivite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SetupDatabase {
    public static void main(String[] args) {
        String dbURL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1"; // Remplacez par votre URL Oracle
        String dbUser = "oudghirm"; // Remplacez par votre nom d'utilisateur Oracle
        String dbPassword = "oudghirm"; // Remplacez par votre mot de passe Oracle

        Connection connection = null;

        try {
            // Se connecter à la base de données
            connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            System.out.println("Connection established successfully!");

            // Exécuter les scripts SQL
            executeSQLScript(connection, "src/tables/creationTable.sql");
            executeSQLScript(connection, "src/tables/insertionDonnees.sql");

            System.out.println("All scripts executed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Méthode pour exécuter un script SQL
     * @param connection La connexion à la base de données
     * @param scriptFilePath Le chemin vers le fichier SQL
     */
    public static void executeSQLScript(Connection connection, String scriptFilePath) {
        Statement stmt = null;
        BufferedReader reader = null;

        try {
            stmt = connection.createStatement();
            reader = new BufferedReader(new FileReader(scriptFilePath));
            String line;
            StringBuilder sqlStatement = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("--")) {
                    continue; // Ignorer les commentaires et les lignes vides
                }

                sqlStatement.append(line);

                // Si la ligne se termine par un point-virgule, exécuter la commande SQL
                if (line.trim().endsWith(";")) {
                    try {
                        String sql = sqlStatement.toString().trim();
                        sql = sql.substring(0, sql.length() - 1); // Supprimer le point-virgule
                        stmt.execute(sql);
                        System.out.println("Executed SQL: " + sql);
                    } catch (Exception e) {
                        System.err.println("Error executing SQL: " + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        sqlStatement.setLength(0); // Réinitialiser pour la prochaine commande
                    }
                }
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}