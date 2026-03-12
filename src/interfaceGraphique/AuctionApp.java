import java.awt.*;
import javax.swing.*;
import java.sql.*;

import connectivite.*;
import EndTask.*;
import requests.*;

public class AuctionApp extends JFrame {

    private String userEmail;
    private Requetes request;


    public AuctionApp(String email, Requetes requetes) {
        this.userEmail = email;
        this.request = requetes;
        try{
            request.setAutoCommit(Constants.AUTOCCOMMIT.OFF);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la désactivation du mode auto-commit: " + e.getMessage());
        }
        
        setTitle("Baie-électronique - Gestion des Enchères");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(200, 200, 200));
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(200, 200, 200));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel titleLabel = new JLabel("Bienvenue sur Baie-électronique", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 102, 204));

        JLabel userLabel = new JLabel("Connecté en tant que : " + userEmail, JLabel.CENTER);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userLabel.setForeground(new Color(50, 50, 50));

        titlePanel.add(titleLabel);
        titlePanel.add(userLabel);
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.setBackground(new Color(255, 255, 255));

        JButton btnManageSales = createStyledButton("Gérer les Ventes");
        JButton btnManageSalesRooms = createStyledButton("Gérer les Salles de Vente");
        JButton btnShowSale = createStyledButton("Voir une vente");
        JButton btnManageOffers = createStyledButton("Faire une offre");
        JButton btnExit = createStyledButton("Quitter");

        buttonPanel.add(btnManageSales);
        buttonPanel.add(btnManageSalesRooms);
        buttonPanel.add(btnShowSale);
        buttonPanel.add(btnManageOffers);
        buttonPanel.add(btnExit);

        btnExit.addActionListener(e -> System.exit(0));
        btnManageSales.addActionListener(e -> {
            ManageSalesWindow manageSalesWindow = new ManageSalesWindow(request);
            manageSalesWindow.setVisible(true);
        });
        btnManageSalesRooms.addActionListener(e -> {
                ManageSalesRoomWindow manageSalesRoomWindow = new ManageSalesRoomWindow(request);
                manageSalesRoomWindow.setVisible(true);
            });
        btnShowSale.addActionListener(e -> showSaleWindow("Voir une vente", new String[]{"ID Vente"}));
        btnManageOffers.addActionListener(e -> showOfferWindow());

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

        GlobalVaraibles.endOfSaleUpdate = Constants.maxTimestamp;
        try {
            request.updateNextSaleToEnd();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la prochaine vente à terminer: " + e.getMessage());
        }

        CheckEndOfSaleTask checkEndOfSaleTask = new CheckEndOfSaleTask(request);
        Thread checkEndOfSaleThread = new Thread(checkEndOfSaleTask);
        checkEndOfSaleThread.start();
    }


    private void showOfferWindow() {
        JFrame frame = new JFrame("Gestion des Offres");
        frame.setSize(380, 380);
        frame.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));  
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField emailField = new JTextField(userEmail); 
        emailField.setEditable(false);
        JTextField idVenteField = new JTextField();
        JTextField prixOffreField = new JTextField();
        JTextField quantiteProduitField = new JTextField();

        formPanel.add(new JLabel("Email utilisateur:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Id Vente:"));
        formPanel.add(idVenteField);
        formPanel.add(new JLabel("Prix Offre Proposé:"));
        formPanel.add(prixOffreField);
        formPanel.add(new JLabel("Quantité Produit:"));
        formPanel.add(quantiteProduitField);

        JButton submitButton = createStyledButton("Valider");
        submitButton.addActionListener(e -> {
            try {
                String emailUser = userEmail;
                int idVente = Integer.parseInt(idVenteField.getText().trim());
                double prixOffre = Double.parseDouble(prixOffreField.getText().trim());
                int quantiteProduit = Integer.parseInt(quantiteProduitField.getText().trim());
                Timestamp dateHeureDepotOffre = new Timestamp(System.currentTimeMillis());  // Date de l'offre générée automatiquement

                request.PlaceOffer(emailUser, idVente, prixOffre, quantiteProduit, dateHeureDepotOffre);
                JOptionPane.showMessageDialog(frame, "Offre placée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose(); 
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de la validation de l'offre : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erreur de saisie : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(submitButton);
        frame.add(formPanel);
        frame.setVisible(true);
    }


    private void showSaleWindow(String title, String[] fields) {
        JFrame frame = new JFrame(title);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        JPanel formPanel = new JPanel(new GridLayout(fields.length + 2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel idVenteLabel = new JLabel("ID Vente:");
        JTextField idVenteField = new JTextField();
        formPanel.add(idVenteLabel);
        formPanel.add(idVenteField);
        JButton confirmButton = new JButton("Confirmer");
        formPanel.add(new JLabel(""));
        formPanel.add(confirmButton);
        JTextArea rapportArea = new JTextArea(10, 30);
        rapportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(rapportArea);
        frame.add(scrollPane, BorderLayout.SOUTH);
        confirmButton.addActionListener(e -> {
            String idVenteStr = idVenteField.getText().trim();
            if (idVenteStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Veuillez entrer un ID de vente valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int idVente = Integer.parseInt(idVenteStr); 
                try {
                    long number = Long.parseLong(idVenteStr);
                    if (!(request.saleExists(number))) {
                        JOptionPane.showMessageDialog(frame, "Cet ID n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException exe) {
                    JOptionPane.showMessageDialog(frame, "Erreur: " + exe.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                String rapport = generateReport(idVente);
                rapportArea.setText(rapport);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "L'ID de vente doit être un entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(formPanel);
        frame.setVisible(true);
    }

    private String generateReport(int idVente) {
        String rapport = "";
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            rapport = request.GenerateAuctionReport(idVente);
        } catch (SQLException e) {
            rapport = "Erreur lors de la génération du rapport : " + e.getMessage();
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection); 
        }
        return rapport;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (text.equals("Quitter")) {
            button.setBackground(new Color(204, 0, 0));
        } else {
            button.setBackground(new Color(0, 153, 204));
        }
        return button;
    }

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            Requetes request = new Requetes(connection);
            
            SwingUtilities.invokeLater(() -> showLoginScreen(request));
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données: " + e.getMessage());
            System.exit(1);
        }

        
    }

    public static void showLoginScreen(Requetes request) {

        

        JFrame loginFrame = new JFrame("Connexion");
        loginFrame.setSize(400, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel emailLabel = new JLabel("Entrez votre email :", JLabel.CENTER);
        JTextField emailField = new JTextField();
        JButton loginButton = new JButton("Se connecter");

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            try { 
                if (!email.isEmpty() && request.checkUser(email)) {
                    loginFrame.dispose();
                    new AuctionApp(email, request);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Veuillez entrer un email valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(loginFrame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginPanel.add(emailLabel);
        loginPanel.add(emailField);
        loginPanel.add(loginButton);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);
    }
}
