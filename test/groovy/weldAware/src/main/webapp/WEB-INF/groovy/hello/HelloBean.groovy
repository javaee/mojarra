package hello

//import javax.faces.bean.ManagedBean
//import javax.faces.bean.ApplicationScoped

//@ManagedBean(name = "hello", eager = true)
//@ApplicationScoped
public class HelloBean implements java.io.Serializable {
    String fname;
  int age;

    public HelloBean() {
        System.out.println("HelloBean instantiated")
    }
    public String getMessage() {
        return "Hello " + fname + ", You are " + age + " years old. Happy Birthday!"
    }
    public String getFname() {
        return fname
    }
    public  void setFname(String name) {
        this.fname = name
    }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}