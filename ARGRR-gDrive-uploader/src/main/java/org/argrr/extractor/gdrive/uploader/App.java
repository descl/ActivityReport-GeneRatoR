package org.argrr.extractor.gdrive.uploader;

/*
 * #%L
 * ARGRR-gDrive-uploader
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

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.ParentReference;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.argrr.extractor.gdrive.downloader.DriveApi;
import org.argrr.extractor.utils.Config;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 *
 * @author cdesclaux <christophe@desclaux.me>
 */
public class App {
    
    
    public static void main( String[] args ) throws IOException{
        String rootOutputPath = App.class.getClassLoader().getResource("./").getPath()+"../../../report/generated";
        String rootTemplatesPath = App.class.getClassLoader().getResource("./").getPath()+"../../../templates";
        Drive drive =  DriveApi.getConnection();
        //remove the previous generated files
        Logger.getLogger(App.class.getName()).log(Level.INFO, "remove old uploaded files ");
            
        emptyFolder(drive,Config.getVar("DRIVE_FOLDER_GENERATED_ID"));
        emptyFolder(drive,Config.getVar("DRIVE_FOLDER_TEMPLATES_ID"));
            
        //add the generated files 
        System.out.println(rootOutputPath);
        
        for(java.io.File  f: FileUtils.listFiles(new java.io.File (rootOutputPath), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)){
            Logger.getLogger(App.class.getName()).log(Level.INFO, "add to gdrive file "+f.getName());
            uploadFile(f,drive,Config.getVar("DRIVE_FOLDER_GENERATED_ID"));
        }
        
        //add the templates files
        for(java.io.File  f: FileUtils.listFiles(new java.io.File (rootTemplatesPath), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)){
            Logger.getLogger(App.class.getName()).log(Level.INFO, "add to gdrive file "+f.getName());
            uploadFile(f,drive,Config.getVar("DRIVE_FOLDER_TEMPLATES_ID"));
        }
        
        uploadFile(FileUtils.getFile(rootOutputPath+"/../report.pdf"),drive,Config.getVar("DRIVE_FOLDER_GENERATED_ID"));
        uploadFile(FileUtils.getFile(rootOutputPath+"/../report.tex"),drive,Config.getVar("DRIVE_FOLDER_GENERATED_ID"));
    }
    
    public static void emptyFolder(Drive drive, String folder) throws IOException{
        ChildList children = drive.children().list(folder).execute();
            for (ChildReference child : children.getItems()) {
              drive.children().delete(folder, child.getId()).execute();
        }
    }
  public static void uploadFile(java.io.File file,Drive drive, String folder) throws IOException{
    

    com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
    fileMetadata.setTitle(file.getName());
    fileMetadata.setParents(Arrays.asList(new ParentReference().setId(folder)));

    FileContent mediaContent;
    if(file.getName().endsWith("pdf")){
        mediaContent = new FileContent("application/pdf", file);
    }else{
        mediaContent = new FileContent("text/plain", file);
    }
    

    Drive.Files.Insert insert = drive.files().insert(fileMetadata, mediaContent);
    insert.execute();

    
    
  }
}
