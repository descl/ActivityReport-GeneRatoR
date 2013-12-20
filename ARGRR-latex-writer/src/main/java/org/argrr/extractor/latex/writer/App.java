package org.argrr.extractor.latex.writer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.argrr.extractor.excel.SpreadSheetFile;
import org.argrr.extractor.inria.raweb.extractor.InriaRaweb;
import org.argrr.extractor.mysql.extractor.MySQLUtils;
import org.argrr.extractor.utils.Config;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String DRIVE_FOLDER = "target/driveDocs";
    private static final String GLC_RA_ID = "RA de GLC.xlsx";
    private static final String GLC_SOFTWARES_ID = "I3S-GLC softwares (distributed or not).xlsx";
    public static void main( String[] args ) throws IOException
    {
        String rootPath = App.class.getClassLoader().getResource("./").getPath()+"../../../";
        LaTeXUtils.init(rootPath);
        System.out.println(rootPath);
        
        VelocityContext context = new VelocityContext();
        
        context.put( "GLC_RA", new SpreadSheetFile(rootPath+DRIVE_FOLDER+"/"+GLC_RA_ID));
        context.put( "GLC_SOFTWARES", new SpreadSheetFile(rootPath+DRIVE_FOLDER+"/"+GLC_SOFTWARES_ID));
        context.put( "INRIA_RAWEB_EXPORT", new InriaRaweb(rootPath+Config.getVar("RAWEB_EXTRACTED_FILE")));
        context.put( "TDB_BDD", new MySQLUtils());
        context.put( "DATE", new DateTool());
        context.put( "Config", Config.class);
        
        
        for(String file : (new File(rootPath+"templates/")).list() ){
            if(file.endsWith(".tex")==true){ 
                System.out.println("Generating file"+ file);
                Template template = Velocity.getTemplate(file,"UTF-8");
                StringWriter sw = new StringWriter();
                template.merge( context, sw );
                String report =  sw.toString();

                LaTeXUtils.saveReport(report,file);
            }
        }
        LaTeXUtils.generatePDF("","report.tex");
    }
}
