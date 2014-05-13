/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.Serializable;
import javax.faces.bean.*;

@SessionScoped
@ManagedBean(name="pageAjaxTest2")
public class PageAjaxTest  implements Serializable {
  
  public void setInputTextValue(String textVal){}
    
  public String getInputTextValue() {
    String result = "test test test test";    

    return(result);
  }
  
  public String getRandomNumber() {
    Double x= Math.random() * 100;
    String retVal = x.toString();
    return retVal;
 }
  
  public void buttonSubmit() {
  }  
    
  }