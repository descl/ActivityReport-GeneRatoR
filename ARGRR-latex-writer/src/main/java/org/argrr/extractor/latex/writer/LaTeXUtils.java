/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.argrr.extractor.latex.writer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.texen.util.FileUtil;
import org.argrr.extractor.excel.SpreadSheetFile;

/**
 *
 * @author cdesclau
 */
public class LaTeXUtils {
    public static File outputPath;
    
    public static void init(String path){
        Properties p = new Properties();
        p.setProperty("file.resource.loader.path", path+"templates");
        LaTeXUtils.outputPath = new File(path+"/report/");
        Velocity.init( p );
        
    }
    
    public static String getTable(Object[][] table){
        Template template = null;
        VelocityContext context = new VelocityContext();
        context.put( "table", table );
        template = Velocity.getTemplate("table.tex");
        StringWriter sw = new StringWriter();
        template.merge( context, sw );
        return sw.toString();
    }
    
    public static void saveReport(String text, String file){
        Writer writer = null;
        File f = new File(outputPath.getAbsolutePath().concat("/generated/").concat(file));
        
        try {
            FileUtils.writeStringToFile(f, text, "UTF-8");
        
            //writer = new BufferedWriter(new OutputStreamWriter(
            //      new FileOutputStream(outputDir.getAbsolutePath().concat(file)), "utf-8"));
            //writer.write(text);
        } catch (IOException ex) {
                Logger.getLogger(LaTeXUtils.class.getName()).log(Level.SEVERE, null, ex);
          // report
        }
    }
    
    public static String getAllReport(SpreadSheetFile sheetFile){
        VelocityContext context = new VelocityContext();
        context.put( "DATA", sheetFile );
        Template template = Velocity.getTemplate("allReport.tex");
        
        StringWriter sw = new StringWriter();
        template.merge( context, sw );
        return sw.toString();
    
    }
    public static String getReport(String core){
        VelocityContext context = new VelocityContext();
        context.put( "DATA", core );
        Template template = Velocity.getTemplate("document.tex");
        StringWriter sw = new StringWriter();
        template.merge( context, sw );
        return sw.toString();
    
    }
    
    public static void generatePDF(String path, String file){
        String s = null;

        try {
            Process p = Runtime.getRuntime().exec("pdflatex -interaction=nonstopmode "+file,null,new File(outputPath.getAbsolutePath().concat(path)));

             BufferedReader stdInput = new BufferedReader(new
             InputStreamReader(p.getInputStream()));

             BufferedReader stdError = new BufferedReader(new
             InputStreamReader(p.getErrorStream()));

             // read the output from the command
             System.out.println("Here is the standard output of the command:\n");
             while ((s = stdInput.readLine()) != null) {
             System.out.println(s);
             }

             // read any errors from the attempted command
             System.out.println("Here is the standard error of the command (if any):\n");
             while ((s = stdError.readLine()) != null) {
             System.out.println(s);
             }

             System.exit(0);
        } catch (Exception e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
