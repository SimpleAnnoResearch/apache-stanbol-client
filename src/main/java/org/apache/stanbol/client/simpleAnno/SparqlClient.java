package org.apache.stanbol.client.simpleAnno;

/**
 * client
 * Created by: Yamen Jeries on 5/4/17.
 * E-mail: yamen.jeries@jkaref.com
 */


import org.apache.stanbol.client.Enhancer;
import org.apache.stanbol.client.Sparql;
import org.apache.stanbol.client.StanbolClientFactory;


public class SparqlClient {

    final static String STANBOL_ENDPOINT = "http://ontology.simple-anno.de:9090/";
    final static String ENHANCEMENT_CHAIN = "glodmed-entity-tagging";
    final static String DBPEDIA_SPOTLIGHT = "dbpedia-spotlight";
    final StanbolClientFactory factory = new StanbolClientFactory(STANBOL_ENDPOINT);
    final Enhancer client = factory.createEnhancerClient();


    public static void main(String[] args) {
        System.out.println(" ");
    }

    public void testSparql() throws Exception {



    }
}
