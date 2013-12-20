/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.argrr.extractor.excel;

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
 * @author cdesclau
 */
public class SpreadSheetFile {
    public XSSFWorkbook workbook;
    public HashMap<String,SpreadSheetTab> tabs;

    public SpreadSheetFile(String f) {
        this(new File(f));
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
