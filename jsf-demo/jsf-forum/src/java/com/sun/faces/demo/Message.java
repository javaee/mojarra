package com.sun.faces.demo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class Message {

    private int id;
    private String text;
    private String subject;
    private static int maxid = 0;
    private Thread parent;

    public Message() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        String topicId = context.getExternalContext()
                .getRequestParameterMap().get("topicId");

        if (topicId == null || !topicId.matches("[0-9]+")) {
            FacesMessage message = new FacesMessage(("Parameter topicId not set"));
            context.addMessage(null, message);
            return;
        }

        String threadId = context.getExternalContext()
                .getRequestParameterMap().get("threadId");

        if (threadId == null || !threadId.matches("[0-9]+")) {
            FacesMessage message = new FacesMessage(("Parameter threadId not set"));
            context.addMessage(null, message);
            return;
        }

        parent = new Thread(new Integer(threadId), new Topic(new Integer(topicId)));

        String replyMessage = context.getExternalContext()
                .getRequestParameterMap().get("replyMessage");
        if ("true".equals(replyMessage)) {
            id = ++maxid;
            return;
        }
        
        String messageId = context.getExternalContext()
                .getRequestParameterMap().get("messageId");

        if (messageId == null || !messageId.matches("[0-9]+")) {
            FacesMessage message = new FacesMessage(("Parameter messageId not set"));
            context.addMessage(null, message);
            return;
        }

        id = new Integer(messageId);
        setMaxId(id);

        load();
    }

    public Message(int id, Thread parent) throws IOException {
        this.id = id;
        setMaxId(id);
        this.parent = parent;
        load();
        parent.addMessage(this);
    }

    public Message(int id, String subject, String text, Thread parent) {
        this.id = id;
        setMaxId(id);
        this.subject = subject;
        this.text = text;
        this.parent = parent;
        parent.addMessage(this);
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    protected Thread getParent() {
        return parent;
    }

    protected void load() throws IOException {
        String dirname = Forum.datahome + File.separator + parent.getParent().getId() + File.separator
                         + parent.getId() + File.separator + id + File.separator;

        File f = new File(dirname+"message.txt");
        if (f.exists()) {
            Scanner s = new Scanner(f);
            if (s.hasNextLine()) {
                subject = s.nextLine();
            } else {
                throw new IOException("Missing subject in "+f);
            }
            if (!s.hasNextLine()) {
                throw new IOException("Missing text in "+f);
            }
            text = "";
            while (s.hasNextLine()) {
                text = text + s.nextLine() + "\r\n";
            }
        }
    }

    public void save() throws IOException {
        String dirname = Forum.datahome + File.separator + parent.getParent().getId() + File.separator
                         + parent.getId() + File.separator + id + File.separator;
        File d = new File(dirname);
        if (!d.exists()) {
            d.mkdirs();
        }
        String filename = dirname + "message.txt";
        File f = new File(filename);
        if (f.exists()) {
            // delete and recreate
            f.delete();
        }
        PrintWriter w = new PrintWriter(f);
        w.println(subject);
        w.print(text);
        w.close();



    }

    private void setMaxId(int id) {
        maxid = (id > maxid ? id : maxid);
    }
}
