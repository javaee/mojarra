package com.sun.faces.config.initfacescontext;

import static java.util.Collections.emptyList;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

public class NoOpFlash extends Flash {

    @Override
    public void doPostPhaseActions(FacesContext ctx) {

    }

    @Override
    public void doPrePhaseActions(FacesContext ctx) {

    }

    @Override
    public boolean isKeepMessages() {
        return false;
    }

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public void keep(String key) {

    }

    @Override
    public void putNow(String key, Object value) {

    }

    @Override
    public void setKeepMessages(boolean newValue) {

    }

    @Override
    public void setRedirect(boolean newValue) {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return Collections.emptySet();
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Set<String> keySet() {
        return Collections.emptySet();
    }

    @Override
    public Object put(String key, Object value) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {

    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Collection<Object> values() {
        return emptyList();
    }

}