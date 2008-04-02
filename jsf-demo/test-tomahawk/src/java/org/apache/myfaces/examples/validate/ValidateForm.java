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
package org.apache.myfaces.examples.validate;

/**
 * @author mwessendorf
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:45 $
 */
public class ValidateForm {

	private String email = null;
	private String email2 = null;
	private String creditCardNumber = null;
	private String url = null;
	private String regExpr = null;

	private String equal = null;
	private String equal2 = null;

	private String isbn =null;


	public String getEmail() {
		return email;
	}

	public void setEmail(String string) {
		email = string;
	}

	public String submit(){
		System.out.println("Action was called.");
		return ("valid");
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public String getUrl(){
		return url;
	}
	public void setCreditCardNumber(String string) {
		creditCardNumber = string;
	}
	public void setUrl(String string) {
		url = string;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String string) {
		email2 = string;
	}

	/**
	 * @return
	 */
	public String getRegExpr() {
		return regExpr;
	}

	/**
	 * @param string
	 */
	public void setRegExpr(String string) {
		regExpr = string;
	}

	/**
	 * @return
	 */
	public String getEqual2() {
		return equal2;
	}

	/**
	 * @param string
	 */
	public void setEqual2(String string) {
		equal2 = string;
	}

	/**
	 * @return
	 */
	public String getEqual() {
		return equal;
	}

	/**
	 * @param string
	 */
	public void setEqual(String string) {
		equal = string;
	}

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
