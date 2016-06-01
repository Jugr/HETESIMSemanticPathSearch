package ownClasses.presentation;

import ownClasses.domain.domainControllers.DomainMainController;
import ownClasses.domain.domainControllers.DomainPersistanceController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by iansangines on 01/06/2016.
 */
public class PresentationEditNode extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JButton acceptarButton;
    private JButton cancelarButton;
    private DomainMainController domainMainController;
    private JPanel panel1;

    public PresentationEditNode(DomainMainController domainMainController, int selectedIndex) {
        super("DELETE NODE");
        $$$setupUI$$$();
        setContentPane(panel1);
        pack();

        this.domainMainController = domainMainController;

        switch (selectedIndex) {
            case (0):
                acceptarButton.addActionListener(e -> callEditAuthor());
                break;
            case (1):
                acceptarButton.addActionListener(e -> callEditPaper());
                break;
            case (2):
                acceptarButton.addActionListener(e -> callEditTerm());
                break;
            case (3):
                acceptarButton.addActionListener(e -> callEditConference());
                break;
        }

        setVisible(true);
    }

    public void callEditAuthor() {
        String authorName = textField1.getText();
        String editName = textField2.getText();
        if (authorName == null || authorName.equals("")) {
            VistaWARNING vm = new VistaWARNING();
            vm.setVisible("Introdueix un Autor a editar");
            return;
        } else if (editName == null || editName.equals("")) {
            VistaWARNING vm = new VistaWARNING();
            vm.setVisible("Introdueix un nom a editar");
            return;

        } else {
            boolean edited = domainMainController.getPersistanceController().editAuthor(authorName, editName,domainMainController.getAuthorsById(),domainMainController.getAuthorsByName());
            if (!edited) {
                VistaWARNING vm = new VistaWARNING();
                vm.setVisible("L'autor no existeix");
            } else {
                JOptionPane.showMessageDialog(panel1, "S'ha editat l'Autor");
            }
        }
    }

    public void callEditPaper() {
        String paperName = textField1.getText();
        String editName = textField2.getText();
        if (paperName == null || paperName.equals("")) {
            VistaWARNING vm = new VistaWARNING();
            vm.setVisible("Introdueix un Article a editar");
            return;
        } else if (editName == null || editName.equals("")) {
            VistaWARNING vm = new VistaWARNING();
            vm.setVisible("Introdueix un nom a editar");
            return;

        } else {
            boolean edited = domainMainController.getPersistanceController().editPaper(paperName, editName,domainMainController.getPapersById(),domainMainController.getPapersByName());
            if (!edited) {
                VistaWARNING vm = new VistaWARNING();
                vm.setVisible("L'Article no existeix");
            } else {
                JOptionPane.showMessageDialog(panel1, "S'ha editat l'Article");
            }
        }
    }

    public void callEditTerm() {
        String termName = textField1.getText();
        String editName = textField2.getText();
        if (termName == null || termName.equals("")) {
            VistaWARNING vm = new VistaWARNING();
            vm.setVisible("Introdueix un Terme a editar");
            return;
        } else if (editName == null || editName.equals("")) {
            VistaWARNING vm = new VistaWARNING();
            vm.setVisible("Introdueix un nom a editar");
            return;

        } else {
            boolean edited = domainMainController.getPersistanceController().editTerm(termName, editName,domainMainController.getTermsById(),domainMainController.getTermsByName());
            if (!edited) {
                VistaWARNING vm = new VistaWARNING();
                vm.setVisible("El terme no existeix");
            } else {
                JOptionPane.showMessageDialog(panel1, "S'ha editat el Terme");
            }
        }
    }

    public void callEditConference() {
        String conferenceName = textField1.getText();
        String editName = textField2.getText();
        if (conferenceName == null || conferenceName.equals("")) {
            VistaWARNING vm = new VistaWARNING();
            vm.setVisible("Introdueix una  Conferencia a editar");
            return;
        } else if (editName == null || editName.equals("")) {
            VistaWARNING vm = new VistaWARNING();
            vm.setVisible("Introdueix un nom a editar");
            return;

        } else {
            boolean edited = domainMainController.getPersistanceController().editConference(conferenceName, editName,domainMainController.getConferencesById(),domainMainController.getConferencesByName());
            if (!edited) {
                VistaWARNING vm = new VistaWARNING();
                vm.setVisible("La conferencia no existeix");
            } else {
                JOptionPane.showMessageDialog(panel1, "S'ha editat la conferencia");
            }
        }
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        final JPanel spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Nom:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipady = 20;
        gbc.insets = new Insets(0, 10, 0, 0);
        panel1.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Nom nou:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipady = 20;
        gbc.insets = new Insets(0, 10, 0, 0);
        panel1.add(label2, gbc);
        textField1 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 350;
        gbc.ipady = 6;
        gbc.insets = new Insets(0, 0, 3, 0);
        panel1.add(textField1, gbc);
        textField2 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 6;
        gbc.insets = new Insets(3, 0, 0, 0);
        panel1.add(textField2, gbc);
        cancelarButton = new JButton();
        cancelarButton.setText("Cancelar");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 40;
        gbc.ipady = 6;
        gbc.insets = new Insets(50, 20, 10, 20);
        panel1.add(cancelarButton, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Edit Node");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 130, 0, 0);
        panel1.add(label3, gbc);
        acceptarButton = new JButton();
        acceptarButton.setMaximumSize(new Dimension(36, 32));
        acceptarButton.setMinimumSize(new Dimension(36, 32));
        acceptarButton.setPreferredSize(new Dimension(36, 32));
        acceptarButton.setText("Acceptar");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 200;
        gbc.insets = new Insets(50, 10, 10, 0);
        panel1.add(acceptarButton, gbc);
    }
}