package org.argrr.extractor.latex.writer;

/*
 * #%L
 * ARGRR-latex-writer
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
 *
 * @author cdesclaux <christophe@desclaux.me>
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
