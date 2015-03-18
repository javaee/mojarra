package com.sun.faces.test.servlet40.facelets;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class MapBean {

    public Map<String, Integer> getMap() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("Amsterdam", 821702);
        map.put("Rotterdam", 624799);
        map.put("Den Haag", 514782);

        return map;
    }

}
