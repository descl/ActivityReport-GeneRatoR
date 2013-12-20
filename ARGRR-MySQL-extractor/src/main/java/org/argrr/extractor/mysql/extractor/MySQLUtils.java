/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.argrr.extractor.mysql.extractor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.argrr.extractor.utils.Config;

/**
 *
 * @author cdesclau
 */
public class MySQLUtils {
    Connection con;
    Statement stmt;

    public MySQLUtils() {
        try {
            con = DriverManager.getConnection(Config.getVar("MYSQL_URL"),Config.getVar("MYSQL_LOGIN"), Config.getVar("MYSQL_PASS"));
            stmt = con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<HashMap<String,Object>> execRequest(String request){
        ArrayList<HashMap<String,Object>> result = new ArrayList<HashMap<String,Object>>();
        try {
            
            ResultSet rs = stmt.executeQuery(request);
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            while(rs.next()) {
                HashMap<String,Object> row = new HashMap<String,Object>();
                for(int i=1; i<=columns; i++){
                    try{
                        row.put(md.getColumnName(i),rs.getString(i));
                    }catch(java.sql.SQLException ex){
                        
                    }
                }
                result.add(row);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MySQLUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
        
    }
    
}
