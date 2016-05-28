package main.java.ownClasses.domain.domainControllers;

import main.java.ownClasses.domain.domainControllers.Persistance.BinaryAuthors;
import main.java.ownClasses.domain.domainControllers.Persistance.BinaryConferences;
import main.java.ownClasses.domain.domainControllers.Persistance.BinaryPapers;
import main.java.ownClasses.domain.domainControllers.Persistance.BinaryTerms;
import main.java.ownClasses.domain.queries.IntervaledQuery;
import main.java.ownClasses.domain.queries.LimitedQuery;
import main.java.ownClasses.domain.queries.OrderedQuery;
import main.java.ownClasses.domain.queries.Query;
import main.java.sharedClasses.domain.domainControllers.DomainPersistanceController;
import main.java.sharedClasses.domain.nodes.Author;
import main.java.sharedClasses.domain.nodes.Conference;
import main.java.sharedClasses.domain.nodes.Paper;
import main.java.sharedClasses.domain.nodes.Term;
import main.java.sharedClasses.utils.Matrix;
import main.java.sharedClasses.utils.Pair;

import java.util.*;

public class DomainMainController {
    private HashMap<Integer, Author> authorsById;
    private HashMap<Integer, Paper> papersById;
    private HashMap<Integer, Conference> conferencesById;
    private HashMap<Integer, Term> termsById;
    private HashMap<String, Author> authorsByName;
    private HashMap<String, Paper> papersByName;
    private HashMap<String, Conference> conferencesByName;
    private HashMap<String, Term> termsByName;
    private DomainPersistanceController persistanceController;
    private DomainHetesimController hetesimController;
    private Scanner scanner;


    private Matrix result;
    private boolean edit;

    private Matrix authorpaper;
    private Matrix confpaper;
    private Matrix termpaper;

    public DomainMainController() {
        authorsById = new HashMap<>();
        papersById = new HashMap<>();
        conferencesById = new HashMap<>();
        termsById = new HashMap<>();
        authorsByName = new HashMap<>();
        papersByName = new HashMap<>();
        papersByName = new HashMap<>();
        conferencesByName = new HashMap<>();
        termsByName = new HashMap<>();
        persistanceController = new DomainPersistanceController(authorsById, papersById, conferencesById, termsById, authorsByName, papersByName, conferencesByName, termsByName);
        persistanceController.readAllFromFile("");
        BinaryAuthors binaryAuthors = new BinaryAuthors();
        BinaryPapers binaryPapers = new BinaryPapers();
        BinaryConferences binaryConferences = new BinaryConferences();
        BinaryTerms binaryTerms = new BinaryTerms();
        System.out.println("Exporta");
        long timeini = System.currentTimeMillis();
        //persistanceController.binaryexport();
        binaryAuthors.write(authorsById);
        binaryPapers.write(papersById);
        binaryTerms.write(termsById);
        binaryConferences.write(conferencesById);
        long timefinal = System.currentTimeMillis();
        System.out.println(timefinal-timeini);

        System.out.println("Importa");

        timeini = System.currentTimeMillis();
        //persistanceController.binaryimport();
        authorsById = (HashMap<Integer,Author>) binaryAuthors.read();
        papersById = (HashMap<Integer,Paper>) binaryPapers.read();
        conferencesById = (HashMap<Integer,Conference>) binaryConferences.read();
        termsById = (HashMap<Integer,Term>) binaryTerms.read();
        timefinal = System.currentTimeMillis();
        System.out.println(timefinal-timeini);
//persistanceController.testDomain();
        authorpaper = getAuthorPaperMatrix(null,null);
        confpaper =getConferencePaperMatrix(null,null);
        termpaper = getTermPaperMatrix(null,null);
        hetesimController = new DomainHetesimController(authorpaper,authorpaper.transpose(),termpaper,termpaper.transpose(),confpaper,confpaper.transpose());
        scanner = new Scanner(System.in);
        edit=false;

    }

    public DomainPersistanceController getPersistanceController() {
        return persistanceController;
    }

    public void NQ(String path){
        if(edit) {
            updateMatrix(null,null,null,null);

        }
        result = hetesimController.heteSim(path);

    }

    public boolean checkName(String name, char node){
        boolean check = false;
        switch (node) {
            case ('A'):
                check = authorsByName.containsKey(name);
                break;
            case ('P'):
                check = papersByName.containsKey(name);
                break;
            case ('C'):
                check = conferencesByName.containsKey(name);
                break;
            case ('T'):
                check = termsByName.containsKey(name);
                break;
        }
        return check;
    }

    public ArrayList<String> resultat(String path, int querytype,boolean ascendent,String name,int n,double max,double min){
        OrderedQuery query = new OrderedQuery(path,ascendent);

        ArrayList<String> total = new ArrayList<>();

        Integer queryId = 0;
        char type = path.charAt(0);

        switch (type) {
            case ('A'):
                    queryId = authorsByName.get(name).getId();
                break;
            case ('P'):
                    queryId = papersByName.get(name).getId();
                break;
            case ('C'):
                    queryId = conferencesByName.get(name).getId();
                break;
            case ('T'):
                    queryId = termsByName.get(name).getId();
                break;
        }

        HashMap<Integer, Double> resultquery = new HashMap<>();
        if (result.columns(queryId) != null) resultquery = result.columns(queryId);


        ArrayList<Pair<Integer,Double>> resultOrdered = resultWithOrder(resultquery, query);

        switch (querytype){
            case (1):
                total = resultWithoutFilters(resultOrdered,query);
                break;
            case (2):
                LimitedQuery lq = new LimitedQuery(query.getPath(), n);
                total = resultWithMax(resultOrdered, lq);
                break;
            case(3):
                IntervaledQuery iq = new IntervaledQuery(query.getPath(), min, max);
                total = resultWithIntervals(resultOrdered, iq);
                break;
        }


        return total;
    }

    public void newQuery() {

        hetesimController = new DomainHetesimController(authorpaper,authorpaper.transpose(),termpaper,termpaper.transpose(),confpaper,confpaper.transpose());
        System.out.println("Quin tipus de cerca vols: -1 Simple o -2 Amb filtres");
        int queryType = scanner.nextInt();
        while(queryType != -1 && queryType != -2){
            System.err.println("Tipus no valid, escriu -1 Simple o -2 Amb filtres");
            queryType = scanner.nextInt();
        }

        boolean pathValid = false;
        System.out.println("Introdueix el teu path (exemple: APA):");
        String queryPath = "";
        while ("".equals(queryPath)) queryPath = scanner.nextLine();
        while(!pathValid) {
            boolean surt = false;
            for(int i=0; i<queryPath.length() && !surt; ++i){
                char node= queryPath.charAt(i);
                if(node != 'A' && node != 'P' && node!='C' && node!='T') surt = true;
            }
            if(!surt){ pathValid = true;}
            else{
                System.err.println("Path no valid, torna'l a escriure (exemple APA):");
                //while ("".equals(queryPath)) queryPath = scanner.nextLine();
                queryPath = scanner.nextLine();
            }
        }

        OrderedQuery query = new OrderedQuery(queryPath,false);
        Matrix result = hetesimController.heteSim(queryPath);

        boolean exit = false;
        char type = queryPath.charAt(0);

        while(!exit) {
            System.out.println("Selecciona l'ordre 1 Ascendent 2 Descendent");
            String r = scanner.nextLine();
            int i = Integer.parseInt(r);
            if (i > 2 || i <= 0) {
                System.out.println("Ordre no disponible");
                break;
            }
            else {
                if (i == 1)
                    query.setAscendent(true);
            }

            System.out.println("OK");
            boolean valid = false;
            Integer queryId = 0;

            while (!valid) {
                System.out.println("Introdueix el nom:");
                String queryName = scanner.nextLine();
                switch (type) {
                    case ('A'):
                        if (authorsByName.containsKey(queryName)) {
                            System.out.println("OK");
                            valid = true;
                            queryId = authorsByName.get(queryName).getId();
                        }
                        break;
                    case ('P'):
                        if (papersByName.containsKey(queryName)) {
                            System.out.println("OK");
                            valid = true;
                            queryId = papersByName.get(queryName).getId();
                        }
                        break;
                    case ('C'):
                        if (conferencesByName.containsKey(queryName)) {
                            System.out.println("OK");
                            valid = true;
                            queryId = conferencesByName.get(queryName).getId();
                        }
                        break;
                    case ('T'):
                        if (termsByName.containsKey(queryName)) {
                            System.out.println("OK");
                            valid = true;
                            queryId = termsByName.get(queryName).getId();
                        }
                        break;
                }
                if (!valid) {
                    System.err.println("Nom no vàlid. Siusplau introdueix un nom correcte.");
                }
            }

            HashMap<Integer, Double> resultquery = new HashMap<>();
            if (result.columns(queryId) != null) resultquery = result.columns(queryId);


            ArrayList<Pair<Integer,Double>> resultOrdered = resultWithOrder(resultquery, query);


            if (queryType == -1) {
                resultWithoutFilters(resultOrdered, query);
            } else {
                int exitfiltres = 0;
                while (exitfiltres == 0) {
                    System.out.println("Escull el tipus de filtre: -1 Intervals de rellevancia, -2 Nombre maxim d'entrades, -3 Restriccio per element:");
                    int queryfilter = scanner.nextInt();
                    switch (queryfilter) {
                        case (-1):
                            System.out.println("Introdueix el valor mes petit (entre 0 i 1)");
                            double firstrelevance = scanner.nextDouble();
                            System.out.println("Introdueix el valor mes gran (entre 0 i 1)");
                            double secondrelevance = scanner.nextDouble();
                            if (firstrelevance > secondrelevance) {
                                System.err.println("firstrelevance > secondrelevance)");
                            } else {
                                IntervaledQuery iq = new IntervaledQuery(query.getPath(), firstrelevance, secondrelevance);
                                resultWithIntervals(resultOrdered, iq);
                            }

                            break;
                        case (-2):

                            System.out.println("Introdueix el nombre maxim d'entrades");
                            int nomMax = scanner.nextInt();
                            LimitedQuery lq = new LimitedQuery(query.getPath(), nomMax);
                            resultWithMax(resultOrdered, lq);

                            break;
                        case (-3):
                            System.out.println("no disponible");
                            break;
                    }
                    System.out.println("Vols escollir un altre tipus de filtre? YES or NO");
                    String answer = "";
                    boolean validesa = false;
                    while(!validesa){
                        answer = scanner.nextLine();
                        if (answer.equals("NO") || answer.equals("no")){
                            exitfiltres = 1;
                            validesa = true;
                        }
                        else{
                            if(answer.equals("YES") || answer.equals("yes")) validesa = true;
                            else System.out.println("torna a introduir la resposta");
                        }
                    }
                }

            }
            System.out.println("Vols seleccionar un altre nom amb el mateix path? YES or NO");
            String answer = scanner.nextLine();
            if (answer.equals("NO") || answer.equals("no")) exit = true;
        }

    }

    private String GetString(char tipus, Integer id, Double relevance){
        if (relevance > 1.0) relevance = 1.0;
        else if (relevance < 0.0) relevance = 0.0;
        String row = new String();
        switch (tipus) {
            case ('A'):
                row = (authorsById.get(id).getName() + "  ->  " + relevance);
                break;
            case ('P'):
                row = (papersById.get(id).getName() + "  ->  " + relevance);
                break;
            case ('C'):
                row = (conferencesById.get(id).getName() + "  ->  " + relevance);
                break;
            case ('T'):
                row = (termsById.get(id).getName() + "  ->  " + relevance);
                break;
        }
        return row;

    }

    private void printresult(char tipus, Integer id, Double relevance){
        if (relevance > 1.0) relevance = 1.0;
        else if (relevance < 0.0) relevance = 0.0;
        switch (tipus) {
            case ('A'):
                System.out.println(authorsById.get(id).getName() + "  ->  " + relevance);
                break;
            case ('P'):
                System.out.println(papersById.get(id).getName() + "  ->  " + relevance);
                break;
            case ('C'):
                System.out.println(conferencesById.get(id).getName() + "  ->  " + relevance);
                break;
            case ('T'):
                System.out.println(termsById.get(id).getName() + "  ->  " + relevance);
                break;
        }
    }

    private ArrayList<Pair<Integer,Double>> resultWithOrder(HashMap<Integer, Double> resultquery, OrderedQuery query){
        /*char tipus = query.getPath().charAt(query.getPath().length()-1);
        System.out.println(" NOM  ->  rellevancia");*/

        Iterator<Map.Entry<Integer, Double>> it= resultquery.entrySet().iterator();
        ArrayList<Pair<Integer,Double>> resultOrdered = new ArrayList<>();
        while(it.hasNext()) {
            Map.Entry<Integer, Double> resultat = it.next();
            int id = Integer.parseInt(resultat.getKey().toString());
            double relevance = Double.parseDouble(resultat.getValue().toString());
            if(resultOrdered.isEmpty()){
                resultOrdered.add(new Pair<Integer, Double>(id,relevance));
            }
            else{
                boolean end=false;
                if(query.isAscendent()) {
                    for (int i = 0; i < resultOrdered.size() && !end; ++i) {
                        if(resultOrdered.get(i).getSecond()>= relevance){
                            resultOrdered.add(i,new Pair<Integer, Double>(id,relevance));
                            end = true;
                        }
                    }
                }
                else{
                    for(int i = 0; i < resultOrdered.size() && !end; ++i) {
                        if(resultOrdered.get(i).getSecond()<= relevance){
                            resultOrdered.add(i,new Pair<Integer, Double>(id,relevance));
                            end = true;
                        }
                    }
                }
                if(!end) resultOrdered.add(new Pair<Integer, Double>(id,relevance));
            }
        }

        /*for (Pair<Integer, Double> aResultOrdered : resultOrdered) {
            printresult(tipus, aResultOrdered.getFirst(), aResultOrdered.getSecond());
        }*/

        return resultOrdered;
    }

    private ArrayList<String> resultWithMax(ArrayList<Pair<Integer,Double>> resultquery, LimitedQuery query) {
        char tipus = query.getPath().charAt(query.getPath().length()-1);
        //System.out.println(" NOM  ->  rellevancia");
        ArrayList<String> total = new ArrayList<>();
        for(Pair<Integer,Double> p : resultquery){
            if(query.getLimit() >0){
                total.add(GetString(tipus,p.getFirst(),p.getSecond()));
                printresult(tipus,p.getFirst(),p.getSecond());
            }
            else{
                break;
            }
            query.setLimit(query.getLimit()-1);
        }

        return total;
    }

    private ArrayList<String> resultWithIntervals(ArrayList<Pair<Integer,Double>> resultquery, IntervaledQuery query){
        char tipus = query.getPath().charAt(query.getPath().length()-1);
        //System.out.println(" NOM  ->  rellevancia");

        ArrayList<String> total = new ArrayList<>();
        for(Pair<Integer,Double> p : resultquery){
            if(p.getSecond() >= query.getFirstRelevance() && p.getSecond() <= query.getSecondRelevance()) {
                total.add(GetString(tipus,p.getFirst(),p.getSecond()));
                printresult(tipus,p.getFirst(),p.getSecond());
            }
        }

        return total;
        /*for (Object o : resultquery.entrySet()) {
            Map.Entry resultat = (Map.Entry) o;
            double res = Double.parseDouble(resultat.getValue().toString());
            if (res >= query.getFirstRelevance() && res <= query.getSecondRelevance()) {

                printresult(tipus, Integer.parseInt(resultat.getKey().toString()), Double.parseDouble(resultat.getValue().toString()));

            }
        }*/

    }

    private ArrayList<String> resultWithoutFilters(ArrayList<Pair<Integer,Double>> resultquery, Query query){
        char tipus = query.getPath().charAt(query.getPath().length()-1);
        //System.out.println(" NOM  ->  rellevancia");

        ArrayList<String> total = new ArrayList<>();
        for(Pair<Integer,Double> p : resultquery){
            String row = GetString(tipus,p.getFirst(),p.getSecond());
            total.add(row);
            printresult(tipus,p.getFirst(),p.getSecond());
        }

        return total;
        /*for (Object o : resultquery.entrySet()) {
            Map.Entry resultat = (Map.Entry) o;
            printresult(tipus, Integer.parseInt(resultat.getKey().toString()), Double.parseDouble(resultat.getValue().toString()));

        }*/

    }

    public void editGraph() {
        DomainPersistanceController domainPersistanceController = new DomainPersistanceController(authorsById, papersById, conferencesById, termsById, authorsByName, papersByName, conferencesByName, termsByName);
        domainPersistanceController.newEdit();
        edit = true;
    }



    private Matrix getAuthorPaperMatrix(String authorname, String papername){
        Matrix authorpaper = new Matrix();

        if(authorname == null && papername ==null) {
            for (Author author : authorsById.values()) {
                HashMap<Integer, Paper> papersOfAuthor = author.getPapersById(papersById);
                for (Paper paper : papersOfAuthor.values()) {
                    authorpaper.addValue(author.getId(), paper.getId(), 1.0);
                }
            }
        }
        if(authorname != null && papername ==null) {
            Author author = authorsByName.get(authorname);
            HashMap<Integer, Paper> papersOfAuthor = author.getPapersById(papersById);
            for (Paper paper : papersOfAuthor.values()) {
                authorpaper.addValue(author.getId(), paper.getId(), 1.0);
            }
        }
        if(authorname == null && papername !=null) {
            Paper paper = papersByName.get(papername);
            HashMap<Integer,Author> authorsOfPaper = paper.getAuthorsById(authorsById);
            for(Author author : authorsOfPaper.values()){
                authorpaper.addValue(author.getId(),paper.getId(),1.0);
            }
        }
        if(authorname != null && papername !=null) {
            Paper paper = papersByName.get(papername);
            Author author = authorsByName.get(authorname);
            authorpaper.addValue(author.getId(),paper.getId(),1.0);

        }

        return authorpaper;
    }

    private Matrix getTermPaperMatrix(String termname, String papername){
        Matrix termpaper = new Matrix();

        if(termname == null && papername ==null) {
            for(Term term : termsById.values()){
                HashMap<Integer,Paper> papersOfTerm = term.getPapersWhichTalkAboutThisById(papersById);
                for(Paper paper : papersOfTerm.values()){
                    termpaper.addValue(term.getId(),paper.getId(),1.0);
                }
            }
        }
        if(termname != null && papername ==null) {
            Term term = termsByName.get(termname);
            HashMap<Integer, Paper> papersOfTerm = term.getPapersWhichTalkAboutThisById(papersById);
            for (Paper paper : papersOfTerm.values()) {
                authorpaper.addValue(term.getId(), paper.getId(), 1.0);
            }
        }
        if(termname == null && papername !=null) {
            Paper paper = papersByName.get(papername);
            HashMap<Integer, Term> termsOfPaper = paper.getTermsById(termsById);
            for (Term term : termsOfPaper.values()) {
                termpaper.addValue(term.getId(),paper.getId(), 1.0);
            }
        }
        if(termname != null && papername !=null) {
            Paper paper = papersByName.get(papername);
            Term term = termsByName.get(termname);
            authorpaper.addValue(term.getId(),paper.getId(),1.0);
        }

        return termpaper;
    }

    private Matrix getConferencePaperMatrix(String confname, String papername){
        Matrix conferencepaper = new Matrix();

        if(confname == null && papername ==null) {
            for(Conference conf : conferencesById.values()){
                HashMap<Integer,Paper> papersOfConf = conf.getExposedPapersById(papersById);
                for(Paper paper : papersOfConf.values()){
                    conferencepaper.addValue(conf.getId(),paper.getId(),1.0);
                }
            }
        }
        if(confname != null && papername ==null) {
            Conference conf = conferencesByName.get(confname);
            HashMap<Integer, Paper> papersOfConf = conf.getExposedPapersById(papersById);
            for (Paper paper : papersOfConf.values()) {
                conferencepaper.addValue(conf.getId(), paper.getId(), 1.0);
            }
        }
        if(confname == null && papername !=null) {
            Paper paper = papersByName.get(papername);
            conferencepaper.addValue(paper.getConference().getId(),paper.getId(), 1.0);
        }
        if(confname != null && papername !=null) {
            Paper paper = papersByName.get(papername);
            Conference conference = conferencesByName.get(confname);
            if(conference.getId()==paper.getConference().getId())
                conferencepaper.addValue(conference.getId(),paper.getId(),1.0);
        }

        return conferencepaper;
    }

    public void updateMatrix(String authorname, String papername, String confname, String termname){
        authorpaper = getAuthorPaperMatrix(authorname,papername);
        confpaper = getConferencePaperMatrix(confname,papername);
        termpaper = getTermPaperMatrix(termname,papername);
        hetesimController = new DomainHetesimController(authorpaper,authorpaper.transpose(),termpaper,termpaper.transpose(),confpaper,confpaper.transpose());
        edit = false;
    }

}

