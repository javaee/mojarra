package com.sun.faces.test.agnostic.facelets.c;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


@ManagedBean(name="setForEachBean")
@RequestScoped
public class SetForEachBean {

    public Set<String> getSet() {
        return new LinkedHashSet<String>(Arrays.asList("foo", "bar", "baz"));
    }
}
