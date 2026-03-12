import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import requests.Requetes;

public class ManageSalesRoomWindow extends JFrame {
    private JTextField idRoomField;
    private JTextField categoryNameField;
    private Requetes requetes;

    public ManageSalesRoomWindow(Requetes request) {
        this.requetes = request;

        setTitle("Ajouter une Salle de Vente");
        setSize(600, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel idRoomLabel = new JLabel("ID de la Salle:");
        idRoomField = new JTextField(20);
        JLabel categoryNameLabel = new JLabel("Nom de la Catégorie:");
        categoryNameField = new JTextField(20);
        JButton validateButton = new JButton("Valider");

        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    long idRoom = Long.parseLong(idRoomField.getText());
                    String categoryName = categoryNameField.getText();
                    requetes.addRoom(idRoom, categoryName);
                    JOptionPane.showMessageDialog(null, "Salle ajoutée avec succès!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(idRoomLabel);
        add(idRoomField);
        add(categoryNameLabel);
        add(categoryNameField);
        add(validateButton);
    }
}