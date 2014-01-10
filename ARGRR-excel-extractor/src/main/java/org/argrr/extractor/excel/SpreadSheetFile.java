package org.argrr.extractor.excel;

/*
 * #%L
 * ARGRR-excel-extractor
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author cdesclaux <christophe@desclaux.me>
 */
public class SpreadSheetFile {
    
    private static final String rootPath = SpreadSheetFile.class.getClassLoader().getResource("./").getPath()+"../../../";
    private static final String DRIVE_FOLDER = "target/driveDocs";
    public XSSFWorkbook workbook;
    public HashMap<String,SpreadSheetTab> tabs;

    public SpreadSheetFile(String f) {
        this(new File(rootPath+DRIVE_FOLDER+"/"+f));
    }
    public SpreadSheetFile(File f) {
        FileInputStream file = null;
        tabs = new HashMap<String,SpreadSheetTab>();
        try {
            file = new FileInputStream(f);
            //Create Workbook instance holding reference to .xlsx file
            workbook = new XSSFWorkbook(file);
            for(int i=0; i < workbook.getNumberOfSheets(); i++){
            //int i = 2;
                XSSFSheet t = workbook.getSheetAt(i);
                tabs.put(t.getSheetName(), new SpreadSheetTab(t));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SpreadSheetFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SpreadSheetFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(SpreadSheetFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public SpreadSheetTab getTab(String name){
        return tabs.get(name);
    }
    
    public static SpreadSheetFile getFile(String file){
        return new SpreadSheetFile(file);
    }
    
    @Override
    public String toString() {
        String str =  "SpreadSheetFile{" + "workbook with =" + tabs.size() + "\ntabs:\n";
        for(Entry<String, SpreadSheetTab> entry : this.tabs.entrySet()) {
            String key = entry.getKey();
            SpreadSheetTab value = entry.getValue();
            str += "\t"+key+" \n";
        }
        return str +="}\n";
    }
    
    
}
