package com.cybertek.implementation;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.User;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.mapper.UserMapper;
import com.cybertek.repository.UserRepository;
import com.cybertek.service.ProjectService;
import com.cybertek.service.TaskService;
import com.cybertek.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private ProjectService projectService;
    private TaskService taskService;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, TaskService taskService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> list = userRepository.findAll(Sort.by("firstName"));

        //convert to DTO  //create a  new mapper class

        return list.stream().map(obj ->{return userMapper.convertToDto(obj);}).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {        //this findByUserName is for Controller use

        //bring the data // return type will be ENTITY
        User user = userRepository.findByUserName(username);  //this findByUserName will be called from UserRepository

        //Convert the data which came as ENTITY to DTO
        return userMapper.convertToDto(user);
    }

    @Override
    public void save(UserDTO dto) {

        //We set the user as enabled before we save it
        //If there is an email confirmation etc it will stay disabled
        User foundUser = userRepository.findByUserName(dto.getUserName());
        dto.setEnabled(true);

        //need to convert DTO to ENTITY
        User obj = userMapper.convertToEntity(dto);

        //we will encode the password before we save
        obj.setPassWord(passwordEncoder.encode(obj.getPassWord()));
        userRepository.save(obj);
    }

    //UPDATE
    @Override
    public UserDTO update(UserDTO dto) { //This dto will be the updated one

        //Find the current user  // THIS user is NOT UPDATED yet
        User user = userRepository.findByUserName(dto.getUserName()); //We dont know the ID. We will bring it from DTO

        //Map update user DTO to ENTITY object
        User convertedUser = userMapper.convertToEntity(dto); // This is updated one but again there is no ID because dto has no ID

        //set ID to the converted object
        convertedUser.setId(user.getId());

        //save the updated user
        userRepository.save(convertedUser);

        return findByUserName(dto.getUserName());
    }

    //We will not physically delete from DB but change isDeleted=true
    //From User ENTITY we used where clause to filter all queries to check isDeleted=false
    @Override
    public void delete(String username) throws TicketingProjectException {
        User user = userRepository.findByUserName(username);

        //add exception
        if(user == null){
            throw new TicketingProjectException("User Does Not Exist");
        }

        if(!checkIfUserCanBeDeleted(user)){
            throw new TicketingProjectException("User can not be deleted. It is linked to a Project or Task");
        }

        //to let the same user name to be used next time:
        user.setUserName(user.getUserName() + "-" +  user.getId());

        user.setIsDeleted(true);
        userRepository.save(user);

    }

    //HARD DELETE is deleting from DB as well BUT it is not recommended
    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);
    }


    @Override
    public List<UserDTO> listAllByRole(String role) {

        List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);

        return users.stream().map(obj -> {return  userMapper.convertToDto(obj);}).collect(Collectors.toList());
    }

    @Override
    public Boolean checkIfUserCanBeDeleted(User user) {

        switch (user.getRole().getDescription()){
            case "Manager":
                List<ProjectDTO> projectList =projectService.readAllByAssignedManager(user);             //go and bring all projects assigned to this manager
                return projectList.size() == 0; //method is Boolean - if it is O it will return True
            case "Employee":
                List<TaskDTO> taskList = taskService.readAllByEmployee(user);
                return taskList.size() == 0; //method is Boolean - if it is not O it will return False
            default:
                return true;
        }
    }

}
