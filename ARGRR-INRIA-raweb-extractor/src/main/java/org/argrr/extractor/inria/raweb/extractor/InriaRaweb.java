/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.argrr.extractor.inria.raweb.extractor;

import org.jdom.input.SAXBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.filter.*;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.argrr.extractor.utils.Config;
/**
 *
 * @author cdesclau
 */
public class InriaRaweb {
    private Element root;
    
    /**
     * 
     */
    public InriaRaweb(){
        this(Config.getVar("RAWEB_EXTRACTED_FILE"));
    }
    
    /**
     * 
     * @param xmlFileUrl :url of the xml file
     */
    public InriaRaweb(String xmlFileUrl){
        root = new Element("raweb");
        try{
            SAXBuilder sxb = new SAXBuilder();
            //do not try to download the dtd file
            sxb.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            org.jdom.Document document = sxb.build(new File(xmlFileUrl));

            root = document.getRootElement();
        } catch (JDOMException ex) {
            Logger.getLogger(InriaRaweb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InriaRaweb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * get the team list
     * @return 
     */
    public ArrayList<HashMap<String,String>> getTeam(){
        ArrayList<HashMap<String,String>> persons = new ArrayList<HashMap<String,String>>();
        List team = this.root.getChild("team").getChildren("person");

        Iterator i = team.iterator();
        while(i.hasNext()){
            //iterate other each person
            Element person = ((Element)i.next());
            HashMap<String,String> personInfos = new HashMap<String,String>();
            Iterator j = person.getChildren().iterator();
            while(j.hasNext()){
                //iterate other each person attrs
                Element attr = ((Element)j.next());
                personInfos.put(attr.getName(), attr.getText());
            }
            persons.add(personInfos);
        }
        return persons;
    }
    
    /**
     * get the team list
     * @return 
     */
    public ArrayList<String> getKeyWords(){
        ArrayList<String> words = new ArrayList<String>();
        List wordsXml = this.root.getChild("identification").getChild("keywords").getChildren("term");

        Iterator i = wordsXml.iterator();
        while(i.hasNext()){
            //iterate other each keyword
            Element word = ((Element)i.next());
            words.add(word.getText());
        }
        return words;
    }
}
