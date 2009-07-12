package com.sun.faces.demo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class Forum {

    private List<Topic> topics = Collections.synchronizedList(new ArrayList());;
    protected static String datahome = "/tmp/jsf-forum-database";
    private  String title = "JSF Forum";


    public Forum() throws IOException {
        load();
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void load() throws IOException {
        File f = new File(datahome+File.separator+"forum.txt");
        if (f.exists()) {
            Scanner s = new Scanner(f);
            if (s.hasNextLine()) {
                title = s.nextLine();
            } else {
                throw new IOException("Missing title in "+f);
            }
        }
        f = new File(datahome);
        File[] flist = f.listFiles();
        for (File file : flist) {
            if (file.isDirectory() && file.getName().matches("[0-9]*")) {
                int topicId = Integer.valueOf(file.getName());
                topics.add(new Topic(topicId));
            }
        }
    }

    /**
     * Write all topics and associated threads and messages to storage
     */
    public void save() throws IOException {
        File f = new File(datahome+File.separator+"forum.txt");
        if (f.exists()) {
            // delete and recreate
            f.delete();
        }
            PrintWriter w = new PrintWriter(f);
            w.println(title);
            w.close();
        
        
        for (Topic topic : topics) {
            topic.save();
        }
    }

    /**
     * Add a topic to the list of forum topics
     * @param topic
     */
    protected void addTopic(Topic topic) {
        topics.add(topic);
    }

    /**
     * Initialize the forum database
     * @throws IOException
     */
    public void init() throws IOException {

        topics.clear();

        String genericText = "Rather long and wordy message text would be inserted here";

        Topic topic1 = new Topic(0, "First Topic", "First Topic");
        Topic topic2 = new Topic(1, "Second Topic", "Second Topic");
        addTopic(topic1);
        addTopic(topic2);

        Thread thread1 = new Thread(0, "First Thread", topic1);
        Thread thread2 = new Thread(1, "Second Thread", topic1);

        Thread thread3 = new Thread(0, "First Thread", topic2);
        Thread thread4 = new Thread(1, "Second Thread", topic2);

        new Message(0, "First Message", genericText, thread1);
        new Message(1, "Second Message", genericText, thread1);
        new Message(2, "Third Message", genericText, thread1);
        new Message(0, "First Message", genericText, thread2);
        new Message(1, "Second Message", genericText, thread2);
        new Message(2, "Third Message", genericText, thread2);
        new Message(0, "First Message", genericText, thread3);
        new Message(1, "Second Message", genericText, thread3);
        new Message(2, "Third Message", genericText, thread3);
        new Message(0, "First Message", genericText, thread4);
        new Message(1, "Second Message", genericText, thread4);
        new Message(2, "Third Message", genericText, thread4);

        File f = new File(datahome);
        if (!f.exists()) {
            if (!f.mkdir()) {
                throw new IOException("Cannot create file "+f);
            }
        }
        save();
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
