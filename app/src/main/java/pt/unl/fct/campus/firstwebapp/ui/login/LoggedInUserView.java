package pt.unl.fct.campus.firstwebapp.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String token;
    private Object a;
    private String profile_pic;
    private String vCode;
    private String email;
    private String role;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String email,String displayName,String token,String profile_pic) {

        this.email = email;
        this.displayName = displayName;
        this.token = token;
        this.profile_pic = profile_pic;
    }

    LoggedInUserView(Object a) {
        this.displayName = displayName;
        this.token = token;
        this.a = a;
    }


    LoggedInUserView() {

    }


    public void setRole(String role){
        this.role=role;
    }
    public String getRole(){
        return role;
    }


   public String getDisplayName() {
        return displayName;
    }
    public String getToken(){return token;}
    public String getProfile_pic(){return  profile_pic;}

    public String getEmail(){
        return email;
    }
    public  void setvCode(String vCode){
        this.vCode = vCode;
    }

    String getvCode(){
        return  vCode;
    }

     Object getObject() {
        return a;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}