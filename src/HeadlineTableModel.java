/*
 * HeadlineTableModel.java
 *
 * Created on December 4, 2006, 11:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author VisokayA
 */
import javax.swing.table.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.text.DateFormat;
import java.util.Date;
import java.sql.*;


public class HeadlineTableModel extends AbstractTableModel {
     String[] columnNames = {"FE","Doc ID#",
                        "Timestamp",
     "Headline"};
     
    ArrayList headlineList;
   
        /** Creates a new instance of JobStatusTableModel */
    public HeadlineTableModel(jfemonmainPanel jPanel) 
    throws IOException{
        super();
     }
    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public int getRowCount() {
        if (headlineList == null)
            return 0;
        return headlineList.size();
    }
    
    public String getColumnName (int columnIndex){
        return columnNames[columnIndex];
    }
    
    public void clearTableModel(){
        if (headlineList != null)
            headlineList.clear();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        HeadlineInfo hi = (HeadlineInfo)headlineList.get(rowIndex);
        
        switch (columnIndex){
            case 0:
                return hi.FE;
            case 1:
                return hi.DocID;
            case 2:
                //return hi.Timestamp;
                return Formatter.timeStampFormat(hi.Timestamp);
            case 3:
                return hi.Headline;
        }//end switch   
        return null;
    }
    
    public void LoadHeadlineTable(){
           headlineList = jmainFrame.searchResults;
           fireTableDataChanged();
    }
}
