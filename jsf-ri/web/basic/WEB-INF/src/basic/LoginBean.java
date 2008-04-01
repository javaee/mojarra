package basic;

public class LoginBean {

    String userName = null;
    String password = null;

    public LoginBean () {
        System.out.println("Model Object Created");
    }
  
    public void setUserName(String user_name) {
        userName = user_name;
        System.out.println("Set userName " + userName);
    }

    public String getUserName() {
        System.out.println("get userName " + userName);
        return userName;
    }

   public void setPassword(String pwd) {
        System.out.println("Set Password " + password);
        password = pwd;
    }

    public String getPassword() {
        System.out.println("get Password " + password);
        return password;
    }

}
