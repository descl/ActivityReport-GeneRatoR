package org.argrr.extractor.inria.raweb.extractor;

/*
 * #%L
 * ARGRR-inria-raweb-extractor
 * %%
 * Copyright (C) 2013 I3S
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import org.jdom.input.SAXBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.jdom.*;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.argrr.extractor.utils.Config;

/**
 *
 * @author cdesclaux <christophe@desclaux.me>
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
