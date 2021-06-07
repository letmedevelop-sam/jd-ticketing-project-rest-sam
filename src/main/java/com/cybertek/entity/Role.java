package com.cybertek.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    private String description;

/*
We can delete this part
From User side we have @ManyToOne relationship

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)  //We dont need cascade, because roles are already loaded before user is created
    private List<User> users = new ArrayList<>();

 */
}
