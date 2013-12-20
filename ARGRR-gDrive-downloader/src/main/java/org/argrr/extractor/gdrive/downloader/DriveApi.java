/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.argrr.extractor.gdrive.downloader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.argrr.extractor.utils.Config;

/**
 *
 * @author cdesclau
 */
public class DriveApi {

    public static final String ITEMS_FOLDER = Config.getVar("DRIVE_FOLDER_ID");
    public static final String OUTPUT_FOLDER = "./target/driveDocs";
  

    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), "/"+Config.getVar("DRIVE_APP_NAME"));

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global Drive API client. */
    public static Drive drive;

    /** Authorizes the installed application to access user's protected data. */
    public static Credential authorize() throws Exception {
        // load client secrets
        InputStream fis = Config.getDriveApi();


        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,new InputStreamReader(fis));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter") || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println(
                "Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
                + "into drive-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }
        
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            httpTransport, JSON_FACTORY, clientSecrets,
            Collections.singleton(DriveScopes.DRIVE)).setDataStoreFactory(dataStoreFactory)
            .build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static Drive getConnection(){
        if(DriveApi.drive != null){
            return DriveApi.drive;
        }

        try {

            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            // authorization
            Credential credential = authorize();
            // set up the global Drive instance
            drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(Config.getVar("DRIVE_APP_NAME")).build();
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return drive;

    }

    public static void downloadFolder() throws IOException{
      List<File> result = new ArrayList<File>();
        Drive.Files.List request = drive.files().list().setQ("'"+ITEMS_FOLDER+"' IN parents");
        
        do {
          try {
            FileList files = request.execute();

            result.addAll(files.getItems());
            request.setPageToken(files.getNextPageToken());
          } catch (IOException e) {
              Logger.getLogger(DriveApi.class.getName()).log(Level.WARNING, null, "An error occurred: " + e);
            request.setPageToken(null);
          }
        } while (request.getPageToken() != null &&
                 request.getPageToken().length() > 0);

        for(File f : result){
            downloadFile(drive, f,OUTPUT_FOLDER,"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","xlsx");
            downloadFile(drive, f,OUTPUT_FOLDER,"application/pdf","pdf");
        }
    }

    private static void downloadFile(Drive service, File file,String folder, String type, String extension)  {
      Logger.getLogger(DriveApi.class.getName()).log(Level.INFO, "Downloading file="+file.getTitle()+"."+extension);
      String downloadUrl = null;
      try{
          downloadUrl = file.getExportLinks().get(type);
      }catch(java.lang.NullPointerException ex){}

      InputStream is = null;
      FileOutputStream os = null;
      if (downloadUrl != null && downloadUrl.length() > 0) {
        try {
          HttpResponse resp =
              service.getRequestFactory().buildGetRequest(new GenericUrl(downloadUrl))
                  .execute();
          is =  resp.getContent();

          java.io.File fi = new java.io.File(folder+"/"+file.getTitle()+"."+extension);
          FileUtils.copyInputStreamToFile(is, fi);

          if(extension.equals("pdf")){
              ChartsDownloader.extractPictures(folder, file.getTitle());
          }
        } catch (IOException e) {
          // An error occurred.
          e.printStackTrace();
        }


      } else {
        Logger.getLogger(App.class.getName()).log(Level.SEVERE,"The file doesn't have any content stored on Drive.");
      }
    }


}
