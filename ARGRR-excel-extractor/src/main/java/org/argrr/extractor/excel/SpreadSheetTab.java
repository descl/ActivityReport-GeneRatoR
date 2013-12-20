/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.argrr.extractor.excel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author cdesclau
 */
public class SpreadSheetTab {
    public XSSFSheet sheet;
    public ArrayList<String> columnNames;
    public ArrayList<HashMap<String,String>> lines;
    
    public SpreadSheetTab(XSSFSheet sheet){
        this.sheet = sheet;
        columnNames = new ArrayList<String>();
        lines = new ArrayList<HashMap<String,String>>();

        
        //iterate throw the first line in order to have columns names
        Iterator<Row> rowIterator = sheet.iterator();
        Row curRow = rowIterator.next();
        for(int cn=0; cn< curRow.getLastCellNum(); cn++) {
            
            Cell cell = curRow.getCell(cn, Row.CREATE_NULL_AS_BLANK);
            columnNames.add(cell.getStringCellValue());
        }
        
        
        
        //Iterate through each other rows in order to have datas
        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            HashMap<String,String> curLine = new HashMap<String,String>();
            
            //For each row, iterate through all the columns
            for(int id=0; id< columnNames.size(); id++) {
                
                //add empty cells names if there are more cols in values than header def
                if(id >= this.columnNames.size())
                    this.columnNames.add("");
                
                Cell cell = row.getCell(id, Row.CREATE_NULL_AS_BLANK);
                if(id == 0 && cell.getCellType() == Cell.CELL_TYPE_BLANK){
                    break;
                }
                
                String cellVal = "";
                //Check the cell type and format accordingly
                switch (cell.getCellType()){
                    case Cell.CELL_TYPE_NUMERIC:
                        if(DateUtil.isCellDateFormatted(cell)){
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            cellVal = sdf.format(cell.getDateCellValue());
                        }else{
                            cellVal = Integer.valueOf(Double.valueOf(cell.getNumericCellValue()).intValue()).toString();
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        cellVal = cell.getStringCellValue();
                        break;
                }
                curLine.put(this.getColumnName(id),cellVal);
            }
            if(curLine.size()>0)
                lines.add(curLine);
        }
    }
    
    public String getColumnName(int i){
        return columnNames.get(i);
    }
    public String[][]  getLines(ArrayList<String> columns){
        String[][] result = new String[this.lines.size()][columns.size()];
        int i=0;
        for(HashMap<String,String> entry : this.lines) {
            
            for(int j=0; j < columns.size(); j++){
                result[i][j] = entry.get(columns.get(j));
                if(result[i][j] == null)result[i][j] = "";
            }
            i++;
        }
        return result;
    }
    
    public ArrayList<HashMap<String,String>>  getHashMap(){
        return lines;
    }
    
    @Override
    public String toString() {
        return "SpreadSheetTab{\n\t" + "columnName=" + columnNames + "\n\t lines=" + lines + '}';
    }
    
    
    
    
}
