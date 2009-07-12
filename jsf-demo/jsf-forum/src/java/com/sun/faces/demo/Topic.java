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
public class Topic {

    private int id;
    private String title;
    private String subject;
    private List<Thread> threads = Collections.synchronizedList(new ArrayList());
    private static int maxid = 0;

    public Topic() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        String addTopic = context.getExternalContext()
                .getRequestParameterMap().get("addTopic");
        if ("true".equals(addTopic)) {
            id = ++maxid;
            return;
        }

        String topicId = context.getExternalContext()
                .getRequestParameterMap().get("topicId");
        if (topicId == null || !topicId.matches("[0-9]+")) {
            FacesMessage message = new FacesMessage(("Parameter topicId not set"));
            context.addMessage(null, message);
            return;
        }
        id = new Integer(topicId);
        setMaxId(id);
        load();
    }

    public Topic(int id) throws IOException {
        this.id = id;
        setMaxId(id);
        load();
    }

    public Topic(int id, String title, String subject) {
        this.id = id;
        setMaxId(id);
        this.title = title;
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public int getSizeThreads() {
        if (threads == null) {
            return 0;
        }
        return threads.size();
    }

    public int getSizeMessages() {
        if (threads == null) {
            return 0;
        }
        int result = 0;
        for (Thread thread : threads) {
            result = result + thread.getSizeMessages();
        }
        return result;
    }

    protected void load() throws IOException {
        String dirname = Forum.datahome + File.separator + id + File.separator;

        File f = new File(dirname+"topic.txt");
        if (f.exists()) {
            Scanner s = new Scanner(f);
            if (s.hasNextLine()) {
                title = s.nextLine();
            } else {
                throw new IOException("Missing title in "+f);
            }
            if (s.hasNextLine()) {
                subject = s.nextLine();
            } else {
                throw new IOException("Missing subject in "+f);
            }
        }
        f = new File(dirname);
        File[] flist = f.listFiles();
        if (flist == null) {
            return;
        }
        for (File file : flist) {
            if (file.isDirectory() && file.getName().matches("[0-9]*")) {
                int threadId = Integer.valueOf(file.getName());
                new Thread(threadId, this);
            }
        }
    }

    public void save() throws IOException {
        String dirname = Forum.datahome + File.separator + id + File.separator;
        File d = new File(dirname);
        if (!d.exists()) {
            d.mkdirs();
        }
        String filename = dirname + "topic.txt";
        File f = new File(filename);
        if (f.exists()) {
            // delete and recreate
            f.delete();
        }
        PrintWriter w = new PrintWriter(f);
        w.println(title);
        w.println(subject);
        w.close();

        for (Thread thread : threads) {
            thread.save();
        }
    }

    public void save(ActionEvent ae) throws IOException {
        save();
    }

    protected void addThread(Thread thread) {
        threads.add(thread);
    }

    private void setMaxId(int id) {
        maxid = (id > maxid ? id : maxid);
    }
}
