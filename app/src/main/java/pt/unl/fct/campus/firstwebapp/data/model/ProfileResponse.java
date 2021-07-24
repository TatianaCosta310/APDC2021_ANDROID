package pt.unl.fct.campus.firstwebapp.data.model;

public class ProfileResponse extends AdditionalAttributes {

    String profilePicture, name, email;
    double participationScore;

    public double getParticipationScore() {
        return participationScore;
    }

    public void setParticipationScore(double participationScore) {
        this.participationScore = participationScore;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    boolean viewingOwnProfile;

    public ProfileResponse() {
        // TODO Auto-generated constructor stub
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getName() {
        return name;
    }

    public boolean isViewingOwnProfile() {
        return viewingOwnProfile;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setViewingOwnProfile(boolean viewingOwnProfile) {
        this.viewingOwnProfile = viewingOwnProfile;
    }
}
