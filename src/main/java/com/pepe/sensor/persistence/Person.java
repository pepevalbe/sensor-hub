package com.pepe.sensor.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NonNull
    @Size(min = 3, max = 20)
    @Column(length = 20)
    private String username;

    @NotNull
    @Column(nullable = false)
    private Timestamp creationTimestamp;

    @NotNull
    @NonNull
    @Size(min = 3, max = 60)
    @Column(nullable = false, length = 60)
    private String password;

    @NotNull
    @NonNull
    @Size(min = 1, max = 10)
    @Column(nullable = false, length = 10)
    private String role;

    @NotNull
    @NonNull
    @Size(min = 3, max = 100)
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotNull
    @NonNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String firstName;

    @NotNull
    @NonNull
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

    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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
}
