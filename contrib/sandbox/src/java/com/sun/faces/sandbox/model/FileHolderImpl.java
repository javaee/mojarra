/**
 * 
 */
package com.sun.faces.sandbox.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
public class FileHolderImpl implements FileHolder {
    Map<String, InputStream> files = new HashMap<String, InputStream>();

    public void addFile(String fileName, InputStream is) {
        files.put(fileName, is);
    }

    public void clearFiles() {
        files.clear();
    }

    public InputStream getFile(String fileName) {
        return files.get(fileName);
    }
    
    public List<String> getFileNames() {
        List<String> fileNames = new ArrayList<String>();
        fileNames.addAll(files.keySet());
        return fileNames;
    }
    
    public void removeFile(String fileName) {
        files.remove(fileName);
    }
}