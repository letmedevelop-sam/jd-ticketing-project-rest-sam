package com.cybertek.converter;


import com.cybertek.dto.UserDTO;
import com.cybertek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding //we will never call any methods from here. Annotation will bind it automatically
public class  UserDtoConverter implements Converter<String, UserDTO> {


    @Autowired
    UserService userService;


    @Override
    public UserDTO convert(String source) { //assigned manager did not come in Project Create Form
        //Manager was coming from Table Field as OBJECT
        //We needed String     --th:value--
        //That's why we created this converter

        return userService.findByUserName(source);
    }

}
