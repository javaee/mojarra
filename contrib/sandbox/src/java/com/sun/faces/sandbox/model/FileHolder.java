/**
 * 
 */
package com.sun.faces.sandbox.model;

import java.io.InputStream;
import java.util.List;

/**
 * @author Jason Lee
 *
 */
public interface FileHolder {
    void addFile(String fileName, InputStream is);
    void removeFile(String fileName);
    void clearFiles();

    List<String> getFileNames();
    InputStream getFile(String fileName);
}
