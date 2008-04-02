package j2meDemo.taglib;


public class InputTag extends J2meComponentTag {   
   public String getComponentType() { return "javax.faces.Input"; }  
   public String getRendererType() { return "j2meDemo.renderkit.Text"; } 
}
