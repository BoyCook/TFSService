package org.cccs.tfs.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.persistence.*;

/**
 * User: boycook
 * Date: 18/02/2011
 * Time: 19:36
 */
@Entity
@Table( name = "locations" )
@XStreamAlias("location")
public class Location {

    private long id;
    private double latitude;
    private double longitude;

    public Location() {
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String longitude() {
        return String.valueOf(this.longitude);
    }

    public String latitude() {
        return String.valueOf(this.latitude);
    }

    @Override
    public String toString() {
        return "Location{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
