/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name="pageAjaxTest2")
public class PageAjaxTest  implements Serializable {
    
    private final NumberFormat format;

    public PageAjaxTest() {
        format = new DecimalFormat("#.##");
    }
    
    
  
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
  
  public String getRenderTime() {
      FacesContext context = FacesContext.getCurrentInstance();
      Long requestStart = (Long) context.getAttributes().get(TimerPhaseListener.REQUEST_START);
      double elapsedSeconds = ((double)(System.currentTimeMillis() - requestStart)) / 1000;
      return format.format(elapsedSeconds) + " seconds";
  }
  
  public void buttonSubmit() {
  }  
    
  }