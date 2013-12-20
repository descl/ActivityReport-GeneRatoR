package org.argrr.extractor.mysql.extractor;

/*
 * #%L
 * ARGRR-MySQL-extractor
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
 * @author cdesclaux <christophe@desclaux.me>
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
