package com.cybertek.implementation;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.Project;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;
import com.cybertek.mapper.ProjectMapper;
import com.cybertek.mapper.UserMapper;
import com.cybertek.repository.ProjectRepository;
import com.cybertek.service.ProjectService;
import com.cybertek.service.TaskService;
import com.cybertek.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {


    private ProjectMapper projectMapper;
    private ProjectRepository projectRepository;
    private UserMapper userMapper;
    private UserService userService;
    private TaskService taskService;

    //create constructor for Injection
    public ProjectServiceImpl(ProjectMapper projectMapper, ProjectRepository projectRepository, UserMapper userMapper, UserService userService, TaskService taskService) {
        this.projectMapper = projectMapper;
        this.projectRepository = projectRepository;
        this.userMapper = userMapper;
        this.userService = userService;
        this.taskService = taskService;
    }

    //Implement overridden methods
    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code);
        return projectMapper.convertToDto(project);
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> list = projectRepository.findAll(Sort.by("projectCode"));
        return list.stream().map(obj ->{
            return projectMapper.convertToDto(obj);
        }).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        // There is no field to set Project Status in our Form. When we start project it will automatically be OPEN
        dto.setProjectStatus(Status.OPEN);

        //When data entered to UI a DTO will come and we will convert it to ENTITY
        Project obj = projectMapper.convertToEntity(dto); //constructor injection needed to use this method from ProjectMapper

        //we need to assign manager //when one object has dependency on other object we need to make this kind of conversion
        obj.setAssignedManager(userMapper.convertToEntity(dto.getAssignedManager())); //constructor injection needed to use this method from UserMapper

        //we need to SAVE the changes
        Project project = projectRepository.save(obj); //constructor injection needed to use this method from ProjectRepository

    }

    @Override
    public void update(ProjectDTO dto) {
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());   //retrieve project from DB
        Project convertedProject = projectMapper.convertToEntity(dto);                  // convert it to ENTITY

        //If we dont get the ID it will create a new project with a new ID
        convertedProject.setId(project.getId());        //DTO does not have id, we get and assign the project ID to  DTO

        convertedProject.setProjectStatus(project.getProjectStatus());  //we need to retrieve the current STATUS of the Project
        projectRepository.save(convertedProject);       //save it to our repository
    }

    @Override
    public void delete(String code) {
        Project project=projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);

        //set a different project code after delete
        project.setProjectCode(project.getProjectCode()+ "-" + project.getId());

        projectRepository.save(project);

        //after delete project work on task
        taskService.deleteByProject(projectMapper.convertToDto(project));


    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {
        //next line will be gone by we learn SECURITY
        UserDTO currentUserDTO = userService.findByUserName("samikaratas2000@gmail.com");

        User user = userMapper.convertToEntity(currentUserDTO);

        List<Project> list = projectRepository.findAllByAssignedManager(user);
        return list.stream().map(project -> {
            ProjectDTO obj = projectMapper.convertToDto(project);
            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTasks(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTasks(project.getProjectCode()));
            return obj;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> readAllByAssignedManager(User user) {

        List<Project> list = projectRepository.findAllByAssignedManager(user);
        return list.stream().map(obj ->projectMapper.convertToDto(obj)).collect(Collectors.toList());
    }
}
