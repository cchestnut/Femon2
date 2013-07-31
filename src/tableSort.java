// Version @(#)tableSort.java	1.1	03/2004
/**
 * A sorter for TableModels. The sorter has a model (conforming to TableModel) 
 * and itself implements TableModel. TableSorter does not store or copy 
 * the data in the TableModel, instead it maintains an array of 
 * integers which it keeps the same size as the number of rows in its 
 * model. When the model changes it notifies the sorter that something 
 * has changed eg. "rowsAdded" so that its internal array of integers 
 * can be reallocated. As requests are made of the sorter (like 
 * getValueAt(row, col) it redirects them to its model via the mapping 
 * array. That way the TableSorter appears to hold another copy of the table 
 * with the rows in a different order. The sorting algorthm used is stable 
 * which means that it does not move around rows when its comparison 
 * function returns 0 to denote that they are equivalent. 
 *
 * @version 1.0 03/2004
 * @author Madhavi Erukulla
 */

import java.util.*;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;

// Imports for picking up mouse events from the JTable. 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import javax.swing.JTable;
import javax.swing.table.*;
import javax.swing.table.TableColumnModel;

public class tableSort {
    DefaultTableModel	model;

    public void tableSort() {
	return;
    }

    public void sortTable(DefaultTableModel model1, Vector headVec) {
	model = model1;
	insertionSort(headVec);
    }

    public void insertionSort(Vector headVec) {
	int	low = 0;
	int	middle;
	int	result;
        int	rowCount = model.getRowCount();
	int	high = rowCount - 1;
        String	time = (String)headVec.get(2);

	if (rowCount == 0) {
	    model.insertRow(0, headVec);
	    return;
	}

	while (low < high) {
	    middle = (low + high)/2;
	    String s1 = (String)model.getValueAt(middle, 2);
	    result = time.compareTo(s1);

	    if (result < 0) {
		high = middle;
	    } else if (result > 0) {
		low = middle + 1;
	    } else {
		model.insertRow(middle, headVec);
		return;
	    }
	}
	if (low == high) {
	    String s1 = (String)model.getValueAt(low, 2);
	    result = time.compareTo(s1);

	    if (result < 0) {
		model.insertRow(low, headVec);
	    } else if (result > 0) {
		model.insertRow(high+1, headVec);
	    } else {
		model.insertRow(low, headVec);
	    }
	} else {
	    System.out.println("ERROR EOORO:" +low +":" +high);
	    System.exit(1);
	}
	return;
    }

}
