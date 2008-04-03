package com.sun.faces.mgbean;

import javax.faces.FacesException;

/**
 * Created by IntelliJ IDEA.
 * User: Ryan
 * Date: Apr 9, 2007
 * Time: 7:26:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ManagedBeanPreProcessingException extends FacesException {

    public enum Type {
        CHECKED,
        UNCHECKED
    }

    private Type type = Type.CHECKED;

    // ------------------------------------------------------------ Constructors

    public ManagedBeanPreProcessingException() {
        super();
    }


    public ManagedBeanPreProcessingException(Type type) {
        super();
        this.type = type;
    }


    public ManagedBeanPreProcessingException(String message) {
        super(message);
    }


    public ManagedBeanPreProcessingException(String message, Type type) {
        super(message);
        this.type = type;
    }


    public ManagedBeanPreProcessingException(Throwable t) {
        super(t);
    }


    public ManagedBeanPreProcessingException(Throwable t, Type type) {
        super(t);
        this.type = type;
    }


    public ManagedBeanPreProcessingException(String message, Throwable t) {
        super(message, t);
    }


    public ManagedBeanPreProcessingException(String message, Throwable t, Type type) {
        super(message, t);
        this.type = type;
    }


    // ---------------------------------------------------------- Public Methods


    public Type getType() {

        return type;

    }

}
