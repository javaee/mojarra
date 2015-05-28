package com.sun.faces.test.javaee6web.facelets;

import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;

@FacesBehavior("my.customBehavior")
public class CustomBehavior
  extends ClientBehaviorBase
{
  public CustomBehavior()
  {
    super();
  }

  @Override
  public String getScript(
    ClientBehaviorContext clientBehaviorContext)
  {
    return "document.getElementById('textForBehavior').innerHTML='CustomBehavior called';";
  }
}
