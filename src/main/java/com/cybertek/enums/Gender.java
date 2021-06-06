package com.cybertek.enums;

public enum Gender {

    MALE("Male"),FEMALE("Female"); //This will let you see the value of gender in regular String format

    private final String value;

    Gender(String value){
        this.value=value;
    }

    public String getValue(){
        return value;
    }

}
