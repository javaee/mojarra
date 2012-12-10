/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.

 */
package com.sun.faces.test.renderKit.passthrough;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Bean implements Serializable {
    
    private String text1 = "text1";
    private String text2 = "text2";
    
    private String publicKey;
    
    private String publicKey2;

    public String getPublicKey2() {
        return publicKey2;
    }

    public void setPublicKey2(String publicKey2) {
        this.publicKey2 = publicKey2;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    private String email = "anybody@example.com";

    private boolean checkboxValue;

    private Integer number = 10;
    private List<String> list = Arrays.asList("1", "2", "3", "4", "5", "6", "7");
    private String selectOne = "2";
    private String selectOneSize2 = "3";
    private List<String> selectMany = Arrays.asList("4", "6");
    private String longText = "Long text";

    private String lastAction;

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public boolean getCheckboxValue() {
        return checkboxValue;
    }

    public void setCheckboxValue(boolean checkboxValue) {
        this.checkboxValue = checkboxValue;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public void setSelectOne(String selectOne) {
        this.selectOne = selectOne;
    }

    public String getSelectOne() {
        return selectOne;
    }

    public void setSelectOneSize2(String selectOneSize2) {
        this.selectOneSize2 = selectOneSize2;
    }

    public String getSelectOneSize2() {
        return selectOneSize2;
    }

    public void setSelectMany(List<String> selectMany) {
        this.selectMany = selectMany;
    }

    public List<String> getSelectMany() {
        return selectMany;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }

    public String getLongText() {
        return longText;
    }

    public String action1() {
        lastAction = "action1";
        return null;
    }

    public String action2() {
        lastAction = "action2";
        return null;
    }

    public String getLastAction() {
        return lastAction;
    }

    private String min = "100";

    public void setMin(String min) {
        this.min = min;
    }

    public String getMin() {
        return min;
    }

    private String max = "500";

    public void setMax(String max) {
        this.max = max;
    }

    public String getMax() {
        return max;
    }
}
