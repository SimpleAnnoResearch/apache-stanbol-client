package org.apache.stanbol.client.simpleAnno;

import com.google.common.collect.Multimap;
import org.apache.stanbol.client.enhancer.model.Enhancement;
import org.apache.stanbol.client.enhancer.model.EntityAnnotation;
import org.apache.stanbol.client.enhancer.model.TextAnnotation;

import java.util.Collection;

/**
 * client
 * Created by: Yamen Jeries on 5/2/17.
 * E-mail: yamen.jeries@jkaref.com
 */
public class EnhancementParser {

    Collection<Enhancement> fileEnhancements;
    Multimap<TextAnnotation,EntityAnnotation> annotations;
    ClientMapper mapper = new ClientMapper();

    public EnhancementParser() {
        this.fileEnhancements = fileEnhancements;
        this.annotations = annotations;
    }


    public ClientMapper parseEnhancement(Collection<Enhancement> enhancements) {
        for (Enhancement enhancement : enhancements) {
            if (enhancement instanceof EntityAnnotation) {
                EntityAnnotation entity = (EntityAnnotation) enhancement;

                String label = entity.getEntityLabel();

                if (!label.isEmpty()) {
                    mapper.addLabel(label);
                }

            }
            if (enhancement instanceof TextAnnotation) {
                TextAnnotation text = (TextAnnotation) enhancement;

                mapper.setLanguage(text.getLanguage());
            }

        }
        return mapper;
    }

    public ClientMapper parseAnnotations(Multimap<TextAnnotation,EntityAnnotation>  annotations) {

        for (EntityAnnotation ea : annotations.values()){
            mapper.addLabel(ea.getEntityLabel());
        }

        for (TextAnnotation ta : annotations.keys()) {
            mapper.setLanguage(ta.getLanguage());
        }

        return mapper;
    }


}
