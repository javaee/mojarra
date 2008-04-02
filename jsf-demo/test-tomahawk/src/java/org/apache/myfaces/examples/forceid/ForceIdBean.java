/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.examples.forceid;


import java.util.ArrayList;

/**
 * @author <a href="mailto:matzew@apache.org">Matthias Weﬂendorf</a> 
 */
public class ForceIdBean {
    
    private String valueOne, valueTwo;
    private User users[];
    private ArrayList choices = new ArrayList();
    private String currentChoice;
    
    public ForceIdBean(){
        users = new User[2];
        users[0] = new User("MyFaces","secrect");
        users[1] = new User("Tomcat","secrect");
        
        choices.add("foo");
        choices.add("bar");
        choices.add("buzz");
    }

    public String getValueOne() {
        return valueOne;
    }
    public void setValueOne(String valueOne) {
        this.valueOne = valueOne;
    }
    public String getValueTwo() {
        return valueTwo;
    }
    public void setValueTwo(String valueTwo) {
        this.valueTwo = valueTwo;
    }
    public User[] getUsers() {
        return users;
    }
    public void setUsers(User[] users) {
        this.users = users;
    }
    
    public ArrayList getChoices()
    {
        return choices;
    }
    
    public void setCurrentChoice(String currentChoice)
    {
        this.currentChoice = currentChoice;
    }
    
    public String getCurrentChoice()
    {
        return currentChoice;
    }
}
