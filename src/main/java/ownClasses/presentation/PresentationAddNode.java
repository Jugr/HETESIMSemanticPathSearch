package main.java.ownClasses.presentation;

import main.java.ownClasses.domain.domainControllers.DomainMainController;
import main.java.sharedClasses.domain.domainControllers.DomainPersistanceController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by andreurd on 24/05/16.
 */
public class PresentationAddNode extends JFrame {
    private static final int AUTHOR = 0;
    private static final int PAPER = 1;
    private static final int TERM = 2;
    private static final int CONFERENCE = 3;
    private final int selectedNodeType;

    private JPanel panel;
    private JButton aButton;
    private JButton cButton;
    private JButton pButton;
    private JButton tButton;
    private JButton NEXTButton;
    private JTextArea TextArea1;
    private JButton DeleteButton;

    private DomainPersistanceController persistanceController;


    int queryType = 0;
    String path = "";

    public PresentationAddNode(DomainPersistanceController persistanceController, int selectedIndex) {
        super("ADD NODE");

        selectedNodeType = selectedIndex;

        this.persistanceController = persistanceController;

        $$$setupUI$$$();

        setContentPane(panel);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        aButton.addActionListener(e1 -> concatpath("A"));
        cButton.addActionListener(e1 -> concatpath("C"));
        pButton.addActionListener(e1 -> concatpath("P"));
        tButton.addActionListener(e1 -> concatpath("T"));

        DeleteButton.addActionListener(e1 -> deletelast());


        setVisible(true);
    }


    private void concatpath(String Node) {
        if ("".equals(path)) path = Node;
        else {
            char last = path.charAt(path.length() - 1);
            if (last == 'P' && "P".equals(Node)) {
                VistaWARNING vw = new VistaWARNING();
                vw.setVisible("Sintaxi de paths incorrecte");
            } else {
                if (last != 'P' && !"P".equals(Node)) path = path.concat("P" + Node);
                else path = path.concat(Node);
            }
        }
        System.out.println(path);
        TextArea1.setText(path);
    }

    private void deletelast() {
        if (!path.isEmpty()) {
            path = path.substring(0, path.length() - 1);
            System.out.println(path);
            TextArea1.setText(path);
        }
    }

    private void callNQ(DomainMainController mainController) {
        if (!path.isEmpty()) {
            mainController.NQ(path);
            //PresentationNewQuery2 window = new PresentationNewQuery2(mainController, path,);
            System.out.println("DONE");
        } else {
            VistaWARNING vw = new VistaWARNING();
            vw.setVisible("PATH buit");
        }
    }

    private void createUIComponents() {
        //simpleRadioButton = new JRadioButton("Simple");
        //ambFiltresRadioButton = new JRadioButton("Amb Filtres");
    }

    private void $$$setupUI$$$() {
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(""));
        GridBagConstraints gbc;
        final JLabel label2 = new JLabel();
        label2.setText("Selecciona el teu PATH");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label2, gbc);
        cButton = new JButton();
        cButton.setText("C");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        panel.add(cButton, gbc);
        tButton = new JButton();
        tButton.setText("T");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        panel.add(tButton, gbc);
        pButton = new JButton();
        pButton.setText("P");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        panel.add(pButton, gbc);
        NEXTButton = new JButton();
        NEXTButton.setText("NEXT");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(NEXTButton, gbc);
        /*simpleRadioButton.setLabel("Simple");
        simpleRadioButton.setText("Simple");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(simpleRadioButton, gbc);
        ambFiltresRadioButton.setText("Amb Filtres");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(ambFiltresRadioButton, gbc);*/
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane1, gbc);
        TextArea1 = new JTextArea();
        scrollPane1.setViewportView(TextArea1);
        aButton = new JButton();
        aButton.setText("A");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        panel.add(aButton, gbc);
        DeleteButton = new JButton();
        DeleteButton.setText("DELETE");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(DeleteButton, gbc);
    }

    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
