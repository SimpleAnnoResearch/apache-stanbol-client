package org.apache.stanbol.client.simpleAnno;

import com.google.common.collect.Multimap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.stanbol.client.enhancer.model.Enhancement;
import org.apache.stanbol.client.enhancer.model.EntityAnnotation;
import org.apache.stanbol.client.enhancer.model.TextAnnotation;
import org.apache.stanbol.client.entityhub.model.Entity;
import org.apache.stanbol.client.simpleAnno.PDFTextParser;
import org.apache.stanbol.client.simpleAnno.SAClient;

import java.util.Collection;
import java.util.Map;

/**
 * Created by yamenj on 24.04.17.
 */
public class Main {



    public static void main(String[] args) throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        String text = "Implant prosthodontics offers multiple clinical options. There is one crucial question that has not\n" +
                "been completely solved: is screw-retention or cementation of implant-supported restorations su-\n" +
                "perior? This question may be answered differently depending on the background and experience\n" +
                "of the practitioner, and there are good arguments for both concepts. The biggest advantage of the\n" +
                "screw-retained implant is minimal-effort retrievability. Furthermore, whenever the emergence profile\n" +
                "needs shaping in the anterior region, screw-retained crowns offer benefits over cemented crowns.\n" +
                "The introduction of the abutment crowns (abutment and crown in one piece) fabricated from lithium\n" +
                "disilicate has significantly simplified the manufacturing of screw-retained crowns in comparison to\n" +
                "cemented crowns, at no loss of esthetic quality. Though long-term clinical results are still lacking, the\n" +
                "authors of this article prefer screw-retained restoration. A case-report highlights the procedure of an\n" +
                "anterior abutment-crown step by step.";


        String file = "/home/yamenj/Development/Simple-Anno/Fallberichte/impl_2015_03_s0305.pdf";
        String file2 = "/home/yamenj/Development/Simple-Anno/Fallberichte/test.txt";

        // getEntities
        SAClient client = new SAClient();
        Collection<Entity> entities = client.getEntities(text);
        client.printEntityCollection(entities);

        // getTextAnnotations
//        SAClient client = new SAClient();
        Collection<TextAnnotation> annotations = client.getTextAnnotations(text);
        client.printTextAnnotations(annotations);


        enhanceText(text);
//        enhanceTXTFile(file);
//        enhanceFile(file);

    }

    public static void enhanceText(String text) throws Exception {
        SAClient client = new SAClient();
        Map<String, Integer> map =  client.enhanceText(text);
        System.out.println("MAP -> " + map);

    }

    /**
     * For now converting the pdf to txt as a temporarily solution
     * @param file
     * @throws Exception
     */
    public static void enhanceFile(String file) throws Exception {
        PDFTextParser pdfTextParserObj = new PDFTextParser();
        String pdfToText = pdfTextParserObj.pdftoText(file);
        pdfTextParserObj.writeTexttoFile(pdfToText, file+"new"+".txt");

        SAClient client = new SAClient();
        Map<String, Integer> map =  client.enhanceFile(file+".txt");
        System.out.println("MAP -> " + map);

    }

    public static void enhanceTXTFile(String file) throws Exception {

        SAClient client = new SAClient();
        Map<String, Integer> map =  client.enhanceFile(file);
        System.out.println("MAP -> " + map);

    }

    /**
     * TODO: get Language from Mapper
     */

}
