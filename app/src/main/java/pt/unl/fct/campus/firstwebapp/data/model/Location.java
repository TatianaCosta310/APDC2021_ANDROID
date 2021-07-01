package pt.unl.fct.campus.firstwebapp.data.model;

public class Location {

    String place_id;
    LooClass loc;
    String name;

    public Location(){}

    public Location(String place_id,String name,LooClass loc){
        this.place_id = place_id;
        this.name = name;
        this.loc = loc;

    }

}
