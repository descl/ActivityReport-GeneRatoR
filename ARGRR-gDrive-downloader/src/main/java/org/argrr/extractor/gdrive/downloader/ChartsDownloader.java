package org.argrr.extractor.gdrive.downloader;

/*
 * #%L
 * ARGRR-gDrive-downloader
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
 * @author cdesclaux <christophe@desclaux.me>
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
