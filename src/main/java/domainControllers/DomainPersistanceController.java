package main.java.domainControllers;

import main.java.domain.nodes.Author;
import main.java.domain.nodes.Conference;
import main.java.domain.nodes.Paper;
import main.java.domain.nodes.Term;
import main.java.*;
import main.java.presentation.ConsolePrinter;

import java.io.*;
import java.util.*;

public class DomainPersistanceController {

    public DomainPersistanceController() {}

    public void readAll(HashMap<Integer, Author> authors, HashMap<Integer, Paper> papers,
                        HashMap<Integer, Term> terms, HashMap<Integer, Conference> conferences,
                        int authorMaxId, int paperMaxId, int termMaxId, int conferenceMaxId){
        readAuthorsFromFile(authors);
        readPapersFromFile(papers);
        readConferencesFromFile(conferences);
        readTermsFromFile(terms);
    }


    public void newEdit(HashMap<Integer, Author> authors, HashMap<Integer, Paper> papers,
                        HashMap<Integer, Term> terms, HashMap<Integer, Conference> conferences,
                        int authorMaxId, int paperMaxId, int termMaxId, int conferenceMaxId){

        ConsolePrinter print = new ConsolePrinter();
        print.printEditInsertOrDelete();
        Scanner scan = new Scanner(System.in);
        String editType = scan.nextLine();
        print.printTypeInputEditMessage();
        String objectType = scan.nextLine();
        switch(editType){
            case("In"): //agefir
                Scanner aux = new Scanner(System.in);
                String objName;
                switch(objectType){
                case("A"):
                    System.out.print("Quin nom te ");
                    objName = aux.nextLine();
                    System.out.print("Te relacio amb algun Paper? (0 -> no en te, nomdelsPapers separats per ; )");
                    String paperNames = aux.nextLine();
                    authorMaxId = authorMaxId + 1;
                    Author author = new Author(objName, authorMaxId);
                    if(!paperNames.equals("0")) {
                        String relationedPapers[] = paperNames.split(";");
                        for (String p : relationedPapers) {
                            for(Integer id:papers.keySet()){
                                Paper relatedPaper = papers.get(id);
                                if(relatedPaper.getName().equals(p)){
                                    relatedPaper.addAuthor(author.getId(),author);
                                    author.addPaper(id,relatedPaper);
                                    writeNewRelationPaperAuthor(relatedPaper,author);
                                    break;
                                }
                            }
                        }
                    }
                    authors.put(authorMaxId,author);
                    writeAuthorToFile(author);
                    writeAuthorRelations(author);
                    break;
                case("P"):
                    System.out.print("Quin nom te ");
                    objName = aux.nextLine();
                    paperMaxId = paperMaxId + 1;
                    Paper paper = new Paper(objName, paperMaxId);
                    //Autors relacionats
                    System.out.print("Te relacio amb algun Autor? (0 -> no en te, nom dels autors separats per ; )");
                    String authorNames = aux.nextLine();
                    System.out.print("Te relacio amb algun Tema? (0 -> no en te, nom dels autors separats per ; )"); //Per a que no es noti si triga molt
                    if(!authorNames.equals("0")) {
                        String relationedAuthors[] = authorNames.split(";");
                        for (String a : relationedAuthors) {
                            for(Integer id:authors.keySet()){
                                Author relatedAuthor = authors.get(id);
                                if(relatedAuthor.getName().equals(a)){
                                    relatedAuthor.addPaper(paper.getId(),paper);
                                    paper.addAuthor(id,relatedAuthor);
                                    writeNewRelationPaperAuthor(paper,relatedAuthor);
                                    break;
                                }
                            }
                        }
                    }
                    //temas relacionats
                    String termNames = aux.nextLine();
                    System.out.print("Te relacio amb alguna Conferencia? (0 -> no en te, indica el nom )");
                    if(!termNames.equals("0")) {
                        String relationedTerms[] = termNames.split(";");
                        for (String t : relationedTerms) {
                            for(Integer id:terms.keySet()){
                                Term relatedTerm = terms.get(id);
                                if(relatedTerm.getName().equals(t)){
                                    relatedTerm.addPaperWhichTalkAboutIt(paper.getId(),paper);
                                    paper.addTerm(id,relatedTerm);
                                    writeNewRelationPaperTerm(paper,relatedTerm);
                                    break;
                                }
                            }
                        }
                    }
                    //Conferencia relacionada
                    String confName = aux.nextLine();
                    if(!confName.equals("0")) {
                        for (Integer id : conferences.keySet()) {
                            Conference relatedConference = conferences.get(id);
                            if (relatedConference.getName().equals(confName)) {
                                relatedConference.addExposedPaper(paper.getId(),paper);
                                paper.setConference(relatedConference);
                                writeNewRelationPaperConf(paper,relatedConference);
                                break;
                            }
                        }
                    }


                    papers.put(authorMaxId,paper);
                    writePaperToFile(paper);
                    writePaperRelations(paper);
                    break;
                case("T"):
                    System.out.print("Quin nom te ");
                    objName = aux.nextLine();
                    System.out.print("Te relacio amb algun Paper? (0 -> no en te, nom dels Papers separats per ; )");
                    paperNames = aux.nextLine();
                    termMaxId = termMaxId + 1;
                    Term term = new Term(objName, termMaxId);
                    if(!paperNames.equals("0")) {
                        String relationedPapers[] = paperNames.split(";");
                        for (String p : relationedPapers) {
                            for(Integer id:papers.keySet()){
                                Paper relatedPaper = papers.get(id);
                                if(relatedPaper.getName().equals(p)){
                                    relatedPaper.addTerm(term.getId(),term);
                                    term.addPaperWhichTalkAboutIt(id,relatedPaper);
                                    writeNewRelationPaperTerm(relatedPaper,term);
                                    break;
                                }
                            }
                        }
                    }
                    terms.put(termMaxId,term);
                    writeTermToFile(term);
                    writeTermRelations(term);
                    break;
                case("C"):
                    System.out.print("Quin nom te ");
                    objName = aux.nextLine();
                    System.out.print("Te relacio amb algun Paper? (0 -> no en te, nom dels Papers separats per ; )");
                    paperNames = aux.nextLine();
                    conferenceMaxId = conferenceMaxId + 1;
                    Conference conf = new Conference(objName, conferenceMaxId);
                    if(!paperNames.equals("0")) {
                        String relationedPapers[] = paperNames.split(";");
                        Paper relatedPaper;
                        for (String p : relationedPapers) {
                            for(Integer id:papers.keySet()){
                                if(papers.get(id).getName().equals(p)){
                                    relatedPaper = new Paper(p,id);
                                    conf.addExposedPaper(id,relatedPaper);
                                    writeNewRelationPaperConf(relatedPaper, conf);
                                    break;
                                }
                            }
                        }
                    }
                    conferences.put(conferenceMaxId,conf);
                    writeConferenceToFile(conf);
                    writeConferenceRelations(conf);
                    break;
            }
                break;
            case("Ed"):
                switch(objectType){
                    case("A"):
                        break;
                    case("P"):
                        break;
                    case("T"):
                        break;
                    case("C"):
                        break;
                }
                break;
            case("El"):
                switch(objectType){
                    case("A"):
                        break;
                    case("P"):
                        break;
                    case("T"):
                        break;
                    case("C"):
                        break;
                }
                break;

        }

    }

    private void readAuthorsFromFile(HashMap<Integer, Author> authors){
        File inputFile = new File("/../data/author.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] aux = line.split(";");
                int id = Integer.parseInt(aux[0]);
                Author author = new Author(aux[1],id);
                readAuthorRelations(author);
                authors.put(id,author);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);        }
    }

    private void readPapersFromFile(HashMap<Integer, Paper> papers){
        File inputFile = new File("/../data/paper.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] aux = line.split(";");
                int id = Integer.parseInt(aux[0]);
                Paper paper = new Paper(aux[1],id);
                readPaperRelations(paper);
                papers.put(id,paper);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);        }
    }

    private void readConferencesFromFile(HashMap<Integer, Conference> conferences){
        File inputFile = new File("/../data/conf.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] aux = line.split(";");
                int id = Integer.parseInt(aux[0]);
                Conference conf = new Conference(aux[1],id);
                conf.setYear(Integer.parseInt(aux[2]));
                conf.setContinent(aux[3]);
                readConferenceRelations(conf);
                conferences.put(id,conf);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);        }
    }


    private void readTermsFromFile(HashMap<Integer, Term> terms){
        File inputFile = new File("/../datda/term.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] aux = line.split(";");
                int id = Integer.parseInt(aux[0]);
                Term term = new Term(aux[1],id);
                readTermRelations(term);
                terms.put(id,term);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);        }
    }


    private void writeAuthorToFile(Author author){
        String wrauthor = Integer.toString(author.getId()) + ";" + author.getName();
        File inputFile = new File("/../datda/author.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))){
            writer.write(wrauthor, 0, wrauthor.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    public void writePaperToFile(Paper paper){
        String wrpaper = Integer.toString(paper.getId()) + ";" + paper.getName();
        File inputFile = new File("/../data/paper.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))){
            writer.write(wrpaper, 0, wrpaper.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    public void writeConferenceToFile(Conference conference) {
        String wrconf = Integer.toString(conference.getId()) + ";" + conference.getName() +
                ";" + conference.getYear() + ";" + conference.getContinent();
        File inputFile = new File("/../data/conf.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))){
            writer.write(wrconf, 0, wrconf.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    private void writeTermToFile(Term term){
        String wrterm = Integer.toString(term.getId()) + ";" + term.getName();
        File inputFile = new File("/../data/term.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
            writer.write(wrterm, 0, wrterm.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }


    }


    private void deleteAuthorFromFile(Author author){
        File inputFile = new File("/../data/author.txt");

       try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {

           String lineToRemove = Integer.toString(author.getId()) + ";" + author.getName();
           String currentLine;

           while ((currentLine = reader.readLine()) != null) {
               if (currentLine.equals(lineToRemove))
                   writer.write("");
           }
       } catch(IOException x){
           System.err.format("IOExeption: %s%n", x);
       }
    }

    private void deletePaperFromFile(Paper paper){

        File inputFile = new File("/../data/paper.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile));) {
            String lineToRemove = Integer.toString(paper.getId()) + ";" + paper.getName();
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(lineToRemove))
                    writer.write("");
            }
        }
        catch(IOException x){
            System.err.format("IOExeption: %s%n", x);
        }
    }


    private void deleteConferenceFromFile(Conference conference){
        File inputFile = new File("/../data/conf.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {

            String lineToRemove = Integer.toString(conference.getId()) + ";" + conference.getName()
                    + ";" + conference.getYear() + ";" + conference.getContinent();
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(lineToRemove))
                    writer.write("");
            }
        }
        catch (IOException x) {
            System.err.format("IOExeption: %s%n", x);
        }
    }

    private void deleteTermFromFile(Term term){
        File inputFile = new File("/../data/term.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {

            String lineToRemove = Integer.toString(term.getId()) + ";" + term.getName();
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(lineToRemove))
                    writer.write("");
            }
        }
        catch (IOException x){
            System.err.format("IOExeption: %s%n", x);
        }
    }

    private void editAuthorFromFile(Author author, String key, String value){
        File inputFile = new File("/../data/author.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {

            String lineToRemove = Integer.toString(author.getId()) + ";" + author.getName();
            String currentLine;
            String wrauthor;

            if (key.equals("nom")) wrauthor = Integer.toString(author.getId()) + ";" + value;
            else wrauthor = value + ";" + author.getName();

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(lineToRemove))
                    writer.write(wrauthor);
            }
        }
        catch(IOException x) {
            System.err.format("IOExeption: %s%n", x);
        }
    }


    private void editPaperFromFile(Paper paper, String key, String value) {
        File inputFile = new File("/../data/paper.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {

            String lineToRemove = Integer.toString(paper.getId()) + ";" + paper.getName();
            String currentLine;
            String wrpaper;

            if (key.equals("nom")) wrpaper = Integer.toString(paper.getId()) + ";" + value;
            else wrpaper = value + ";" + paper.getName();

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(lineToRemove))
                    writer.write(wrpaper);
            }

        }
        catch(IOException x){
            System.err.format("IOExeption: %s%n", x);
        }
    }


    private void editConferenceFromFile(Conference conference, String key, String value){
        File inputFile = new File("/../data/conference.txt");

        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {

            String lineToRemove = Integer.toString(conference.getId()) + ";" + conference.getName()
                    + ";" + conference.getYear() + ";" + conference.getContinent();
            String currentLine;
            String wrconf = null;

            switch (key) {
                case "nom":
                    wrconf = Integer.toString(conference.getId()) + ";" + value
                            + ";" + conference.getYear() + ";" + conference.getContinent();
                    break;
                case "id":
                    wrconf = value + ";" + conference.getName()
                            + ";" + conference.getYear() + ";" + conference.getContinent();
                    break;
                case "any":
                    wrconf = Integer.toString(conference.getId()) + ";" + conference.getName()
                            + ";" + value + ";" + conference.getContinent();
                    break;
                case "continent":
                    wrconf = Integer.toString(conference.getId()) + ";" + conference.getName()
                            + ";" + conference.getYear() + ";" + value;
                    break;
            }

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(lineToRemove))
                    writer.write(wrconf);
            }
        }
        catch (IOException x){
            System.err.format("IOExeption: %s%n", x);
        }
    }


    private void editTermFromFile(Term term, String key, String value){
        File inputFile = new File("/../data/term.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {

            String lineToRemove = Integer.toString(term.getId()) + ";" + term.getName();
            String currentLine;
            String wrterm;

            if (key.equals("nom")) wrterm = Integer.toString(term.getId()) + ";" + value;
            else wrterm = value + ";" + term.getName();

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals(lineToRemove))
                    writer.write(wrterm);
            }
        }
        catch(IOException x){
            System.err.format("IOExeption: %s%n", x);
        }
    }

    private void readAuthorRelations(Author author){}
    private void readPaperRelations(Paper paper){}
    private void readConferenceRelations(Conference conference){}
    private void readTermRelations(Term term){}

    private void writeAuthorRelations(Author author){}
    private void writePaperRelations(Paper paper){}
    private void writeConferenceRelations(Conference conference){}
    private void writeTermRelations(Term term){}

    private void writeNewRelationPaperAuthor(Paper paper, Author author){}
    private void writeNewRelationPaperTerm(Paper paper, Term term){}
    private void writeNewRelationPaperConf(Paper paper, Conference conf){}





}
