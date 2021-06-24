package pt.unl.fct.campus.firstwebapp.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String token;
    private Object a;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName,String token) {
        this.displayName = displayName;
        this.token = token;
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

     Object getObject() {
        return a;
    }
}