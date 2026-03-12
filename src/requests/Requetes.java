package requests;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Requetes extends Transaction {

    public Requetes(Connection connection) {
        super(connection);
    }

    //////////////////////////////
    /// Gestion des catégories ///
    //////////////////////////////

    

    private boolean tryCategory(String nameCategory, String categoryDescription) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Categorie WHERE nomCat = ?");
        stmt.setString(1, nameCategory);
        ResultSet rs = stmt.executeQuery();
        if(categoryDescription.isEmpty()) {
            return rs.next();
        } else {
            if(rs.next()) {
                throw new SQLException("La catégorie avec l'identifiant " + nameCategory + " existe déjà, impossible de la créer.");
            }
            PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO Categorie VALUES (?, ?)");
            stmt2.setString(1, nameCategory);
            stmt2.setString(2, categoryDescription);
            stmt2.executeUpdate();
            System.out.println("Inserting category " + nameCategory);
            return true;
        }
    }





    private boolean productExists(long idProduct) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Produit WHERE idProduit = ?");
        stmt.setLong(1, idProduct);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }


    private long getStock(long idProduct) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT Stock FROM Produit WHERE idProduit = ?");
        stmt.setLong(1, idProduct);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getLong("Stock");
    }


    private void reduceStock(long idProduct, int quantity) throws SQLException {

        if(!productExists(idProduct)) {
            throw new SQLException("Le produit avec l'identifiant " + idProduct + " n'existe pas.");
        }

        if(quantity < 0) {
            throw new SQLException("La quantité à enlever ne peut pas être négative.");
        }

        if(getStock(idProduct) < quantity) {
            throw new SQLException("Le stock du produit " + idProduct + " est insuffisant.");
        }

        System.out.println("Reducing stock of product " + idProduct + " by " + quantity);
        PreparedStatement stmt = connection.prepareStatement("UPDATE Produit SET Stock = Stock - ? WHERE idProduit = ?");
        stmt.setLong(1, quantity);
        stmt.setLong(2, idProduct);
        stmt.executeUpdate();
    }

    private String getCategoryFromProduct(long idProduct) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT nomCat FROM Produit WHERE idProduit = ?");
        stmt.setLong(1, idProduct);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getString("nomCat");
    }

    private double getReturnPrice(long idProduct) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT PrixRevient FROM Produit WHERE idProduit = ?");
        stmt.setLong(1, idProduct);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getDouble("PrixRevient");
    }

    private long getProductFromSale(long idSale) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT idProduit FROM Vente WHERE idVente = ?");
        stmt.setLong(1, idSale);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getLong("idProduit");
    }

    


    ///////////////////////////////////
    /// Gestion des salles de vente ///
    /// ///////////////////////////////


    private boolean roomExists(long idRoom) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Salle WHERE idSalle = ?");
        stmt.setLong(1, idRoom);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public void addRoom(long idRoom, String nameCategory) throws SQLException {

        begin();
        if(!tryCategory(nameCategory, "")) {
            rollback();
            throw new SQLException("La catégorie avec l'identifiant " + nameCategory + " n'existe pas.");
        }

        if(roomExists(idRoom)){
            rollback();
            throw new SQLException("La salle de vente avec l'identifiant " + idRoom + " existe déjà.");
        }

        System.out.println("Inserting room " + idRoom + " with category " + nameCategory);
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Salle VALUES (?, ?)");
        stmt.setLong(1, idRoom);
        stmt.setString(2, nameCategory);
        stmt.executeUpdate();
        commit();
    }


    private String getCategoryFromRoom(long idRoom) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT nomCat FROM Salle WHERE idSalle = ?");
        stmt.setLong(1, idRoom);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getString("nomCat");
    }


    //////////////////////////////
    ///// Gestion des ventes /////
    /// //////////////////////////


    public boolean saleExists(long idSale) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vente WHERE IdVente = ?");
        stmt.setLong(1, idSale);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    private boolean isRevocable(long idSale) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT VenteRevocable FROM Vente WHERE IdVente = ?");
        stmt.setLong(1, idSale);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getBoolean("VenteRevocable");
    }
    

    private String getSaleType(long idSale) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT TypeVente FROM Vente WHERE IdVente = ?");
        stmt.setLong(1, idSale);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getString("TypeVente");
    }

    public boolean isSaleEnded(long idSale) throws SQLException {
        boolean res = false;

        // Check if the sale exists
        if(!saleExists(idSale)) {
            throw new SQLException("La vente avec l'identifiant " + idSale + " n'existe pas.");
        }

        // Check if the sale has a limited end date at if this date is passed
        PreparedStatement stmt1 = connection.prepareStatement("SELECT * FROM VenteLimitee WHERE IdVente = ?");
        stmt1.setLong(1, idSale);
        ResultSet rs1 = stmt1.executeQuery();
        if(rs1.next()) {
            Timestamp end = rs1.getTimestamp("DateHeureFinVente");
            res = LocalDateTime.now().isAfter(end.toLocalDateTime());
        }

        // Check if there has been no offer in the last 10 minutes
        PreparedStatement stmt2 = connection.prepareStatement("SELECT DateHeureDepotOffre FROM Offre WHERE IdVente = ? AND DateHeureDepotOffre >= ? ORDER BY DateHeureDepotOffre DESC FETCH FIRST 1 ROW ONLY");
        stmt2.setLong(1, idSale);
        stmt2.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().minusMinutes(10)));
        ResultSet rs2 = stmt2.executeQuery();
        if(rs2.next()) {
            res = res || rs2.getTimestamp("DateHeureDepotOffre").before(Timestamp.valueOf(LocalDateTime.now().minusMinutes(10)));
        }

        // Check if the price has reached 0 for descending sales
        PreparedStatement stmt3 = connection.prepareStatement("SELECT DateHeureDebutVente FROM Vente WHERE IdVente = ? AND TypeVente = ?");
        int time_reduction = (int)(1/GlobalVaraibles.reductionRate);
        Timestamp now_minus_reduction = Timestamp.valueOf(LocalDateTime.now().minusMinutes(time_reduction));
        stmt3.setLong(1, idSale);
        stmt3.setString(2, "decroissante");
        ResultSet rs3 = stmt3.executeQuery();
        if(rs3.next()) {
            Timestamp start = rs3.getTimestamp("DateHeureDebutVente");
            res = res || start.before(now_minus_reduction);
        }

        // Check if an offer was placed for descending sales
        PreparedStatement stmt4 = connection.prepareStatement("SELECT * FROM Offre JOIN Vente ON Vente.IdVente = Offre.IdVente WHERE Offre.IdVente = ? AND TypeVente = ?");
        stmt4.setLong(1, idSale);
        stmt4.setString(2, "decroissante");
        ResultSet rs4 = stmt4.executeQuery();
        if(rs4.next()) {
            res = true;
        }


        return res;
    }


    public void updateNextSaleToEnd() throws SQLException {
        begin();
        // Get the next sale to end according to the end date
        PreparedStatement stmt1 = connection.prepareStatement("SELECT IdVente, DateHeureFinVente FROM VenteLimitee WHERE DateHeureFinVente >= ? ORDER BY DateHeureFinVente FETCH FIRST 1 ROW ONLY");
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp1 = Timestamp.valueOf(now);
        stmt1.setTimestamp(1, timestamp1);
        ResultSet rs1 = stmt1.executeQuery();

        // Get the next sale to end according to the last offer date
        PreparedStatement stmt2 = connection.prepareStatement(
        "SELECT IdVente, DateHeureFinVente FROM (" +
        "SELECT Vente.IdVente, (Offre.DateHeureDepotOffre + INTERVAL '10' MINUTE) AS DateHeureFinVente " +
        "FROM Vente JOIN Offre ON Vente.IdVente = Offre.IdVente " +
        "WHERE Offre.DateHeureDepotOffre >= ? " +
        "UNION " + // The second part of the union is to get the sales with no offers
        "SELECT IdVente, (DateHeureDebutVente + INTERVAL '10' MINUTE) AS DateHeureFinVente " +
        "FROM Vente WHERE IdVente NOT IN (SELECT IdVente FROM Offre) AND DateHeureDebutVente >= ?) " +
        "ORDER BY DateHeureFinVente FETCH FIRST 1 ROW ONLY"
        );
        Timestamp timestamp2 = Timestamp.valueOf(now.minusMinutes(10));
        stmt2.setTimestamp(1, timestamp2);
        stmt2.setTimestamp(2, timestamp2);
        ResultSet rs2 = stmt2.executeQuery();

        // Get the next sale to end according to the price reduction
        PreparedStatement stmt3 = connection.prepareStatement("SELECT IdVente, (DateHeureDebutVente + INTERVAL '1' MINUTE * ?) AS DateHeureFinVente FROM Vente WHERE TypeVente = ? AND DateHeureDebutVente >= ? ORDER BY DateHeureDebutVente FETCH FIRST 1 ROW ONLY");
        int time_reduction = (int)(1/GlobalVaraibles.reductionRate);
        Timestamp timestamp3 = Timestamp.valueOf(now.minusMinutes(time_reduction));
        stmt3.setInt(1, time_reduction);
        stmt3.setString(2, "decroissante");
        stmt3.setTimestamp(3, timestamp3);
        ResultSet rs3 = stmt3.executeQuery();

        // Get the sale that ends first of those three
        if(rs1.next()){
            if(rs1.getTimestamp("DateHeureFinVente").before(GlobalVaraibles.endOfSaleUpdate) && !isSaleEnded(rs1.getLong("IdVente"))){
                GlobalVaraibles.endOfSaleUpdate = rs1.getTimestamp("DateHeureFinVente");
                GlobalVaraibles.nextSaleToEnd = rs1.getLong("IdVente");
            }
        }
        if(rs2.next()){
            if(rs2.getTimestamp("DateHeureFinVente").before(GlobalVaraibles.endOfSaleUpdate) && !isSaleEnded(rs2.getLong("IdVente"))){
                GlobalVaraibles.endOfSaleUpdate = rs2.getTimestamp("DateHeureFinVente");
                GlobalVaraibles.nextSaleToEnd = rs2.getLong("IdVente");
            }
        }
        if(rs3.next()){
            if(rs3.getTimestamp("DateHeureFinVente").before(GlobalVaraibles.endOfSaleUpdate) && !isSaleEnded(rs3.getLong("IdVente"))){
                GlobalVaraibles.endOfSaleUpdate = rs3.getTimestamp("DateHeureFinVente");
                GlobalVaraibles.nextSaleToEnd = rs3.getLong("IdVente");
            }
        }
        System.out.println("Next sale to end is " + GlobalVaraibles.nextSaleToEnd + " at " + GlobalVaraibles.endOfSaleUpdate);
        commit();
    }


    public void addSale(long idSale,
                        long idRoom,
                        long idProduct,
                        double startPrice,
                        String saleType,
                        boolean isRevocable,
                        boolean isLimited,
                        Timestamp dateOfEnd) throws SQLException {

        begin();
    
        if(saleExists(idSale)){
            rollback();
            throw new SQLException("La vente avec l'identifiant " + idSale + " existe déjà.");
        }

        if(!productExists(idProduct)) {
            rollback();
            throw new SQLException("Le produit avec l'identifiant " + idProduct + " n'existe pas.");
        }

        if(!roomExists(idRoom)) {
            rollback();
            throw new SQLException("La salle de vente avec l'identifiant " + idRoom + " n'existe pas.");
        }

        if(!getCategoryFromRoom(idRoom).equals(getCategoryFromProduct(idProduct))) {
            rollback();
            throw new SQLException("La catégorie du produit et de la salle de vente ne correspondent pas.");
        }

        if(startPrice < 0) {
            rollback();
            throw new SQLException("Le prix de départ ne peut pas être négatif.");
        }

        if(!saleType.equals("croissante") && !saleType.equals("decroissante")) {
            rollback();
            throw new SQLException("Type de vente inconnu.");
        }


        System.out.println("Inserting sale " + idSale + " with product " + idProduct + " in room " + idRoom);
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Vente VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        stmt.setLong(1, idSale);
        stmt.setLong(2, idRoom);
        stmt.setLong(3, idProduct);
        stmt.setDouble(4, startPrice);
        stmt.setString(5, saleType);
        stmt.setBoolean(6, isRevocable);
        stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        stmt.setBoolean(8, isLimited);
        stmt.executeUpdate();
        if(dateOfEnd != null) {
            PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO VenteLimitee VALUES (?, ?)");
            stmt2.setLong(1, idSale);
            stmt2.setTimestamp(2, dateOfEnd);
            stmt2.executeUpdate();
        }


        commit();
        updateNextSaleToEnd();
        
    }


    private ResultSet getWinner(long idSale) throws SQLException {
        // Check if the sale exists
        if(!saleExists(idSale)) {
            throw new SQLException("La vente avec l'identifiant " + idSale + " n'existe pas.");
        }

        // Get the winner of the sale according to the type of sale
        String saleType = getSaleType(idSale);
        if(saleType.equals("croissante")) {
            // Get the highest offer
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Offre WHERE IdVente = ? ORDER BY PrixOffrePropose DESC FETCH FIRST 1 ROW ONLY");
            stmt.setLong(1, idSale);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } else if (saleType.equals("decroissante")) {
            // Get the first offer
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Offre WHERE IdVente = ? ORDER BY DateHeureDepotOffre ASC FETCH FIRST 1 ROW ONLY");
            stmt.setLong(1, idSale);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } else {
            throw new SQLException("Type de vente " + saleType + " inconnu pour la vente " + idSale + ".");
        }
    }
 
    public void EndSale() throws SQLException {

        begin();
        try{
            ResultSet winner = getWinner(GlobalVaraibles.nextSaleToEnd);
            if(winner.next()) {
                String emailWinner = winner.getString("EmailUser");
                double priceWinner = winner.getDouble("PrixOffrePropose");
                int quantityWinner = winner.getInt("QuantiteProduit");
                long idSale = winner.getLong("IdVente");

                if(!(isRevocable(idSale) && getReturnPrice(getProductFromSale(idSale)) < priceWinner)) {
                

                    System.out.println("Le gagnant de la vente" + GlobalVaraibles.nextSaleToEnd +  " est: " + emailWinner + " avec un prix de " + priceWinner + " et une quantité de " + quantityWinner);
                    reduceStock(GlobalVaraibles.nextSaleToEnd, quantityWinner);
                } else {
                    System.out.println("La vente " + GlobalVaraibles.nextSaleToEnd + " est révoquée.");
                }
            }

            System.out.println("La vente " + GlobalVaraibles.nextSaleToEnd + " est terminée. Pas de gagnant.");

            updateNextSaleToEnd();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
        commit();
    
    }


    private ArrayList<Long> getSales() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT IdVente FROM Vente");
        ResultSet rs = stmt.executeQuery();
        ArrayList<Long> sales = new ArrayList<>();
        while(rs.next()) {
            sales.add(rs.getLong("IdVente"));
        }
        return sales;
    }


    private Timestamp getEndDate(long idSale) throws SQLException {
        // Check if the sale exists
        if(!saleExists(idSale)) {
            throw new SQLException("La vente avec l'identifiant " + idSale + " n'existe pas.");
        }

        Timestamp end = null;

        // Get the end date of the sale
        PreparedStatement stmt1 = connection.prepareStatement("SELECT DateHeureFinVente FROM VenteLimitee WHERE IdVente = ?");
        stmt1.setLong(1, idSale);
        ResultSet rs1 = stmt1.executeQuery();
        if(rs1.next()) {
            end = rs1.getTimestamp("DateHeureFinVente");
        }

        // Get the end date of the last offer
        PreparedStatement stmt2 = connection.prepareStatement("SELECT DateHeureDepotOffre FROM Offre WHERE IdVente = ? ORDER BY DateHeureDepotOffre DESC FETCH FIRST 1 ROW ONLY");
        stmt2.setLong(1, idSale);
        ResultSet rs2 = stmt2.executeQuery();
        if(rs2.next()) {
            // end 2 date of the last offer plus 10 minutes
            Timestamp end2 = rs2.getTimestamp("DateHeureDepotOffre");
            end2 = Timestamp.valueOf(end2.toLocalDateTime().plusMinutes(10));
            if(end == null || end2.before(end)) {
                end = end2;
            }
        }

        // Get the end date according to the price reduction
        PreparedStatement stmt3 = connection.prepareStatement("SELECT DateHeureDebutVente FROM Vente WHERE IdVente = ? AND TypeVente = ?");
        stmt3.setLong(1, idSale);
        stmt3.setString(2, "decroissante");
        ResultSet rs3 = stmt3.executeQuery();
        if(rs3.next()) {
            Timestamp start = rs3.getTimestamp("DateHeureDebutVente");
            int time_reduction = (int)(1/GlobalVaraibles.reductionRate);
            Timestamp end3 = Timestamp.valueOf(start.toLocalDateTime().plusMinutes(time_reduction));
            if(end == null || end3.before(end)) {
                end = end3;
            }
        }

        // Get the end date if there is no offer
        PreparedStatement stmt4 = connection.prepareStatement("SELECT DateHeureDebutVente FROM Vente WHERE IdVente = ? AND IdVente NOT IN (SELECT IdVente FROM Offre)");
        stmt4.setLong(1, idSale);
        ResultSet rs4 = stmt4.executeQuery();
        if(rs4.next()) {
            Timestamp start = rs4.getTimestamp("DateHeureDebutVente");
            Timestamp end4 = Timestamp.valueOf(start.toLocalDateTime().plusMinutes(10));
            if(end == null || end4.before(end)) {
                end = end4;
            }
        }

        return end;
    }

    public void updateEndOfSale() throws SQLException {
        begin();
        ArrayList<Long> sales = getSales();
        Timestamp end = null;
        long nextSale = 0;
        for(long idSale : sales) {
            Timestamp endSale = getEndDate(idSale);
            if(end == null || endSale.before(end)) {
                end = endSale;
                nextSale = idSale;
            }
        }
        GlobalVaraibles.endOfSaleUpdate = end;
        GlobalVaraibles.nextSaleToEnd = nextSale;
        commit();
        System.out.println("Next sale to end is " + GlobalVaraibles.nextSaleToEnd + " at " + GlobalVaraibles.endOfSaleUpdate);
    }


    public boolean checkUser(String emailUser) throws SQLException {
        PreparedStatement checkUserStmt = connection.prepareStatement("SELECT * FROM Utilisateur WHERE EmailUser = ?");
        checkUserStmt.setString(1, emailUser);
        ResultSet rs = checkUserStmt.executeQuery();
        return rs.next();
    }


    public void PlaceOffer(String emailUser, long idVente, double prixOffrePropose, int quantiteProduit,
                       Timestamp dateHeureDepotOffre) throws SQLException {
    begin();
    try {
        if (!checkUser(emailUser)) {
            throw new SQLException("Utilisateur avec l'email " + emailUser + " n'existe pas.");
        }

        if (!saleExists(idVente)) {
            throw new SQLException("Vente avec l'identifiant " + idVente + " n'existe pas.");
        }

        double prixdepart = -1;
        PreparedStatement stmt = connection.prepareStatement("SELECT PrixDepart FROM Vente WHERE IdVente = ?");
        stmt.setLong(1, idVente);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            prixdepart = rs.getDouble("PrixDepart");
        } else {
            System.out.println("Impossible de récuperer le prix de départ");
        }
        rs.close();
        stmt.close();
        
        double prixMax = 0.0;
        PreparedStatement stmtMax = connection.prepareStatement(
            "SELECT MAX(PrixOffrePropose) AS PrixMax FROM Offre WHERE IdVente = ?"
        );
        stmtMax.setLong(1, idVente);
        ResultSet rsMax = stmtMax.executeQuery();
        if (rsMax.next()) {
            prixMax = rsMax.getDouble("PrixMax");
        }
        rsMax.close();
        stmtMax.close();
        String typevente = getTypeVente(idVente);

        if ("croissante".equals(typevente)) {
            if (prixOffrePropose < prixdepart) {
                throw new SQLException("Le prix proposé doit être supérieur au prix de départ");
            }
            if (prixOffrePropose <= prixMax) {
                throw new SQLException("Le prix proposé doit être strictement supérieur au prix le plus élevé déjà proposé " + prixMax);
            }
        }

        if ("decroissante".equals(typevente)) {
                
            if (isOfferPlaced(idVente)) {
                throw new SQLException("la vente est déjà terminée");
            }

            if (prixOffrePropose < getPrixminimal(idVente)) {
                throw new SQLException("le prix de l'offre doit supérieur à " + getPrixminimal(idVente));
            } 
        }

        if (limiteOffreAtteinte(emailUser, idVente)) {
            throw new SQLException("Vous avez dejà placé une vente, votre limite d'offre est donc atteinte !");

        }

        if (prixOffrePropose <= 0) {
            throw new SQLException("Le prix proposé doit être supérieur à 0.");
        }
        if (quantiteProduit <= 0) {
            throw new SQLException("La quantité proposée doit être supérieure à 0.");
        }
        if (quantiteProduit > getStock(getProductFromSale(idVente))) {
            throw new SQLException("La quantité proposée est supérieure au stock disponible.");
        }

        if (isSaleEnded(idVente)) {
            throw new SQLException("La vente avec l'identifiant " + idVente + " est terminée.");
        }

        PreparedStatement insertOfferStmt = connection.prepareStatement(
            "INSERT INTO OFFRE (EmailUser, IdVente, PrixOffrePropose, DateHeuredepotOffre, QuantiteProduit) " +
            "VALUES (?, ?, ?, ?, ?)"
        );
        insertOfferStmt.setString(1, emailUser);
        insertOfferStmt.setLong(2, idVente);
        insertOfferStmt.setDouble(3, prixOffrePropose);
        insertOfferStmt.setTimestamp(4, dateHeureDepotOffre);
        insertOfferStmt.setInt(5, quantiteProduit);
        insertOfferStmt.executeUpdate();

        commit();
        System.out.println("Offre placée avec succès.");
        if(getSaleType(idVente).equals("decroissante")) {
            GlobalVaraibles.nextSaleToEnd = idVente;
            EndSale();
        }
        updateNextSaleToEnd();
        } catch (SQLException e) {
            rollback();
            throw e;
        }
    }


    


     public String GenerateAuctionReport(int idVente) throws SQLException {
        StringBuilder report = new StringBuilder();
        PreparedStatement auctionStmt = connection.prepareStatement(
            "SELECT v.IdVente, p.NomProduit, o.EmailUser, o.PrixOffrePropose, o.QuantiteProduit, o.DateHeureDepotOffre " +
            "FROM Vente v " +
            "JOIN Offre o ON v.IdVente = o.IdVente " +
            "JOIN Produit p ON v.IdProduit = p.IdProduit " +
            "WHERE v.IdVente = ? " +  
            "ORDER BY o.PrixOffrePropose DESC"
        );
        auctionStmt.setDouble(1, idVente);
        ResultSet rs = auctionStmt.executeQuery();

        if (!rs.next()) {
            throw new SQLException("Aucune offre trouvée pour la vente avec l'identifiant " + idVente + ".");
        }

        report.append("Rapport de Vente ID: ").append(idVente).append("\n");
        report.append(String.format("%-20s %-20s %-10s %-10s %-20s\n", "Utilisateur", "Produit", "Prix", "Quantité", "Date de Dépôt"));

        do {
            String email = rs.getString("EmailUser");
            String product = rs.getString("NomProduit");
            double price = rs.getDouble("PrixOffrePropose");
            int quantity = rs.getInt("QuantiteProduit");
            Timestamp dateTimestamp = rs.getTimestamp("DateHeureDepotOffre");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(dateTimestamp);
            report.append(String.format("%-20s %-20s %-10.2f %-10d %-20s\n", email, product, price, quantity, formattedDate));
        } while (rs.next());

        if (this.isSaleEnded(idVente)) {
                report.append("\n La vente est terminée");
            }

        return report.toString();
    }


    public String getTypeVente(long idVente) throws SQLException {
    String typeVente = null;

    String commande = "SELECT TypeVente FROM Vente WHERE IdVente = ?";
    PreparedStatement stmt = connection.prepareStatement(commande);
    stmt.setLong(1, idVente);

    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
        typeVente = rs.getString("TypeVente");
    } else {
        throw new SQLException("Aucune vente trouvée avec l'identifiant " + idVente);
    }
    rs.close();
    stmt.close();

    return typeVente;
}

    public boolean isOfferPlaced(long idVente) throws SQLException {
    boolean offerExists = false;
    PreparedStatement stmt = connection.prepareStatement("SELECT 1 FROM Offre WHERE IdVente = ? FETCH FIRST 1 ROW ONLY");
    stmt.setLong(1, idVente);
    ResultSet rs = stmt.executeQuery();
    if (rs.next()) {
        offerExists = true;
    }
    rs.close();
    stmt.close();

    return offerExists;
}

    public double getPrixminimal(long idVente) throws SQLException {
    String query = "SELECT PrixDepart, DateHeureDebutVente FROM Vente WHERE IdVente = ? AND TypeVente = 'decroissante'";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setLong(1, idVente);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                double prixDepart = rs.getDouble("PrixDepart");
                Timestamp dateDebut = rs.getTimestamp("DateHeureDebutVente");

                long minutesElapsed = java.time.Duration.between(
                    dateDebut.toLocalDateTime(), 
                    java.time.LocalDateTime.now()
                ).toMinutes();

                double reductionRate = 0.05; //  - 5% par minute
                double prixMinimal = prixDepart * Math.pow(1 - reductionRate, minutesElapsed);
                if (prixMinimal > 0) {
                    return prixMinimal;
                }
                else {
                    return 0;
                }
            } else {
                throw new SQLException("Vente non trouvée ou n'est pas de type décroissante.");
            }
        }

        
    }
}

    public boolean limiteOffreAtteinte(String emailUtilisateur, long idVente) throws SQLException {
    try (PreparedStatement stmt = connection.prepareStatement("SELECT LimiteOffre FROM Vente WHERE IdVente = ?")) {
        stmt.setLong(1, idVente);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int limiteOffre = rs.getInt("LimiteOffre");

                if (limiteOffre == 1) {
                    try (PreparedStatement offerStmt = connection.prepareStatement("SELECT 1 FROM Offre WHERE EMAILUSER = ? AND IDVENTE = ? FETCH FIRST 1 ROW ONLY")) {
                        offerStmt.setString(1, emailUtilisateur);
                        offerStmt.setLong(2, idVente);

                        try (ResultSet offerRs = offerStmt.executeQuery()) {
                            return offerRs.next();
                        }
                    }
                }
            }
        }
    }

    return false;
}
}
