package com.sun.faces.demo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;


@ManagedBean
@RequestScoped
public class Thread {

    private int id;
    private static int maxid = 0;
    private Topic parent;

    private String title;
    private String text;
    private List<Message> messages =  Collections.synchronizedList(new ArrayList());;


    public Thread() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        String topicId = context.getExternalContext()
                .getRequestParameterMap().get("topicId");

        if (topicId == null || !topicId.matches("[0-9]+")) {
            FacesMessage message = new FacesMessage(("Parameter topicId not set"));
            context.addMessage(null, message);
            return;
        }

        String addThread = context.getExternalContext()
                .getRequestParameterMap().get("addThread");
        if ("true".equals(addThread)) {
            parent = new Topic(new Integer(topicId));
            id = ++maxid;
            return;
        }

        String threadId = context.getExternalContext()
                .getRequestParameterMap().get("threadId");

        if (threadId == null || !threadId.matches("[0-9]+")) {
            FacesMessage message = new FacesMessage(("Parameter threadId not set"));
            context.addMessage(null, message);
            return;
        }
        parent = new Topic(new Integer(topicId));
        id = new Integer(threadId);

        load();
    }

    public Thread(int id, Topic parent) throws IOException{
        this.id = id;
        this.parent = parent;
        parent.addThread(this);
        load();
    }

    public Thread(int id, String title, Topic parent) {
        this.id = id;
        setMaxId(id);
        this.title = title;
        this.parent = parent;
        parent.addThread(this);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    protected Topic getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public int getSizeMessages() {
        return messages.size();
    }

    protected void load() throws IOException {
        String dirname = Forum.datahome + File.separator + parent.getId() + File.separator
                         + id + File.separator;

        File f = new File(dirname+"thread.txt");
        if (f.exists()) {
            Scanner s = new Scanner(f);
            if (s.hasNextLine()) {
                title = s.nextLine();
            } else {
                throw new IOException("Missing title in "+f);
            }
        }
        f = new File(dirname);
        File[] flist = f.listFiles();
        if (flist == null) {
            return;
        }
        for (File file : flist) {
            if (file.isDirectory() && file.getName().matches("[0-9]*")) {
                int messageId = Integer.valueOf(file.getName());
                new Message(messageId, this);
            }
        }
    }

    public void save() throws IOException {

        String dirname = Forum.datahome + File.separator + parent.getId() + File.separator
                         + id + File.separator;
        File d = new File(dirname);
        if (!d.exists()) {
            d.mkdirs();
        }
        String filename = dirname + "thread.txt";
        File f = new File(filename);
        if (f.exists()) {
            // delete and recreate
            f.delete();
        }
        PrintWriter w = new PrintWriter(f);
        w.println(title);
        w.close();

        for (Message message: messages) {
            message.save();
        }
    }

    public void createThread(ActionEvent ae) throws IOException {

        Message firstMessage = new Message(0, title, text, this);
        save();

    }

    protected void addMessage(Message message) {
        messages.add(message);
    }

    private void setMaxId(int id) {
        maxid = (id > maxid ? id : maxid);
    }

}
