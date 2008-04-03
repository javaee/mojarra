/**
 * 
 */
package com.sun.faces.sandbox.test.webapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.html.HtmlOutputText;

import com.sun.faces.sandbox.component.YuiTree;
import com.sun.faces.sandbox.component.YuiTreeNode;
import com.sun.faces.sandbox.model.FileHolder;
import com.sun.faces.sandbox.model.FileHolderImpl;

/**
 * @author lee
 *
 */
public class TestBean {
    protected YuiTree tree;
    protected Date date;
    protected Date date2;
    protected FileHolder fileHolder = new FileHolderImpl();
    
    public TestBean() {
        tree = new YuiTree();
        YuiTreeNode node1 = new YuiTreeNode();
        YuiTreeNode node2 = new YuiTreeNode();
        YuiTreeNode node3 = new YuiTreeNode();
        
        HtmlOutputText text1 = new HtmlOutputText(); text1.setValue("Text 1");
        HtmlOutputText text2 = new HtmlOutputText(); text2.setValue("Text 2");
        HtmlOutputText text3 = new HtmlOutputText(); text3.setValue("Text 3");

        node1.getFacets().put("label", text1);
        node1.getChildren().add(node3);
        node2.getFacets().put("label", text3);
        node3.getFacets().put("label", text2);

        tree.getChildren().add(node1);
        tree.getChildren().add(node2);

        Calendar tempcal = Calendar.getInstance();
        tempcal.set(1974, 11, 23);
        date2 = tempcal.getTime();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date) {
        this.date2 = date;
    }

    public byte[] getPdf() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/sample.pdf");
            
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
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/sample.png");
            
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
        return "success";
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
        list.add(new Person("Jim", "Halpert"));
        list.add(new Person("Michael", "Scott"));
        
        return list;
    }

    public YuiTree getTree() {
        return tree;
    }

    public void setTree(YuiTree tree) {
        this.tree = tree;
    }
}

class Person {
    protected Integer id;
    protected String lastName;
    protected String firstName;
    
    public Person() {
    }
    
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
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
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}