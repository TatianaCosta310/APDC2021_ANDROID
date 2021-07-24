package pt.unl.fct.campus.firstwebapp.data.model;

public class ChangeEmailArgs {


    String newEmail, password;
    public String getNewEmail() {
        return newEmail;
    }
    public String getPassword() {
        return password;
    }
    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public ChangeEmailArgs() {
        // TODO Auto-generated constructor stub
    }

}
