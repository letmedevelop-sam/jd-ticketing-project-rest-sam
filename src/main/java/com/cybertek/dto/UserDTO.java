package com.cybertek.dto;

import com.cybertek.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"}, ignoreUnknown = true)

public class UserDTO {

    private Long id;   // to assign manually an id as manager_id

    private String  firstName;
    private String  lastName;
    private String  userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String  passWord;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassWord;     //we added to make sure two passwords are not concatenated

    private boolean  enabled;
    private String  phone;
    private RoleDTO  role;   //create Role DTO
    private Gender  gender;  // import
}
