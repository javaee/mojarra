package com.sun.faces.i_spec_802_war;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Scanner;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

@Named
@SessionScoped
public class UserBean implements Serializable {
    
    private Part uploadedFile;

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    
    public String getFileText() {
        String text = "";

        if (null != uploadedFile) {
            try {
                InputStream is = uploadedFile.getInputStream();
                text = new Scanner( is ).useDelimiter("\\A").next();
            } catch (IOException ex) {
                
            }
        }
        return text;
    }
    
}

