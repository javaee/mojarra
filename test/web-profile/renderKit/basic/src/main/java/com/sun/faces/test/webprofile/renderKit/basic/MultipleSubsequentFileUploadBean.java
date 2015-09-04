package com.sun.faces.test.webprofile.renderKit.basic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javax.servlet.http.Part;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named
@RequestScoped
public class MultipleSubsequentFileUploadBean {

    private Part file;

    public String doUpload() {

        return null;
    }

    public String getFileText() {
        String text = "";

        if (null != getFile()) {
            try {
                InputStream is = getFile().getInputStream();
                text = new Scanner(is).useDelimiter("\\A").next();
            } catch (IOException ex) {

            }
        }
        return text;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

}
