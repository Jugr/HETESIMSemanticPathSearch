package ownClasses.presentation;

import ownClasses.domain.domainControllers.DomainMainController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import ownClasses.domain.domainControllers.Persistance.BinaryAuthors;
import ownClasses.domain.domainControllers.Persistance.BinaryConferences;
import ownClasses.domain.domainControllers.Persistance.BinaryPapers;
import ownClasses.domain.domainControllers.Persistance.BinaryTerms;

/**
 * Created by andreurd on 03/05/16.
 */
public class PresentationController extends JFrame {

    public PresentationController(DomainMainController mainController) {
        super("HETESIM SEMANTIC PATH SEARCH");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        final JLabel label1 = new JLabel();
        label1.setText("Hetesim Semantic Path Search");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        JButton newQueryButton = new JButton();
        newQueryButton.setText("New Query");
        JButton editGraphButton = new JButton();
        editGraphButton.setText("Edit Graph");

        panel.add(label1);
        panel.add(newQueryButton);
        panel.add(editGraphButton);

        BrowseFileController browseFileController = new BrowseFileController(mainController);
        panel.add(browseFileController);

        setContentPane(panel);
        pack();
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                BinaryAuthors binaryAuthors = new BinaryAuthors();
                BinaryPapers binaryPapers = new BinaryPapers();
                BinaryConferences binaryConferences = new BinaryConferences();
                BinaryTerms binaryTerms = new BinaryTerms();
                binaryAuthors.write(mainController.getAuthorsById());
                binaryPapers.write(mainController.getPapersById());
                binaryTerms.write(mainController.getTermsById());
                binaryConferences.write(mainController.getConferencesById());
                System.exit(0);
            }
        });

        newQueryButton.addActionListener(e -> new PresentationNewQuery(mainController,mainController.getPersistanceController()));
        //newQueryButton.addActionListener(e->mainController.newQuery());
        editGraphButton.addActionListener(e -> new PresentationEditGraph(mainController));
        setVisible(true);
    }
}
