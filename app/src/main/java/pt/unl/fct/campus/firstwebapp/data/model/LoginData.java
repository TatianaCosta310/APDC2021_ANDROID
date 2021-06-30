package pt.unl.fct.campus.firstwebapp.data.model;


public class LoginData {

    String email;
    String password;
    String token;
    String profilePicUrl;


    public LoginData(String email, String password ){
        this.email = email;
        this.password = password;

    }



 public String getUsername(){
        return email;
    }

    public String getPassword() {
        return password;
    }


    public void setToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setProfilePicUrl(String url){
        profilePicUrl = url;

    }

    public String getProfilePicUrl(){
        return profilePicUrl;
    }

}
