package com.sun.faces.systest;

import java.util.StringTokenizer;

public class Name {
  String first = "";
  String last = "";

    public Name(String name) {

        System.out.println("Name : name = " + name);
        StringTokenizer token = new StringTokenizer(name);
        String[] nameArray = new String[2];
        int i = 0;
        while (token.hasMoreTokens()) {
            nameArray[i] = token.nextToken();
            //System.out.println("Name : nameArray[" + i + "] = " + nameArray[i]);
            i++;
        }
        if (nameArray[0] != null)
            this.first = nameArray[0].toUpperCase();
        if (nameArray[1] != null)
            this.last = nameArray[1].toUpperCase();
    }

    public String getFirst() {
        return first;
    }

    public String  getLast() {
        return last;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
