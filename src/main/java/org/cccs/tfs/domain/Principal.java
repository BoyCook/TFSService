package org.cccs.tfs.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.cccs.tfs.service.Unique;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Craig Cook
 * Date: Apr 1, 2010
 * Time: 6:52:43 PM
 */
@Entity
@Table(name = "principals")
@Unique(entity = Principal.class, idField = "id", uniqueFields = {"shortName"}, message = "Short Name must be unique")
@XStreamAlias("principal")
public class Principal {

    private long id;
    private String shortName;
    private String foreName;
    private String surName;
    private String password;
    private String email;
    private String phoneNumber;
    private Location location;
    private boolean active;
    private double distance;
    private Set<Principal> friends;
    private Set<Principal> friendsOf;
    private Set<Principal> friendRequests;

    public Principal() {
        this(null);
    }

    public Principal(String shortName) {
        this(shortName, null, null, null, null, null);
    }

    public Principal(String shortName, String password) {
        this(shortName, null, null, password, null, null);
    }

    public Principal(String shortName, String foreName, String surName, String password, String email, String phoneNumber) {
        this(shortName, foreName, surName, password, email, phoneNumber, null);
    }

    public Principal(String shortName, String foreName, String surName, String password, String email, String phoneNumber, Location location) {
        setShortName(shortName);
        setForeName(foreName);
        setSurName(surName);
        setPassword(password);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setLocation(location);
        setFriends(new HashSet<Principal>());
        setFriendsOf(new HashSet<Principal>());
        setFriendRequests(new HashSet<Principal>());
    }

	@Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE)
    public long getId() {
        return id;
    }

    @NotEmpty(message = "Short name cannot be empty")
    @Column(unique = true, nullable = false)
    public String getShortName() {
        return shortName;
    }

    @NotEmpty(message = "Forename cannot be empty")
    @Column(nullable = false)
    public String getForeName() {
        return foreName;
    }

    @NotEmpty(message = "Surname cannot be empty")
    @Column(nullable = false)
    public String getSurName() {
        return surName;
    }

    @NotEmpty(message = "Password cannot be empty")
    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    @NotEmpty(message = "Email cannot be empty")
    @Column(nullable = false)
    public String getEmail() {
        return email;
    }

    @NotEmpty(message = "Phone number cannot be empty")
    @Column(nullable = false)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="friends", joinColumns = { @JoinColumn( name="principal_id") }, inverseJoinColumns = @JoinColumn( name="friend_id"))
    public Set<Principal> getFriends() {
        return this.friends;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="friends", joinColumns = { @JoinColumn( name="friend_id") }, inverseJoinColumns = @JoinColumn( name="principal_id"))
    public Set<Principal> getFriendsOf() {
        return this.friendsOf;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="friendrequests")
    public Set<Principal> getFriendRequests() {
        return friendRequests;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Location getLocation() {
        return location;
    }

    @Transient
    public String getFullName() {
        return getName() + " (" + getShortName() + ")";
    }

    @Transient
    public String getName() {
        return getForeName() + " " + getSurName();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getDistance() {
        return distance;
    }

    @Transient
    public void setDistance(double distance) {
        //This is generated by a getter
        this.distance = distance;
    }

    public void addFriend(Principal friend) {
        friends.add(friend);
        friendsOf.add(friend);
    }

    public void removeFriend(Principal friend) {
        friends.remove(friend);
        friendsOf.remove(friend);
    }

    public void addFriendRequest(Principal friend) {
        friendRequests.add(friend);
    }

    public void removeFriendRequest(Principal friend) {
        friendRequests.remove(friend);
    }

    public void setFriends(Set<Principal> friends) {
        this.friends = friends;
    }

    public void setFriendsOf(Set<Principal> friendsOf) {
        this.friendsOf = friendsOf;
    }

    public void setFriendRequests(Set<Principal> friendRequests) {
        this.friendRequests = friendRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Principal principal = (Principal) o;
        return id == principal.id && shortName.equals(principal.shortName);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + shortName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Principal{" +
                "id=" + id +
                ", shortName='" + shortName + '\'' +
                ", foreName='" + foreName + '\'' +
                ", surName='" + surName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
