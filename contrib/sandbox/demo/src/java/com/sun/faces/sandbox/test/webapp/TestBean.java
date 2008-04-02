/**
 * 
 */
package com.sun.faces.sandbox.test.webapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sun.faces.sandbox.model.FileHolder;
import com.sun.faces.sandbox.model.FileHolderImpl;

/**
 * @author lee
 *
 */
public class TestBean {
    protected Date date;
    protected FileHolder fileHolder = new FileHolderImpl();
    
    public TestBean() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getPdf() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("/sample.pdf");
            
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return baos.toByteArray();
    }
    
    public byte[] getImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("/sample.png");
            
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return baos.toByteArray();
    }

    public String getDestination() {
        return "jsp/success";
    }

    public FileHolder getFileHolder() {
        return fileHolder;
    }

    public String[] getFileNames() {
        String[] fileNames = fileHolder.getFileNames().toArray(new String[]{});
        fileHolder.clearFiles();
        
        return fileNames;
    }
    
    public List<Person> getPersonList() {
        List<Person> list = new ArrayList();
        
        return list;
    }
}

class Person {
    protected int id;
    protected String lastName;
    protected String firstName;
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    
}