package com.cybertek.entity;

import com.cybertek.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "is_deleted=false")  //where is coming from HIBERNATE
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"}, ignoreUnknown = true)

public class Project extends BaseEntity{    //All fields need to MATCH projectDTO

    @Column(unique = true)      //This will not let the user to add same code to different project
    private String projectCode;

    private String projectName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User assignedManager;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status projectStatus;
    private String projectDetail;


}
