package pt.unl.fct.campus.firstwebapp.data.model;

public class AdditionalAttributes {

    String fixNumber;
    String mobileNumber;
    String address;
    String cAddress;
    String locality;

    String userType;

    public AdditionalAttributes(String userType, String fixNumber, String mobileNumber, String address, String cAddress,String locality){

        this.userType = userType;
        this.fixNumber = fixNumber;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.cAddress = cAddress;
        this.locality = locality;
    }

    public String getUserType() { return userType; }

    public String getAddress() { return address; }

    public String getcAddress() { return cAddress; }

    public String getFixNumber() { return fixNumber; }

    public String getMobileNumber() { return mobileNumber; }

    public String getLocality() { return locality; }
}
