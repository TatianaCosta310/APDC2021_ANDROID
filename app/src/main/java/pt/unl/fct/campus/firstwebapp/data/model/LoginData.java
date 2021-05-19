package pt.unl.fct.campus.firstwebapp.data.model;

public class LoginData {

    String username;
    String password;


    public LoginData(String username, String password){
        this.username = username;
        this.password = password;


    }



    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }


}
