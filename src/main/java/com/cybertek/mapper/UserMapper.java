package com.cybertek.mapper;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component // create bean to be managed by the container
public class UserMapper {

    private ModelMapper modelMapper;

    //generate constructor for INJECTION
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //ENTITY fieldNames MUST match DTO fieldNames //If they dont match NULL will be assigned to not matching ones

    //create a method to convert DTO to ENTITY
    public User convertToEntity(UserDTO dto){
        return modelMapper.map(dto, User.class);
    }

    //create a method to convert ENTITY to DTO
    public UserDTO convertToDto(User entity){
        return modelMapper.map(entity, UserDTO.class);
    }

}
