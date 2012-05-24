package com.sun.faces.test.webprofile.seam;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class Issue2405Bean implements Serializable {

    private static final Logger log = Logger.getLogger(Issue2405Bean.class.getName());
    private Integer pqId;

    @PostConstruct
    public void construct() {
    }

    public Integer getPqId() {
        return pqId;
    }

    public void setPqId(Integer pqId) {
        this.pqId = pqId;
    }

    public void init() {
    }
}
