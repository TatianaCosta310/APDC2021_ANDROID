package pt.unl.fct.campus.firstwebapp.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String token;
    private Object a;
    private String profile_pic;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName,String token,String profile_pic) {
        this.displayName = displayName;
        this.token = token;
        this.profile_pic = profile_pic;
    }

    LoggedInUserView(Object a) {
        this.displayName = displayName;
        this.token = token;
        this.a = a;
    }

    String getDisplayName() {
        return displayName;
    }
    String getToken(){return token;}
    String getProfile_pic(){return  profile_pic;}

     Object getObject() {
        return a;
    }
}