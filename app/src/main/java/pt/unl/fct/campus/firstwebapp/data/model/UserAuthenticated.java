package pt.unl.fct.campus.firstwebapp.data.model;

public class UserAuthenticated {

    String username;
    String tokenID;
    String role;
    long creationData;
    long expirationData;

    public  UserAuthenticated( String username, String tokenID,String role,long creationData,long expirationData){
        this.username = username;
        this.tokenID = tokenID;
        this.role = role;
        this.creationData = creationData;
        this.expirationData = expirationData;
    }

    public String getUsername() {
        return username;
    }

    public String getTokenID() {
        return tokenID;
    }

    public String getRole() {
        return role;
    }

    public long getCreationData() {
        return creationData;
    }

    public long getExpirationData() {
        return expirationData;
    }
}
