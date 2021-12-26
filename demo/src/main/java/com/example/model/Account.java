package com.example.model;

import com.sun.istack.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.util.Date;

@Entity
@Validated
public class Account {

    //to use domain key is more user friendly, the change of the key data it self could be handled by specific logic if necessary such as changeover
    @Id
    @NotNull
    @Column(name = "identity_id", unique = true, nullable = false)
    private String identityid;

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdentityid() {
        return identityid;
    }

    public void setIdentityid(String identityId) {
        this.identityid = identityId;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
