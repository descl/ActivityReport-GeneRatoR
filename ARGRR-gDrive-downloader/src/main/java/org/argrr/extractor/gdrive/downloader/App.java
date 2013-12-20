package org.argrr.extractor.gdrive.downloader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

  public static void main(String[] args) {
      String rootOutputPathCharts = App.class.getClassLoader().getResource("./").getPath()+"../../../report/gdoc-charts";
      try {
          DriveApi.getConnection();
          ChartsDownloader.initChartsFolder(rootOutputPathCharts);
          DriveApi.downloadFolder();
      } catch (IOException ex) {
          Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
      }
  }

}
