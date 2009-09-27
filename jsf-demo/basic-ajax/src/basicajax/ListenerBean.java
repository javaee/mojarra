package basicajax;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

@ManagedBean(name="listenBean")
@ViewScoped
public class ListenerBean {

    private String hello = "Hello";

    private int length = hello.length();

    private int eventCount = 0;

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public int getLength() {
        return length;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void update(AjaxBehaviorEvent event) {
        length = hello.length();
        eventCount++;
    }
}
