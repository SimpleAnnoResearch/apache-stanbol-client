package org.apache.stanbol.client.simpleAnno;

/**
 * client
 * Created by: Yamen Jeries on 5/4/17.
 * E-mail: yamen.jeries@jkaref.com
 */


import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Resource;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.hp.hpl.jena.rdf.model.Literal;
import org.apache.stanbol.client.Enhancer;
import org.apache.stanbol.client.Sparql;
import org.apache.stanbol.client.StanbolClientFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.rdf.model.RDFNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SparqlClient {

    final static String STANBOL_ENDPOINT = "http://ontology.simple-anno.de:9090/sparql";

    public static ResultSet tripleQuery(String subject, String predicate, String object){
        ResultSet results = null;
        String query =  "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "PREFIX dbp: <http://dbpedia.org/resource/>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"+
                "select * where {?"+subject+ " ?"+predicate+" ?"+object+"} LIMIT 100";
        System.out.println("Query -> \n" + query);

        try {
            results = SPARQLUtil.INSTANCE.stanbolQuery(query);
            ResultSetFormatter.out(System.out, results);
            while (results.hasNext()) {

                QuerySolution soln = results.next();

                Resource Subject = soln.getResource(subject);
                Resource Predicate = soln.getResource(predicate);
                Resource Object = soln.getResource(object);

                System.out.println(Subject + " |----| " + Predicate + " |----| " + Object);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }


    public static ResultSet generalParameterizedTripleQuery(String subject, String predicate, String object){
        ResultSet results = null;
        ParameterizedSparqlString qs = new ParameterizedSparqlString("PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "PREFIX dbp: <http://dbpedia.org/resource/>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"+
                "select * where {" +
                "?subject ?predicate ?object" +
                "} LIMIT 100");

        qs.setIri("subject", subject);
        qs.setIri("predicate", predicate);
        qs.setLiteral("object", object);

        System.out.println("Query -> \n" + qs.asQuery());

        try {
            QueryExecution exec = QueryExecutionFactory.sparqlService(STANBOL_ENDPOINT, qs.asQuery());
            results = ResultSetFactory.copyResults(exec.execSelect());


            while (results.hasNext()) {

                QuerySolution soln = results.next();

                Literal Subject = soln.getLiteral("subject");
                Literal Predicate = soln.getLiteral("predicate");
                Literal Object = soln.getLiteral("object");

                System.out.println(Subject + " |----| " + Predicate + " |----| " + Object);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }


    /**
     * Convert ResultSet to map
     * @param rs
     * @return
     */
    public Map<String, Integer> getResultAsMap(ResultSet rs) {
        Map<String, Integer> myMap = new HashMap<String, Integer>();

        for (; rs.hasNext();) {
            QuerySolution soln = rs.nextSolution();

            String stream = soln.get("stream").toString();
            String noOfStudentsStr = soln.get("numberOfStudents").toString();
            int noOfStudents = Integer.parseInt(noOfStudentsStr);
            myMap.put(stream, noOfStudents);
        }

        return myMap;
    }

    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);
        SparqlClient exec = new SparqlClient();


//        exec.tripleQuery("a", "b", "c");
        exec.generalParameterizedTripleQuery("a", "b", "c");


    }



}
