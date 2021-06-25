package com.cybertek.implementation;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.User;
import com.cybertek.exception.TicketingProjectException;

import com.cybertek.repository.UserRepository;
import com.cybertek.service.ProjectService;
import com.cybertek.service.TaskService;
import com.cybertek.service.UserService;
import com.cybertek.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ProjectService projectService;
    private TaskService taskService;
    private MapperUtil mapperUtil;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, @Lazy ProjectService projectService, TaskService taskService, MapperUtil mapperUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectService = projectService;
        this.taskService = taskService;
        this.mapperUtil = mapperUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> list = userRepository.findAll(Sort.by("firstName"));

        return list.stream().map(obj -> mapperUtil.convert(obj, new UserDTO())).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) throws AccessDeniedException {        //this findByUserName is for Controller use

        //bring the data
        User user = userRepository.findByUserName(username);  //this findByUserName will be called from UserRepository

        checkForAuthorities(user);

        //Convert the data which came as ENTITY to DTO
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public UserDTO save(UserDTO dto) throws TicketingProjectException {

        User foundUser = userRepository.findByUserName(dto.getUserName());

        if(foundUser!=null){
            throw new TicketingProjectException("User already exists");
        }

        User user =  mapperUtil.convert(dto,new User());
        //we will encode the password before we save
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));

        User save = userRepository.save(user);

    return mapperUtil.convert(save,new UserDTO());
    }

    //UPDATE
    @Override
    public UserDTO update(UserDTO dto) throws TicketingProjectException, AccessDeniedException { //This dto will be the updated one

        //Find the current user  // THIS user is NOT UPDATED yet
        User user = userRepository.findByUserName(dto.getUserName()); //We dont know the ID. We will bring it from DTO

            //add exception
        if(user == null){
            throw new TicketingProjectException("User Does Not Exist");
        }

        //Map update user DTO to ENTITY object
        User convertedUser = mapperUtil.convert(user, new User()); // This is updated one but again there is no ID because dto has no ID

        //After update password is not encrypted
        convertedUser.setPassWord(passwordEncoder.encode(convertedUser.getPassWord())); //now password is encoded

        if (!user.getEnabled()){
            throw  new TicketingProjectException("User is NOT confirmed");
        }

        //check if user has authority to do so

        checkForAuthorities(user);

        convertedUser.setEnabled(true);

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
        return users.stream().map(obj -> {return mapperUtil.convert(obj,new UserDTO());}).collect(Collectors.toList());
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

    @Override
    public UserDTO confirm(User user) {
        user.setEnabled(true);
        User confirmedUser = userRepository.save(user);

        return mapperUtil.convert(confirmedUser, new UserDTO());
    }


    private void checkForAuthorities(User user) throws AccessDeniedException {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !authentication.getName().equals("anonymousUser")){

            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

            if(!(authentication.getName().equals(user.getId().toString())) || roles.contains("Admin")){

                throw new AccessDeniedException("Access is Denied");
            }
        }


    }


}

