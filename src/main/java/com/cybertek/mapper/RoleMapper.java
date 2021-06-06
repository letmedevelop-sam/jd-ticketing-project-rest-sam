package com.cybertek.mapper;

import com.cybertek.dto.RoleDTO;
import com.cybertek.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component     //create bean
public class RoleMapper {

    private ModelMapper modelMapper;

    //generate constructor
    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    //create a method to convert DTO to ENTITY
    public Role convertToEntity(RoleDTO dto){
        return modelMapper.map(dto, Role.class);
    }

    //create a method to convert ENTITY to DTO
    public RoleDTO convertToDto(Role entity){
        return modelMapper.map(entity, RoleDTO.class);
    }

}
