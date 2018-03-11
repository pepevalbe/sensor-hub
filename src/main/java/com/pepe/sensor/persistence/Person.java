package com.pepe.sensor.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Timestamp creationTimestamp;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @NotNull
    @Size(min = 3, max = 60)
    @Column(nullable = false, length = 60)
    private String password;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(nullable = false, length = 10)
    private String role;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String lastName;

    @NotNull
    @Size(min = 36, max = 36)
    @Column(nullable = false, length = 36)
    private String token;

    @NotNull
    @Column(nullable = false)
    private boolean activated;

    @NotNull
    @Column(nullable = false)
    private boolean doorRegisterActiveFlag;

    @OneToOne(targetEntity = TemporaryToken.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(nullable = true, name = "resetPasswordToken_id")
    private TemporaryToken temporaryToken;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoorEvent> doorEvent;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TempHumidity> tempHumidity;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorReading> sensorReading;

    @PrePersist
    protected void onCreate() {
        creationTimestamp = new Timestamp(System.currentTimeMillis());
        token = UUID.randomUUID().toString();
        activated = false;
        doorRegisterActiveFlag = true;
    }

    public Person() {
    }

    public Person(Long id) {
        this.id = id;
    }

    public Person(String username, String password, String role, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
    
    public Boolean getDoorRegisterActiveFlag() {
        return doorRegisterActiveFlag;
    }

    public void setDoorRegisterActiveFlag(Boolean doorRegisterActiveFlag) {
        this.doorRegisterActiveFlag = doorRegisterActiveFlag;
    }

    public TemporaryToken getTemporaryToken() {
        return temporaryToken;
    }

    public void setTemporaryToken(TemporaryToken temporaryToken) {
        this.temporaryToken = temporaryToken;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pepe.sensor.persistence.User[ id=" + id + " ]";
    }

}
