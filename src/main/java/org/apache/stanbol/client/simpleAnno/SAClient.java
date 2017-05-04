package org.apache.stanbol.client.simpleAnno;

import com.google.common.collect.Multimap;
import org.apache.stanbol.client.Enhancer;
import org.apache.stanbol.client.StanbolClientFactory;
import org.apache.stanbol.client.enhancer.impl.EnhancerParameters;
import org.apache.stanbol.client.enhancer.model.*;
import org.apache.stanbol.client.entityhub.model.Entity;
import org.apache.stanbol.client.exception.StanbolClientException;
import org.apache.stanbol.client.services.exception.StanbolServiceException;
import org.apache.stanbol.client.simpleAnno.ClientMapper;
import org.apache.stanbol.client.simpleAnno.EnhancementParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * client
 * Created by: Yamen Jeries on 27/4/17.
 * E-mail: yamen.jeries@jkaref.com
 */

public class SAClient {

    final static String STANBOL_ENDPOINT = "http://ontology.simple-anno.de:9090/";
    final static String ENHANCEMENT_CHAIN = "glodmed-entity-tagging";
    final static String DBPEDIA_SPOTLIGHT = "dbpedia-spotlight";
    final StanbolClientFactory factory = new StanbolClientFactory(STANBOL_ENDPOINT);
    final Enhancer client = factory.createEnhancerClient();


    public Collection<TextAnnotation> getTextAnnotations(String text) throws StanbolServiceException, StanbolClientException {

        EnhancerParameters.EnhancerParametersBuilder parameterBuilder = new EnhancerParameters.EnhancerParametersBuilder();
        EnhancerParameters parameters = parameterBuilder.setChain(ENHANCEMENT_CHAIN).setContent(text).build();
        EnhancementStructure eRes = client.enhance(parameters);
        return eRes.getTextAnnotations();

    }

    public Multimap<TextAnnotation, EntityAnnotation> getBestAnnotations(String text) throws StanbolServiceException, StanbolClientException {

        EnhancerParameters.EnhancerParametersBuilder parameterBuilder = new EnhancerParameters.EnhancerParametersBuilder();
        EnhancerParameters parameters = parameterBuilder.setChain(ENHANCEMENT_CHAIN).setContent(text).build();
        EnhancementStructure eRes = client.enhance(parameters);
        return eRes.getBestAnnotations();

    }

    public Collection<Entity> getEntities(String text) throws StanbolServiceException, StanbolClientException {

        EnhancerParameters.EnhancerParametersBuilder parameterBuilder = new EnhancerParameters.EnhancerParametersBuilder();
        EnhancerParameters parameters = parameterBuilder.setChain(ENHANCEMENT_CHAIN).setContent(text).build();
        EnhancementStructure eRes = client.enhance(parameters);
        return eRes.getEntities();

    }


    public InputStream getInputStream(String file) {

        InputStream is = null;

        try {
            is = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return is;
    }

    public Map<String, Integer> enhanceText(String text) throws Exception {

        Multimap<TextAnnotation, EntityAnnotation> bestAnnotations =  getBestAnnotations(text);
        EnhancementParser parser = new EnhancementParser();
        ClientMapper mapper = parser.parseAnnotations(bestAnnotations);


        HashMap<String, Integer> labels = mapper.getLabels();
        Map<String, Integer> sortedMap = sortByValue(labels);
        return sortedMap;
    }



    /**
     * Enhancements of Files from GLODmed
     */
    public Map<String, Integer> enhanceFile(String file) throws Exception {

        EnhancerParameters.EnhancerParametersBuilder parameterBuilder = new EnhancerParameters.EnhancerParametersBuilder();
        InputStream is = getInputStream(file);
        EnhancerParameters parameters = parameterBuilder.setChain(ENHANCEMENT_CHAIN).setContent(is).build();
        EnhancementStructure eRes = client.enhance(parameters);
        Collection<Enhancement> enhancements = eRes.getEnhancements();
        EnhancementParser parser = new EnhancementParser();
        ClientMapper mapper = parser.parseEnhancement(enhancements);
        HashMap<String, Integer> labels = mapper.getLabels();
        Map<String, Integer> sortedMap = sortByValue(labels);
        return sortedMap;
    }

    /**
     * efficiently sorting HashMaps - Only with Java 8
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }



    /**
     * For output and evaluating purposes
     *
     */


    public void printBestEntities(Multimap<TextAnnotation, EntityAnnotation> annotations) {
        for (TextAnnotation ta : annotations.keys()) {
            System.out.println("********************************************");

            System.out.println("Selection Context: " + ta.getSelectionContext());
            System.out.println("Selected Text: " + ta.getSelectedText());
            System.out.println("Engine: " + ta.getCreator());
            System.out.println("Candidates: ");
            for (EntityAnnotation ea : annotations.get(ta))
                System.out.println("\t" + ea.getEntityLabel() + " - " + ea.getEntityReference());
        }
    }

    public void printEntityCollection(Collection<Entity> c) {
        for (Entity en : c) {
            System.out.println("********************************************");
            System.out.println("Resource : " + en.getResource());
            System.out.println("Uri : " + en.getUri());
            System.out.println("Referenced Site : " + en.getReferencedSite());
            System.out.println("Categories (for Ontologies) : " + en.getCategories());
        }
    }

    public void printTextAnnotations(Collection<TextAnnotation> c) {
        for (Annotation en : c) {
            System.out.println("********************************************");
            System.out.println("Annotation : " + en );
//            System.out.println("Uri : " + en.getUri());
//            System.out.println("Selected Text : " + en.getExtractedFrom());
//            System.out.println("Confidence : " + en.getConfidence());
        }
    }
}

