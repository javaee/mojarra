/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.test.agnostic.facelets.html;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class DataTablePassthroughBean {

    public class Entity {

        private String name;

        private Date modifiedOn;

        public Entity(String name, Date modifiedOn) {
            this.name = name;
            this.modifiedOn = modifiedOn;
        }

        public Date getModifiedOn() {
            return modifiedOn;
        }

        public String getName() {
            return name;
        }

    }

    public List<Entity> entities = Arrays.asList(
            new Entity("name1", new Date()), new Entity("name2", new Date(
                            new Date().getTime() + (1000 * 60 * 60 * 24))), new Entity(
                    "name0", new Date(new Date().getTime()
                            + (1000 * 60 * 60 * 48))));

    public List<Entity> getEntities() {
        return entities;
    }
}
