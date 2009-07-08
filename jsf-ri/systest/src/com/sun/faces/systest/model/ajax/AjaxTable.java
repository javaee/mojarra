package com.sun.faces.systest.model.ajax;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean
@SessionScoped
public class AjaxTable {


    private Info[] list = new Info[]{
            new Info(101, "Ryan", "Mountain Fastness", true),
            new Info(102, "Jim", "Santa Clara", true),
            new Info(103, "Roger", "Boston", true),
            new Info(104, "Ed", "Orlando", false),
            new Info(105, "Barbera", "Mountain View", true),
    };

    public Info[] getList() {
        return list;
    }

    public class Info {
        int id;
        String name;
        String city;
        boolean likescheese;

        public Info(int id, String name, String city, boolean likescheese) {
            this.id = id;
            this.name = name;
            this.city = city;
            this.likescheese = likescheese;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }


        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public boolean getLikesCheese() {
            return likescheese;
        }

        public void setLikesCheese(boolean likescheese) {
            this.likescheese = likescheese;
        }

        public String getCheesePreference() {
            return (likescheese ? "Cheese Please" : "Eww");
        }

    }


}
