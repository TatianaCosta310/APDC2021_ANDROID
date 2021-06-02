package pt.unl.fct.campus.firstwebapp.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    String email;
    String password;


    public LoginData(String email, String password){
        this.email = email;
        this.password = password;


    }



 public String getUsername(){
        return email;
    }

    public String getPassword() {
        return password;
    }


}
