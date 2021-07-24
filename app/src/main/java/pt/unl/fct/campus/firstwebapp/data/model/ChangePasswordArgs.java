package pt.unl.fct.campus.firstwebapp.data.model;

public class ChangePasswordArgs {

    String email, password, vcode;

    public String getVcode() {
        return vcode;
    }
    public ChangePasswordArgs() {
        // TODO Auto-generated constructor stub
    }

    public ChangePasswordArgs(String email,String password, String vcode) {
        this.email = email;
        this.password = password;
        this.vcode = vcode;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
