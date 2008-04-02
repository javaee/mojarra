package com.sun.faces.systest;

import javax.faces.el.PropertyResolver;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;


public class RegisteredPropertyResolver extends PropertyResolver { 

     private PropertyResolver delegate;

    public RegisteredPropertyResolver(PropertyResolver delegate) {
        this.delegate = delegate;
    }

    public Object getValue(Object object, Object object1) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".getValue(Object,Object) called");
        return delegate.getValue(object, object1);
    }

    public Object getValue(Object object, int i) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".getValue(Object,int) called");
        return delegate.getValue(object, i);
    }

    public void setValue(Object object, Object object1, Object object2) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".setValue(Object,Object) called");
        delegate.setValue(object, object1, object2);
    }

    public void setValue(Object object, int i, Object object1) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".setValue(Object,int) called");
        delegate.setValue(object, i, object1);
    }

    public boolean isReadOnly(Object object, Object object1) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".isReadOnly(Object,Object) called");
        return delegate.isReadOnly(object, object1);
    }

    public boolean isReadOnly(Object object, int i) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".isReadOnly(Object,int) called");
        return delegate.isReadOnly(object, i);
    }

    public Class getType(Object object, Object object1) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".getType(Object,Object) called");
        return delegate.getType(object, object1);
    }

    public Class getType(Object object, int i) throws EvaluationException, PropertyNotFoundException {
        System.out.println(this.getClass().getName() + ".getValue(Object,int) called");
        return delegate.getType(object, i);
    }
}
