
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="testBean")
@SessionScoped
public class TestBean {
    
    private String field1;
    private String field2;
    private String field3;

    
    @PostConstruct
    public void init(){
        
        this.field1 = "DEFAULT OF FIELD 1";
        this.field2 = "DEFAULT OF FIELD 2";
        this.field3 = "DEFAULT OF FIELD 3";
        
    }
    
    
    private void log(String text) {
        Logger.getLogger("").log(Level.INFO,  text + "\n");
    }
    
    public String getField1() {
        this.log("<--- GETTER [field1] : value = " + this.field1);
        return this.field1;
    }

    public void setField1(String field1) {
        this.log("---> SETTER [field1] : value = " + field1);
        this.field1 = field1;
    }

    public String getField2() {
        this.log("<--- GETTER [field2] : value = " + this.field2);
        return this.field2;
    }

    public void setField2(String field2) {
        this.log("---> SETTER [field2] : value = " + field2);
        this.field2 = field2;
    }

    public String getField3() {
        this.log("<--- GETTER [field3] : value = " + this.field3);
        return this.field3;
    }

    public void setField3(String field3) {
        this.log("---> SETTER [field3] : value = " + field3);
        this.field3 = field3;
    }

    
    
}
