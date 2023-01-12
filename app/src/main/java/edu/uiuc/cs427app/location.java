package edu.uiuc.cs427app;

public class location
{
    // Variable to store data corresponding to locationName keyword in database
    private String locationName;
    private Double locationLatitude;
    private Double locationLongitude;

    /**
     *  Mandatory empty constructor for use of FirebaseUI
     */
    public location() {}
    /**
     *  Getter and setter methods for Location (City) name
     */
    public String getLocationName()
    {
        return locationName;
    }
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    /**
     *  Getter and setter methods for latitude of city
     */

    // Getter and setter methods for latitude
    public Double getLocationLatitude() {
        return locationLatitude;
    }
    public void setLocationLatitude(Double locationLatitude) {this.locationLatitude = locationLatitude;}

    /**
     *  Getter and setter methods for longitude of city
     */

    // Getter and setter methods for longitude
    public Double getLocationLongitude() {
        return locationLongitude;
    }
    public void setLocationLongitude(Double locationLongitude) {this.locationLongitude = locationLongitude;}
}
