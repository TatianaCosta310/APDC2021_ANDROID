package pt.unl.fct.campus.firstwebapp.data.model;


public class LoginData {

    String name, email, profilePictureURL,password,token;

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public LoginData(String email,String password) {
        this.email = email;
        this.password = password;

    }

    public LoginData() {

    }


    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}