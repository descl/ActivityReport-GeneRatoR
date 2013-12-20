/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.argrr.extractor.gdrive.downloader;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import java.io.File;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author cdesclau
 */
public class ChartsDownloader {
    private static String rootOutputPathCharts;
        
    public static void initChartsFolder(String outputFolder){
        ChartsDownloader.rootOutputPathCharts = outputFolder;
        File f = new File(outputFolder);
        try {
            FileUtils.cleanDirectory(f); //clean out directory (this is optional -- but good know)
            FileUtils.forceDelete(f); //delete directory
        } catch (Exception ex) {}
        try {
            FileUtils.forceMkdir(f); //create directory
        } catch (IOException ex) {
            Logger.getLogger(ChartsDownloader.class.getName()).log(Level.WARNING, "can't create the gdoc charts folder", ex);
        }
    }
    
    public static void extractPictures(String path, String fileName) throws IOException{
        PDDocument document = null; 
        try {
            document = PDDocument.load(path+"/"+fileName+".pdf");
         } catch (IOException ex) {
             System.out.println("" + ex);
         }
         List pages = document.getDocumentCatalog().getAllPages();
         Iterator iter = pages.iterator(); 
         int i =1;
         String name = null;

         while (iter.hasNext()) {
             PDPage page = (PDPage) iter.next();
             PDResources resources = page.getResources();
             Map pageImages = resources.getImages();
             if (pageImages != null) { 
                 Iterator imageIter = pageImages.keySet().iterator();
                 while (imageIter.hasNext()) {
                     String key = (String) imageIter.next();
                     PDXObjectImage image = (PDXObjectImage) pageImages.get(key);
                     image.write2file(ChartsDownloader.rootOutputPathCharts+"/"+fileName+"-"+i);
                     i ++;
                 }
             }
        }
    }
}
