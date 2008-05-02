package model;

public class Bean {
    
    private String message = "Hello World!";
    
    public String getMessage() {
        return message;
    }
    
    public String loginAction() {
        return "login";
    }

    public String backAction() {
        return "back";
    }
}
