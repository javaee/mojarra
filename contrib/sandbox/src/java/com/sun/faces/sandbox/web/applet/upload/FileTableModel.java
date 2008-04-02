/**
 * 
 */
package com.sun.faces.sandbox.web.applet.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * @author Jason Lee
 *
 */
public class FileTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private String[] columnNames = { "Name", "Size", "Directory", "Readable" };
    private List<File> data = new ArrayList<File>();
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnNames.length;
    }
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return data.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        String value = "";
            File file = data.get(row);
            switch (col) {
            case 0 : value = file.getName(); break;
            case 1 : value = Long.toString(file.length()); break;
            case 2 : value = file.getParentFile().getPath(); break;
            case 3 : value = Boolean.toString(file.canRead()); break;
            }
        return value;
    }

    public void addFile(File file) {
        data.add(file);
    }
    
    public void removeFile(int index) {
        data.remove(index);
    }
    
    public List<File> getFiles() {
        return data;
    }
}
