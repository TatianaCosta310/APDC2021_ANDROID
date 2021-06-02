package pt.unl.fct.campus.firstwebapp.data.model;

public class RegisterData   {

    String name;
    String password;
    String email;


    public RegisterData(String name, String password, String email){

        this.name = name;
        this.password = password;
        this.email = email;
    }


    public String getUsername(){
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }


}
