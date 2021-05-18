package pt.unl.fct.campus.firstwebapp.data.model;

import android.net.wifi.hotspot2.pps.Credential;

public class UserCredentials {

    String username;
    String password;
    String email;
    String fixNumber;
    String mobileNumber;
    String address;
    String cAddress;

    String userType;

    public UserCredentials(String username, String password){
        this.username = username;
        this.password = password;


    }

    public UserCredentials(String username, String password,String email,String address,String cAddress,
                           String fixNumber, String mobileNumber, String userType){

        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.cAddress = cAddress;
        this.fixNumber = fixNumber;
        this.mobileNumber = mobileNumber;
        this.userType = userType;

    }

    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() { return address; }

    public String getcAddress() { return cAddress; }

    public String getFixNumber() { return fixNumber; }

    public String getMobileNumber() { return mobileNumber; }

    public String getUserType() { return userType; }
}
