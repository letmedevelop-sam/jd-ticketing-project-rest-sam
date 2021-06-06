package com.cybertek.mapper;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.entity.Project;
import com.cybertek.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component // create bean to be managed by the container
public class ProjectMapper {
    //Will retrieve data from DataBase
    //Controller will talk to Service
    //We will need to convert DTO - ENTITY - DTO

    private ModelMapper modelMapper;
    private ProjectRepository projectRepository;

    public ProjectMapper(ModelMapper modelMapper, ProjectRepository projectRepository) {
        this.modelMapper = modelMapper;
        this.projectRepository = projectRepository;
    }

    public Project convertToEntity(ProjectDTO dto){
        return modelMapper.map(dto, Project.class);
         }

    public ProjectDTO convertToDto(Project entity){
        return modelMapper.map(entity, ProjectDTO.class);
    }


}
