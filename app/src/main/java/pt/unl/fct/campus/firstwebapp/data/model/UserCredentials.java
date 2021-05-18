package pt.unl.fct.campus.firstwebapp.data.model;

import android.net.wifi.hotspot2.pps.Credential;

public class UserCredentials {

    String username;
    String password;

    public UserCredentials(String username, String Password){
        this.username = username;
        this.password = getPassword();


    }
    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }
}
