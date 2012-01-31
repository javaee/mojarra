package com.sun.faces.i_spec_479_war;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class UserManager implements Serializable {
    
    Collection<UserBean> users;

    public Collection<UserBean> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserBean> users) {
        this.users = users;
    }
    
    public UserManager() {
        users = new MyCollection(new ArrayList<UserBean>());
        UserBean b;
        for (int i = 0; i < 3; i++) {
            b = new UserBean();
            b.setFirstName("First" + i);
            b.setLastName("Last"+i);
            users.add(b);
        }
    }
    
    private static class MyCollection implements Collection {
        
        private Collection inner;
        
        private MyCollection(Collection c) {
            inner = c;
        }

        @Override
        public boolean add(Object e) {
            return inner.add(e);
        }

        @Override
        public boolean addAll(Collection c) {
            return inner.addAll(c);
        }

        @Override
        public void clear() {
            inner.clear();
        }

        @Override
        public boolean contains(Object o) {
            return inner.contains(o);
        }

        @Override
        public boolean containsAll(Collection c) {
            return inner.containsAll(c);
        }

        @Override
        public boolean isEmpty() {
            return inner.isEmpty();
        }

        @Override
        public Iterator iterator() {
            return inner.iterator();
        }

        @Override
        public boolean remove(Object o) {
            return inner.remove(o);
        }

        @Override
        public boolean removeAll(Collection c) {
            return inner.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection c) {
            return inner.retainAll(c);
        }

        @Override
        public int size() {
            return inner.size();
        }

        @Override
        public Object[] toArray() {
            return inner.toArray();
        }

        @Override
        public Object[] toArray(Object[] a) {
            return inner.toArray(a);
        }
        
    }
    
}

