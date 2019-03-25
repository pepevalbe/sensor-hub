package com.pepe.sensor.persistence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * TemporayToken entity
 *
 * This class represents a token that can be used for user activation or
 * password reset. Tokens are UUID and have expiration time. They have OneToOne
 * relationship with person.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class TemporaryToken implements Serializable {

    // Expiration time set to 24h
    private static final long TOKEN_EXPIRATION_TIME_MILLIS = 24 * 60 * 60 * 1000L;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 36, max = 36)
    @Column(nullable = false, length = 36)
    private String token;

    @NotNull
    @Column(nullable = false)
    private Timestamp expirationTimestamp;

    // Fetch is EAGER by default in ToOne relationship (LAZY in ToMany)
    @OneToOne
    @JoinColumn(nullable = false, name = "person_id")
    private Person person;

    public TemporaryToken(Person person) {
        this.person = person;
    }

    @PrePersist
    protected void onCreate() {
        token = UUID.randomUUID().toString();
        expirationTimestamp = new Timestamp(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME_MILLIS);
    }

    public boolean hasExpired() {
        return expirationTimestamp.getTime() < System.currentTimeMillis();
    }

}
