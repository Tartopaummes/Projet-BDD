package requests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import connectivite.DBConnection;


public class Insertion {

    public static void main(String[] args) {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false); // Pour gérer les transactions manuellement

            // Insertion d'un utilisateur
            /* String insertUserSQL = "INSERT INTO UTILISATEUR (EmailUser, NomUser, PrenomUser, AdressePostale) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psUser = connection.prepareStatement(insertUserSQL)) {
                psUser.setString(1, "utilisateur@example.com");
                psUser.setString(2, "Nom");
                psUser.setString(3, "Prenom");
                psUser.setString(4, "123 Rue Exemple, Ville, CodePostal");
                psUser.executeUpdate();
            } */

            // Insertion d'une vente
            String insertSaleSQL = "INSERT INTO VENTE (IdVente, IdSalle, IdProduit, PrixDépart, TypeVente, VenteRevocable, DateHeureDebutVente) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psSale = connection.prepareStatement(insertSaleSQL)) {
                psSale.setInt(1, 1); // IdVente
                psSale.setInt(2, 101); // IdSalle
                psSale.setInt(3, 202); // IdProduit
                psSale.setDouble(4, 50.00); // PrixDépart
                psSale.setString(5, "croissante"); // TypeVente
                psSale.setBoolean(6, true); // VenteRevocable
                psSale.setTimestamp(7, Timestamp.valueOf("2024-11-27 10:00:00")); // DateHeureDebutVente
                psSale.executeUpdate();
            }

            connection.commit(); // Validation de la transaction
            System.out.println("Insertion effectuée avec succès");

        } catch (SQLException e) {
            e.printStackTrace();

            }
        }
    }

