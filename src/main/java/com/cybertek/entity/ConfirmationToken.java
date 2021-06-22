package com.cybertek.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "confirmation_email")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause="is_deleted=false")
public class ConfirmationToken extends BaseEntity {

    private String token;

    @OneToOne(targetEntity = User.class)        // will make it uni-directional
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate expireDate;

    public Boolean isTokenValid(LocalDate date){
        LocalDate now = LocalDate.now();
        return date.isEqual(now) || date.isEqual(now.plusDays(1));  // one day valid token
    }

    public ConfirmationToken(User user){        //createed the constructor manually
        this.user=user;
        expireDate=LocalDate.now().plusDays(1);
        token= UUID.randomUUID().toString();        // we used UUID to create something unique
    }


}
