package pt.unl.fct.campus.firstwebapp.data.model;

import pt.unl.fct.campus.firstwebapp.data.model.EventData;

public class EventData2 extends EventData {

    boolean owner, participating;
    String participants;
    int currentParticipants;

    public boolean isOwner() {
        return owner;
    }
    public void setOwner(boolean isOwner) {
        this.owner = isOwner;
    }

   // public int  getCurrentParticipants(){return currentParticipants;}
    public EventData2() {

        currentParticipants=0;
    }
}
