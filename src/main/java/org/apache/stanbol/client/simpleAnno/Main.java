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
import org.pdfbox.cos.COSDocument;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
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


        // getEntities
//        SAClient client = new SAClient();
//        Collection<Entity> entities = client.getEntities(text);
//        client.printEntityCollection(entities);

        // getTextAnnotations
//        SAClient client = new SAClient();
//        Collection<TextAnnotation> annotations = client.getTextAnnotations(text);
//        client.printTextAnnotations(annotations);


        /**
         * Enhance Text
         */
//        Map<String, Integer> map = enhanceText();
//        System.out.println("MAP -> " + map);

        /**
         * Enhance File
         */
        File file = new File("/home/yamenj/Development/Simple-Anno/Fallberichte/impl_2015_04_s0433.pdf");
        Map<String, Integer> map = enhanceFile(file);
        System.out.println("MAP -> " + map);
    }

    public static String convertPDFtoText(File file) {
        PDFParser parser;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        PDFTextStripper pdfStripper;
        String parsedText;
        String text = null;
        try {
            try {
                parser = new PDFParser(new FileInputStream(file));
                parser.parse();
                cosDoc = parser.getDocument();
                pdfStripper = new PDFTextStripper();
                pdDoc = new PDDocument(cosDoc);
                parsedText = pdfStripper.getText(pdDoc);
                text = parsedText.replaceAll("[^A-Za-z0-9. ]+", "");
//                System.out.println(text);
            } finally {
                if (cosDoc != null)
                    cosDoc.close();
                if (pdDoc != null)
                    pdDoc.close();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return text;
    }

    public static void getMimeType(File file) {
        System.out.println("Mime Type of " + file.getName() + " is " +
                new MimetypesFileTypeMap().getContentType(file));

    }


    public static Map<String, Integer> enhanceText(String text) throws Exception {
        SAClient client = new SAClient();
        Map<String, Integer> map =  client.enhanceText(text);
        return map;


    }


    public static Map<String, Integer> enhanceFile(File file) throws Exception {

        String text = convertPDFtoText(file);
        SAClient client = new SAClient();
        Map<String, Integer> map =  client.enhanceText(text);
        return map;

    }

    /**
     * TODO: get Language from Mapper
     */

}
