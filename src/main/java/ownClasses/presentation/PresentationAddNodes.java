package main.java.ownClasses.presentation;

import main.java.sharedClasses.domain.domainControllers.DomainPersistanceController;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by iansangines on 29/05/2016.
 */
public class PresentationAddNodes extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JTextField aName1;
    private JComboBox comboBox2;
    private JTextField aName2;
    private JTextField aName3;
    private JTextField aName4;
    private JTextField aName5;
    private JTextField aName6;
    private JTextField aName7;
    private JTextField aName8;
    private JTextField aName9;
    private JTextField aName10;
    private JTextField tName1;
    private JTextField cName;
    private JTextField tName2;
    private JTextField tName3;
    private JTextField tName4;
    private JTextField tName5;
    private JTextField tName6;
    private JTextField tName7;
    private JTextField tName8;
    private JTextField tName9;
    private JTextField tName10;
    private JLabel Num1;
    private JLabel Nom;
    private JLabel Conferencia;
    private JLabel Num2;
    private JButton acceptarButton;
    private JButton cancelarButton;
    private JLabel addNode;
    private ArrayList<JTextField> firstTextFields;
    private ArrayList<JTextField> secondTextFields;
    private DomainPersistanceController persistanceController;
    private String authoraux, termaux, confaux, paperaux;

    public PresentationAddNodes(DomainPersistanceController persistanceController, int selecedIndex, String authoraux,String paperaux, String termaux, String confaux) {
        super("ADD NODE");
        $$$setupUI$$$();
        setContentPane(panel1);
        pack();

        this.authoraux = authoraux;
        this.termaux = termaux;
        this.confaux = confaux;
        this.paperaux=paperaux;

        this.persistanceController = persistanceController;
        firstTextFields = new ArrayList<>();
        firstTextFields.add(aName1);
        firstTextFields.add(aName2);
        firstTextFields.add(aName3);
        firstTextFields.add(aName4);
        firstTextFields.add(aName5);
        firstTextFields.add(aName6);
        firstTextFields.add(aName7);
        firstTextFields.add(aName8);
        firstTextFields.add(aName9);
        firstTextFields.add(aName10);

        secondTextFields = new ArrayList<>();
        secondTextFields.add(tName1);
        secondTextFields.add(tName2);
        secondTextFields.add(tName3);
        secondTextFields.add(tName4);
        secondTextFields.add(tName5);
        secondTextFields.add(tName6);
        secondTextFields.add(tName7);
        secondTextFields.add(tName8);
        secondTextFields.add(tName9);
        secondTextFields.add(tName10);

        for(int i = 1; i < firstTextFields.size(); i++) firstTextFields.get(i).setEnabled(false);

        for(int i = 0; i < secondTextFields.size(); i++) secondTextFields.get(i).setEnabled(false);


        comboBox1.addActionListener(e-> enableFields(firstTextFields,comboBox1));
        comboBox2.addActionListener(e-> enableFields(secondTextFields,comboBox2));

        switch (selecedIndex) {
            case (0): { //AUTHOR
                addNode.setText("Afegir Autor");
                Num1.setText("Número d'Articles:");
                Num2.setVisible(false);
                comboBox2.setVisible(false);
                for(int i = 0; i < secondTextFields.size(); i++) secondTextFields.get(i).setVisible(false);
                Conferencia.setVisible(false);
                cName.setVisible(false);
                acceptarButton.addActionListener(e1-> callAddAuthor());

                break;
            }
            case (1): { //PAPER
                addNode.setText("Afegir Article");
                Num1.setText("Número d'Autors");
                Num2.setText("Número de Termes");
                if(authoraux != null){
                    textField1.setText(paperaux);
                    textField1.setEnabled(false);
                    comboBox1.setEnabled(false);
                    firstTextFields.get(0).setText(authoraux);
                    firstTextFields.get(0).setEnabled(false);
                    for(int i = 1; i < firstTextFields.size();i++){
                        firstTextFields.get(i).setVisible(false);
                    }
                }
                acceptarButton.addActionListener(e2-> callAddPaper());
                break;
            }
            case (2): { //TERM
                addNode.setText("Afegir Terme");
                break;
            }
            case (3): { //CONFERENCES
                addNode.setText("Afegir Conferència");
                break;
            }
        }

        setVisible(true);
    }

    private void enableFields(ArrayList<JTextField> a, JComboBox b){
        int numF = Integer.parseInt((String)b.getSelectedItem());
        for(int i = 0; i < numF; i++) a.get(i).setEnabled(true);
    }

    private void callAddAuthor(){
        ArrayList<String> papersToAdd = new ArrayList<>();
        String authorName = textField1.getText();
        if(authorName == null || authorName.equals("")){
            VistaWARNING vw = new VistaWARNING();
            vw.setVisible("Introdueix un nom per l'Autor");
            return;
        }
        if(paperaux != null) {
            for (int i = 0; i < firstTextFields.size(); i++) {
                if (firstTextFields.get(i).isEnabled()) {
                    String pName = firstTextFields.get(i).getText();
                    if (pName == null || pName.equals("")) {
                        VistaWARNING vw = new VistaWARNING();
                        vw.setVisible("Introdueix un nom per l'Article");
                        return;
                    } else {
                        papersToAdd.add(pName);
                    }
                }
            }
        }
        else {
            papersToAdd.add(firstTextFields.get(0).getText());
        }
        ArrayList<String> newPapers = persistanceController.addNewAuthor(authorName,papersToAdd);
        if(newPapers.size() == 0){
            //SHA CREAT TO BIEN
            return;
        }
        else if(newPapers.get(0).equals("Ja Existeix")){
            VistaWARNING vw = new VistaWARNING();
            vw.setVisible("Aquest autor ja existeix");
            return;
        }
        else {
            Object[] options = {"Acceptar", "Cancelar"};
            int op = JOptionPane.showOptionDialog(panel1, "Hi ha Articles que no existeixen, els vols crear?", "WARNING", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if(op == JOptionPane.YES_OPTION){
                for(int i = 0; i < newPapers.size(); i++){
                    new PresentationAddNodes(persistanceController,1,authorName,newPapers.get(i),null,null);
                }
            }
            else{
                persistanceController.deleteAuthor(authorName);
                return;
            }
        }
        super.dispose();
    }


    private void callAddPaper(){
        ArrayList<String> authorToAdd = new ArrayList<>();
        ArrayList<String> termsToAdd = new ArrayList<>();
        String confToAdd;
        String paperName = textField1.getText();
        if(paperName == null || paperName.equals("")){
            VistaWARNING vw = new VistaWARNING();
            vw.setVisible("Introdueix un nom per l'Article");
            return;
        }
        if(authoraux != null ){
            for(int i = 0; i < firstTextFields.size(); i++){
                if(firstTextFields.get(i).isEnabled()){
                    String aName = firstTextFields.get(i).getText();
                    if(aName == null || aName.equals("")) {
                        VistaWARNING vw = new VistaWARNING();
                        vw.setVisible("Introdueix un nom per l'Autor");
                        return;
                    }
                    else {
                        authorToAdd.add(aName);
                    }
                }
            }
        }
        else{
            authorToAdd.add(firstTextFields.get(0).getText());
        }

        if(termaux != null){
            for(int i = 0; i < firstTextFields.size(); i++){
                if(firstTextFields.get(i).isEnabled()){
                    String tName = firstTextFields.get(i).getText();
                    if(tName == null || tName.equals("")) {
                        VistaWARNING vw = new VistaWARNING();
                        vw.setVisible("Introdueix un nom per el Terme");
                        return;
                    }
                    else {
                        termsToAdd.add(tName);
                    }
                }
            }
        }
        else{
            termsToAdd.add(secondTextFields.get(0).getText());
        }

        confToAdd = cName.getText();
        if (confToAdd == null || confToAdd.equals("")){
            VistaWARNING vw = new VistaWARNING();
            vw.setVisible("Introdueix un nom per la Conferència");
            return;
        }

        HashMap<String,ArrayList<String>> newNodes = persistanceController.addNewPaper(paperName,authorToAdd,termsToAdd,confToAdd);

        if(newNodes.get("Fail") != null){
            VistaWARNING vw = new VistaWARNING();
            vw.setVisible("L'article ja existeix");
        }
        else {
            Object[] options = {"Acceptar", "Cancelar"};
            int op = JOptionPane.showOptionDialog(panel1, "Hi ha Autors que no existeixen, els vols crear?", "WARNING", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if(op == JOptionPane.YES_OPTION) {
                ArrayList<String> newAuthors = newNodes.get("A");
                for (int i = 0; i < newAuthors.size(); i++) {
                    paperaux = paperName;
                    callAddAuthor();
                }
            }

            ArrayList<String> newTerms = newNodes.get("T");
            for(int i = 0; i < newTerms.size(); i++){
               // new PresentationAddNodes(persistanceController,2,null,paperName,null,null);
            }

            ArrayList<String> newConference = newNodes.get("C");
            for(int i = 0; i < newConference.size(); i++){
               // new PresentationAddNodes(persistanceController,3,null,paperName,null,null);
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
        Nom = new JLabel();
        Nom.setText("Nom");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 20;
        gbc.insets = new Insets(0, 20, 0, 0);
        panel1.add(Nom, gbc);
        Num1 = new JLabel();
        Num1.setText("Num 1:");
        Num1.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 20;
        gbc.insets = new Insets(0, 20, 0, 0);
        panel1.add(Num1, gbc);
        textField1 = new JTextField();
        textField1.setMaximumSize(new Dimension(14, 24));
        textField1.setPreferredSize(new Dimension(14, 28));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 100;
        gbc.insets = new Insets(0, 20, 0, 20);
        panel1.add(textField1, gbc);
        Num2 = new JLabel();
        Num2.setText("Num 2:");
        Num2.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 20;
        gbc.insets = new Insets(0, 20, 0, 0);
        panel1.add(Num2, gbc);
        String[] numbers = {"1","2","3","4","5","6","7","8","9","10"};
        comboBox2 = new JComboBox(numbers);
       // final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
       // comboBox2.setModel(defaultComboBoxModel1);
        comboBox2.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 0, 0);
        panel1.add(comboBox2, gbc);
        Conferencia = new JLabel();
        Conferencia.setText("Conferencia");
        Conferencia.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 22;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipady = 20;
        gbc.insets = new Insets(0, 20, 0, 0);
        panel1.add(Conferencia, gbc);
        cName = new JTextField();
        cName.setPreferredSize(new Dimension(14, 28));
        cName.setText("");
        cName.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 22;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 0, 10);
        panel1.add(cName, gbc);
        aName1 = new JTextField();
        aName1.setMinimumSize(new Dimension(14, 28));
        aName1.setPreferredSize(new Dimension(14, 28));
        aName1.setText("");
        aName1.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 150;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel1.add(aName1, gbc);
        aName2 = new JTextField();
        aName2.setMinimumSize(new Dimension(14, 28));
        aName2.setPreferredSize(new Dimension(14, 28));
        aName2.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 150;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel1.add(aName2, gbc);
        aName3 = new JTextField();
        aName3.setMinimumSize(new Dimension(14, 28));
        aName3.setPreferredSize(new Dimension(14, 28));
        aName3.setText("");
        aName3.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 150;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(aName3, gbc);
        aName4 = new JTextField();
        aName4.setMinimumSize(new Dimension(14, 28));
        aName4.setPreferredSize(new Dimension(14, 28));
        aName4.setText("");
        aName4.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 150;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(aName4, gbc);
        aName5 = new JTextField();
        aName5.setMinimumSize(new Dimension(14, 28));
        aName5.setPreferredSize(new Dimension(14, 28));
        aName5.setText("");
        aName5.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(aName5, gbc);
        aName6 = new JTextField();
        aName6.setMinimumSize(new Dimension(14, 28));
        aName6.setPreferredSize(new Dimension(14, 28));
        aName6.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(aName6, gbc);
        aName7 = new JTextField();
        aName7.setMinimumSize(new Dimension(14, 28));
        aName7.setPreferredSize(new Dimension(14, 28));
        aName7.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(aName7, gbc);
        aName8 = new JTextField();
        aName8.setMinimumSize(new Dimension(14, 28));
        aName8.setPreferredSize(new Dimension(14, 28));
        aName8.setText("");
        aName8.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 9;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(aName8, gbc);
        aName9 = new JTextField();
        aName9.setMinimumSize(new Dimension(14, 28));
        aName9.setPreferredSize(new Dimension(14, 28));
        aName9.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 10;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(aName9, gbc);
        aName10 = new JTextField();
        aName10.setMinimumSize(new Dimension(14, 28));
        aName10.setPreferredSize(new Dimension(14, 28));
        aName10.setText("");
        aName10.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 11;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(aName10, gbc);
        tName1 = new JTextField();
        tName1.setMinimumSize(new Dimension(14, 28));
        tName1.setPreferredSize(new Dimension(14, 28));
        tName1.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 12;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(tName1, gbc);
        tName2 = new JTextField();
        tName2.setMinimumSize(new Dimension(14, 28));
        tName2.setPreferredSize(new Dimension(14, 28));
        tName2.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 13;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(tName2, gbc);
        tName3 = new JTextField();
        tName3.setMinimumSize(new Dimension(14, 28));
        tName3.setPreferredSize(new Dimension(14, 28));
        tName3.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 14;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(tName3, gbc);
        tName4 = new JTextField();
        tName4.setMinimumSize(new Dimension(14, 28));
        tName4.setPreferredSize(new Dimension(14, 28));
        tName4.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 15;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(tName4, gbc);
        tName5 = new JTextField();
        tName5.setMinimumSize(new Dimension(14, 28));
        tName5.setPreferredSize(new Dimension(14, 28));
        tName5.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 16;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(tName5, gbc);
        tName6 = new JTextField();
        tName6.setMinimumSize(new Dimension(14, 28));
        tName6.setPreferredSize(new Dimension(14, 28));
        tName6.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 17;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(tName6, gbc);
        tName7 = new JTextField();
        tName7.setMinimumSize(new Dimension(14, 28));
        tName7.setPreferredSize(new Dimension(14, 28));
        tName7.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 18;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(tName7, gbc);
        tName8 = new JTextField();
        tName8.setMinimumSize(new Dimension(14, 28));
        tName8.setPreferredSize(new Dimension(14, 28));
        tName8.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 19;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(tName8, gbc);
        tName9 = new JTextField();
        tName9.setMinimumSize(new Dimension(14, 28));
        tName9.setPreferredSize(new Dimension(14, 28));
        tName9.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 20;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(tName9, gbc);
        addNode = new JLabel();
        addNode.setText("Add Node");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.ipadx = 100;
        gbc.ipady = 30;
        gbc.insets = new Insets(0, 20, 0, 0);
        panel1.add(addNode, gbc);
        comboBox1 = new JComboBox(numbers);
        //final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        //comboBox1.setModel(defaultComboBoxModel2);
        comboBox1.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 0, 0);
        panel1.add(comboBox1, gbc);

        tName10 = new JTextField();
        tName10.setMinimumSize(new Dimension(14, 28));
        tName10.setPreferredSize(new Dimension(14, 28));
        tName10.setText("");
        tName10.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 21;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 10, 3, 10);
        panel1.add(tName10, gbc);
        cancelarButton = new JButton();
        cancelarButton.setText("Cancelar");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 23;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 20);
        panel1.add(cancelarButton, gbc);
        acceptarButton = new JButton();
        acceptarButton.setHorizontalAlignment(0);
        acceptarButton.setText("Acceptar");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 23;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.ipadx = 30;
        gbc.insets = new Insets(0, 0, 10, 200);
        panel1.add(acceptarButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
