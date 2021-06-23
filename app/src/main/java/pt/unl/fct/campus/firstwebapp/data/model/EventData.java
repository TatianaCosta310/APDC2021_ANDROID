package pt.unl.fct.campus.firstwebapp.data.model;

import android.icu.util.ULocale;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public class EventData {

   /*String name, description, goals, location,
            meetingPlace, startDate, endDate, startTime, endTime;

    long volunteers;

    MultipartBody.Part images;

    public EventData(){}

    public EventData(String name, String description, String location,
                     String meetingPlace, String startDate, String endDate,
                     String startTime, String endTime,long volunteers, MultipartBody.Part images){

        this.name = name;
        this.description = description;
        this.location = location;
        this.meetingPlace = meetingPlace;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.images = images;
        this.volunteers = volunteers;
    }
*/

   /* public void setName(String name){ this.name = name;}

    public void setDescription(String description){this.description = description;}

    public void setGoals(String goals){this.goals = goals;}

    public void setLocation(String location) {this.location = location;}

    public void  setMeetingPlace(String meetingPlace){this.meetingPlace = meetingPlace;}

    public void setStartDate (String startDate){this.startDate = startDate;}

    public void setEndDate (String endDate){this.endDate = endDate;}

    public void setStartTime(String startTime){this.startTime = startTime;}

    public  void setEndTime(String endTime){this.endTime = endTime;}

    public void setVolunteers(long volunteers){this.volunteers = volunteers;}

    public void setImages(MultipartBody.Part images){this.images = images;}
    public String getStartTime() {
        return startTime;
    }


    public String getEndTime() {
        return endTime;
    }


    public long getVolunteers() {
        return volunteers;
    }

    public MultipartBody.Part getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }*/


    String name, description, goals, location,
            meetingPlace, startDate, endDate, organizer, startTime, endTime, images;
    long eventId, volunteers;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(long volunteers) {
        this.volunteers = volunteers;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }


    public EventData() {}

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        //String unsafe = "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";
        //String safe = Jsoup.clean(unsafe, Whitelist.basic());
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getGoals() {
        return goals;
    }
    public void setGoals(String goals) {
        this.goals = goals;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getMeetingPlace() {
        return meetingPlace;
    }
    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}