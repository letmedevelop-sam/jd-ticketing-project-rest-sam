package com.cybertek.entity;

import com.cybertek.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@Where(clause = "is_deleted=false")  //This will add all queries "where "is deleted = false

public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    private String userName;
    private String passWord;
    private boolean enabled;    //We did not need it while it was coming from DB
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne      //we deleted         (fetch = FetchType.LAZY)  it will come EAGER as default
    @JoinColumn(name = "role_id")
    private Role role;


}
