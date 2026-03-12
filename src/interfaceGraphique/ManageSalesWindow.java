import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import requests.*;

public class ManageSalesWindow extends JFrame {
    private JTextField idSaleField;
    private JTextField idRoomField;
    private JTextField productField;
    private JTextField startPriceField;
    private JTextField typeVenteField;
    private JTextField isRevocableField;
    private JTextField isLimitedField;
    private JTextField limitDateField;
    private Requetes requetes;

    public ManageSalesWindow(Requetes requetes) {
        this.requetes = requetes;

        setTitle("Ajouter une Vente");
        setSize(1000, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel idSaleLabel = new JLabel("ID de la Vente:");
        idSaleField = new JTextField(20);
        JLabel idRoomLabel = new JLabel("ID de la Salle:");
        idRoomField = new JTextField(20);
        JLabel productLabel = new JLabel("ID du Produit:");
        productField = new JTextField(20);
        JLabel startPriceLabel = new JLabel("Prix de départ:");
        startPriceField = new JTextField(20);
        JLabel typeVenteLabel = new JLabel("Type de Vente (croissante ou decroissante):");
        typeVenteField = new JTextField(20);
        JLabel isRevocableLabel = new JLabel("Est révocable (oui ou non):");
        isRevocableField = new JTextField(20);
        JLabel isLimitedLabel = new JLabel("Est limitée à une offre par utilisateur (oui ou non):");
        isLimitedField = new JTextField(20);
        JLabel limitDateLabel = new JLabel("Date limite, format (YYYY-MM-DD-HH-MM-SS), ne rien mettre pour illimité:");
        limitDateField = new JTextField(20);
        JButton validateButton = new JButton("Valider");

        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    long idSale = Long.parseLong(idSaleField.getText());
                    long idRoom = Long.parseLong(idRoomField.getText());
                    long idProduct = Long.parseLong(productField.getText());
                    double startPriceDouble = Double.parseDouble(startPriceField.getText());
                    String typeVenteString = typeVenteField.getText();
                    if(!isRevocableField.getText().equals("oui") && !isRevocableField.getText().equals("non")) {
                        JOptionPane.showMessageDialog(null, "Erreur: isRevocable doit être 'oui' ou 'non'", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    boolean isRevocableBoolean = isRevocableField.getText().equals("oui");
                    if(!isLimitedField.getText().equals("oui") && !isLimitedField.getText().equals("non")) {
                        JOptionPane.showMessageDialog(null, "Erreur: isLimited doit être 'oui' ou 'non'", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    boolean isLimitedBoolean = isLimitedField.getText().equals("oui");
                    String limitDateString = limitDateField.getText();
                    Timestamp limitDateTimestamp = null;
                    if (!limitDateString.isEmpty()) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
                            LocalDateTime limitDateTime = LocalDateTime.parse(limitDateString, formatter);
                            limitDateTimestamp = Timestamp.valueOf(limitDateTime);
                        } catch (DateTimeParseException ex) {
                            JOptionPane.showMessageDialog(null, "Erreur de format de date: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    requetes.addSale(idSale, idRoom, idProduct, startPriceDouble, typeVenteString, isRevocableBoolean, isLimitedBoolean, limitDateTimestamp);
                    JOptionPane.showMessageDialog(null, "Vente ajoutée avec succès!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(idSaleLabel);
        add(idSaleField);
        add(idRoomLabel);
        add(idRoomField);
        add(productLabel);
        add(productField);
        add(startPriceLabel);
        add(startPriceField);
        add(typeVenteLabel);
        add(typeVenteField);
        add(isRevocableLabel);
        add(isRevocableField);
        add(isLimitedLabel);
        add(isLimitedField);
        add(limitDateLabel);
        add(limitDateField);
        add(validateButton);
    }
}