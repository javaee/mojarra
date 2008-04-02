package j2meDemo.taglib;

public class FormTag extends J2meComponentTag {   
   public String getComponentType() { return "javax.faces.Form"; }  
   public String getRendererType() { return "j2meDemo.renderkit.Form"; } 
}
