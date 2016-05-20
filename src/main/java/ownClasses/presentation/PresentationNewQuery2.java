package main.java.ownClasses.presentation;

import main.java.ownClasses.domain.domainControllers.DomainMainController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Nicola on 15/05/2016.
 */
public class PresentationNewQuery2 extends JFrame {
    private JPanel panel1;
    private JRadioButton ascendentButton;
    private JRadioButton descendentButton;
    private JRadioButton SimpleButton;
    private JRadioButton LimitedButton;
    private JRadioButton IntervaledButton;
    private JTextField nField;
    private JTextField petitField;
    private JTextField granField;
    private JTextField nameField;
    private JButton NEXTButton;

    boolean ascendent = true;
    String name = "";
    int n = 0;
    double max = 0.0;
    double min = 0.0;

    int querytype = 1;

    public PresentationNewQuery2(DomainMainController mainController, String path) {
        super("NEW QUERY2");

        $$$setupUI$$$();

        ButtonGroup group1 = new ButtonGroup();
        group1.add(ascendentButton);
        group1.add(descendentButton);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(SimpleButton);
        group2.add(LimitedButton);
        group2.add(IntervaledButton);

        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ascendentButton.addActionListener(e -> setascendent(true));
        descendentButton.addActionListener(e -> setascendent(false));

        SimpleButton.addActionListener(e1 -> setType(1));
        LimitedButton.addActionListener(e2 -> setType(2));
        IntervaledButton.addActionListener(e2 -> setType(3));

        nField.addActionListener(e1 -> setN());
        petitField.addActionListener(e1 -> setMin());
        granField.addActionListener(e1 -> setMax());

        nameField.addActionListener(e -> setname(mainController));

        NEXTButton.addActionListener(e -> callresult(mainController, path));

        setVisible(true);

    }

    private void setascendent(boolean set) {
        ascendent = set;
        System.out.println("set ascendent to " + ascendent);
    }

    private void setname(DomainMainController mainController) {
        if(mainController.checkName(nameField.getText())) name = nameField.getText();
        else{
            VistaWARNING vw = new VistaWARNING();
            vw.setVisible("NOM no trobat");
        }
    }

    private void setType(int type) {
        querytype = type;
        System.out.println("set type to " + type);
    }

    private void setN() {
        n = Integer.parseInt(nField.getText());
    }

    private void setMin() {
        min = Double.parseDouble(petitField.getText());
    }

    private void setMax() {
        max = Double.parseDouble(granField.getText());
    }

    private void callresult(DomainMainController mainController, String path) {
        ArrayList<String> resultat = mainController.resultat(path, querytype, ascendent, name, n, max, min);
        new PresentationResult(resultat,name,path);
    }

    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        final JLabel label1 = new JLabel();
        label1.setText("Selecciona Ordre");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Selecciona Tipus");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label2, gbc);
        ascendentButton = new JRadioButton();
        ascendentButton.setText("Ascendent");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(ascendentButton, gbc);
        descendentButton = new JRadioButton();
        descendentButton.setText("Descendent");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(descendentButton, gbc);
        SimpleButton = new JRadioButton();
        SimpleButton.setSelected(false);
        SimpleButton.setText("Simple");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(SimpleButton, gbc);
        LimitedButton = new JRadioButton();
        LimitedButton.setText("Nombre Maxim Resultats");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(LimitedButton, gbc);
        IntervaledButton = new JRadioButton();
        IntervaledButton.setText("Intervals");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(IntervaledButton, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Introdueix el numero");
        label3.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label3, gbc);
        nField = new JTextField();
        nField.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(nField, gbc);
        final JLabel label4 = new JLabel();
        label4.setRequestFocusEnabled(false);
        label4.setText("petit");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label4, gbc);
        final JLabel label5 = new JLabel();
        label5.setRequestFocusEnabled(false);
        label5.setText("gran");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label5, gbc);
        petitField = new JTextField();
        petitField.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(petitField, gbc);
        granField = new JTextField();
        granField.setText("");
        granField.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(granField, gbc);
        final JLabel label6 = new JLabel();
        label6.setOpaque(true);
        label6.setText("Introdueix el nom:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label6, gbc);
        nameField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 5;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(nameField, gbc);
        NEXTButton = new JButton();
        NEXTButton.setText("NEXT");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(NEXTButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}