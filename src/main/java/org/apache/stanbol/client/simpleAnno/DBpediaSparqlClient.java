package org.apache.stanbol.client.simpleAnno;

/**
 * client
 * Created by: Yamen Jeries on 5/8/17.
 * E-mail: yamen.jeries@jkaref.com
 */

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Literal;

import java.util.ArrayList;


public class DBpediaSparqlClient {

    final static String DBPEDIA_ENDPOINT = "https://dbpedia.org/sparql";


    public static ArrayList<String> getResolvedEntity(String label) {

        /**
         * Example for using patameterized queries (need dbpedia endpoint
         */
        ParameterizedSparqlString qs = new ParameterizedSparqlString(""
                + "PREFIX dcterms: <http://purl.org/dc/terms/>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT ?categoryUri ?categoryName\n"
                + "WHERE {\n"
                + "  <http://dbpedia.org/resource/" + label + "> dcterms:subject ?categoryUri.\n"
                + "  ?categoryUri rdfs:label ?categoryName.\n"
                + "  FILTER (lang(?categoryName) = \"en\")\n"
                + "}");

        //System.out.println(qs);
        QueryExecution exec = QueryExecutionFactory.sparqlService(DBPEDIA_ENDPOINT, qs.asQuery());
        ResultSet results = ResultSetFactory.copyResults(exec.execSelect());
        ArrayList<String> KeywordsList = new ArrayList<>();
        try {
            while (results.hasNext()) {

                //System.out.println(results.next().get("categoryUri"));
                RDFNode cat = results.next().get("categoryUri");
                RDFNode name_en = results.next().get("categoryName");

                //System.out.println("Cat: " + cat);
                //System.out.println("Name: " + name_en.toString().replace("@en", ""));
                String name = name_en.toString().replace("@en", "");
                System.out.println("Category    : " + name);

                KeywordsList.add(name);

                //secondQuery(uri);
                ResultSet res = extractSubCategories(cat);
                try {
                    while (res.hasNext()) {
                        //System.out.println("Subcategory = " + res.next().get("label"));
                        String label_en = res.next().get("label").toString();
                        String name_label = label_en.replace("@en", "");
                        System.out.println("Sub-Category: " + name_label);

                        KeywordsList.add(name_label);
                        //System.out.println(results.next().get("label"));

                    }
                } catch (Exception e) {
                }
                System.out.println("=========================================== ");

            }

        } catch (Exception e) {
        }
        //System.out.println("Keywords: " + KeywordsList);

        //ResultSetFormatter.out(results);
        return KeywordsList;
    }

    public static void printResultSet(ResultSet result) {
        try {
            while (result.hasNext()) {
                System.out.println("Result -> " + result.next());
//                System.out.println("Subcategory = " + res.next().get("label"));
//                String label_en = result.next().get("label").toString();
//                String name_label = label_en.replace("@en", "");
//                System.out.println("Label Name : " + name_label);
            }
        } catch (Exception e) {
            System.out.println("Error while printing ResultSet!");
        }
        System.out.println("=========================================== ");
    }

    public static ResultSet extractSubCategories(RDFNode node) {

        String qs = "prefix category:<http://dbpedia.org/resource/Category:>\n"
                + "prefix skos:<http://www.w3.org/2004/02/skos/core#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "select ?value ?label\n"
                + "where {\n"
                + "<" + node + "> skos:broader ?value.\n"
                + "  ?value rdfs:label ?label.\n"
                + "  FILTER (lang(?label) = \"en\")\n"
                + "}";

        //System.out.println(qs);
        ResultSet results = SPARQLUtil.INSTANCE.dbpediaQuery(qs);

        //ResultSetFormatter.out(results);
        return results;
    }


    public static ResultSet queryStanbol() {

        ResultSet results = null;
        String query =  "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                        "PREFIX dbp: <http://dbpedia.org/resource/>\n" +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"+
                        "select * where {?Subject ?Predicate ?Object} LIMIT 100";
        System.out.println("Query -> \n" + query);

        try {
            results = SPARQLUtil.INSTANCE.stanbolQuery(query);
            ResultSetFormatter.out(System.out, results);
            while (results.hasNext()) {

                QuerySolution soln = results.next();

                Resource Subject = soln.getResource("Subject");
                Resource Predicate = soln.getResource("Predicate");
                Resource Object = soln.getResource("Object");

                System.out.println(Subject + " |----| " + Predicate + " |----| " + Object);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }


    public static void exampleQuery(){
        String query1 = " 	PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + " PREFIX : <http://dbpedia.org/resource/>"
                + " PREFIX d: <http://dbpedia.org/ontology/> "
                + " SELECT distinct ?albumName ?artistName "
                + " WHERE "
                + " { "
                + " ?album d:producer :Timbaland . "
                + " ?album d:musicalArtist ?artist ."
                + " ?album rdfs:label ?albumName . "
                + " ?artist rdfs:label ?artistName ."
                + " FILTER ( lang(?artistName) = \"en\")"
                + " FILTER ( lang(?albumName) = \"en\" )" + " }";

        ResultSet results = SPARQLUtil.INSTANCE.dbpediaQuery(query1);
        ResultSetFormatter.out(System.out, results);

        while (results.hasNext()) {

            QuerySolution soln = results.next();

            Literal albumName = soln.getLiteral("albumName");
            Literal artistName = soln.getLiteral("artistName");

            System.out.println(albumName+"--"+artistName);

        }

    }

    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);
        ArrayList<String> list = getResolvedEntity("Barack_Obama");

//        ArrayList<String> list = getResolvedEntity("Sheffield_Wednesday_F.C.");
        System.out.println(list);

    }

}
