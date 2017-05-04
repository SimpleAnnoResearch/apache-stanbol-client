package org.apache.stanbol.client.simpleAnno;

import org.apache.stanbol.client.enhancer.model.Enhancement;

import java.util.*;

/**
 * client
 * Created by: Yamen Jeries on 5/2/17.
 * E-mail: yamen.jeries@jkaref.com
 */
public class ClientMapper {

    HashMap<String, Integer> labels = new HashMap<>();

    private String language;


    public HashMap<String, Integer> getLabels() {
        return labels;
    }

    public void addLabel(String label) {

        if (!labels.containsKey(label)) {  // first time we've seen this string
            labels.put(label, 1);
        }
        else {
            int count = labels.get(label);
            labels.put(label, count + 1);
        }

    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
