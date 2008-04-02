package j2meDemo.taglib;


public class OutputTag extends J2meComponentTag {   
   public String getComponentType() { return "javax.faces.Output"; }  
   public String getRendererType() { return "j2meDemo.renderkit.Text"; } 
}
