/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package beanvalidation;

import beanvalidation.constraints.CreditCard;
import beanvalidation.constraints.Email;
import beanvalidation.groups.Order;
import beanvalidation.groups.Personal;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

@SessionScoped
@ManagedBean(name = "user")
public class UserBean implements Serializable {
    private String lastName;
    private String firstName;
    private String emailAddress;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String creditCard;

    @NotNull(groups = Order.class)
    @Size(min = 1, message = "{validator.notEmpty}", groups = Order.class)
    @CreditCard(groups = Order.class)
    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    @NotNull(groups = Order.class)
    @Size(min = 1, message = "{validator.notEmpty}", groups = Order.class)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @NotNull
    @Size(min = 1, message = "{validator.notEmpty}")
    @Email
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @NotNull(groups = {Default.class, Personal.class})
    @Size(min = 1,
          message = "{validator.notEmpty}",
          groups = {Default.class, Personal.class})
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull(groups = {Default.class, Personal.class})
    @Size(min = 1,
          message = "{validator.notEmpty}",
          groups = {Default.class, Personal.class})
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotNull(groups = Order.class)
    @Size(min = 1, message = "{validator.notEmpty}", groups = Order.class)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @NotNull(groups = Order.class)
    @Size(min = 1, message = "{validator.notEmpty}", groups = Order.class)
    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    @NotNull(groups = Order.class)
    @Pattern(regexp = "[0-9]+",
             message = "{validator.numbers}",
             groups = Order.class)
    @Size(min = 5, max = 5, groups = Order.class)
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
