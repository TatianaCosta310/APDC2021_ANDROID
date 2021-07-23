package pt.unl.fct.campus.firstwebapp.data.model;

import pt.unl.fct.campus.firstwebapp.data.Events.Coords;

public class EventLocationArgs {

    public String getName() {
        return name;
    }
    public Coords getLoc() {
        return loc;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setLoc(Coords loc) {
        this.loc = loc;
    }
    String name;
    Coords loc;
    /**
     * loc:{ lat: place.geometry.location.lat(), lng: place.geometry.location.lng() },
     name:getPlaceName(place),
     postal_code:postalCode.textContent,
     locality:locality.textContent,
     country_name:countryName.textContent
     */
    public EventLocationArgs() {
        // TODO Auto-generated constructor stub
    }

}

