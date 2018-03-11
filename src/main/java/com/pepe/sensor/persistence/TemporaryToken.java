package com.pepe.sensor.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
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

    @OneToOne(targetEntity = Person.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "person_id")
    private Person person;

    @PrePersist
    protected void onCreate() {
        token = UUID.randomUUID().toString();
        expirationTimestamp = new Timestamp(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME_MILLIS);
    }

    public TemporaryToken() {
    }

    public TemporaryToken(Person person) {
        this.person = person;
    }

    public boolean hasExpired() {
        return expirationTimestamp.getTime() < System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(Timestamp expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
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
        if (!(object instanceof TemporaryToken)) {
            return false;
        }
        TemporaryToken other = (TemporaryToken) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pepe.sensor.persistence.ResetPasswordToken[ id=" + id + " ]";
    }

}
