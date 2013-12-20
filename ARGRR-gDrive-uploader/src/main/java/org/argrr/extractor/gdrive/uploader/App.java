package org.argrr.extractor.gdrive.uploader;

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
 * Hello world!
 *
 */
public class App {
    
    
    public static void main( String[] args ) throws IOException{
        String rootOutputPath = App.class.getClassLoader().getResource("./").getPath()+"../../../report/generated";
        Drive drive =  DriveApi.getConnection();
        //remove the previous generated files
        Logger.getLogger(App.class.getName()).log(Level.INFO, "remove old uploaded files ");
            
        ChildList children = drive.children().list(Config.getVar("DRIVE_FOLDER_GENERATED_ID")).execute();
            for (ChildReference child : children.getItems()) {
              drive.children().delete(Config.getVar("DRIVE_FOLDER_GENERATED_ID"), child.getId()).execute();
        }
            
        for(java.io.File  f: FileUtils.listFiles(new java.io.File (rootOutputPath), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)){
            Logger.getLogger(App.class.getName()).log(Level.INFO, "add to gdrive file "+f.getName());
            uploadFile(f,drive);
        }
        uploadFile(FileUtils.getFile(rootOutputPath+"/../report.pdf"),drive);
        uploadFile(FileUtils.getFile(rootOutputPath+"/../report.tex"),drive);
    }
    
  public static void uploadFile(java.io.File file,Drive drive) throws IOException{
    

    com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
    fileMetadata.setTitle(file.getName());
    fileMetadata.setParents(Arrays.asList(new ParentReference().setId(Config.getVar("DRIVE_FOLDER_GENERATED_ID"))));

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
