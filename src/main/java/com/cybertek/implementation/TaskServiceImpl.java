package com.cybertek.implementation;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.Task;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;
import com.cybertek.mapper.ProjectMapper;
import com.cybertek.mapper.TaskMapper;
import com.cybertek.repository.TaskRepository;
import com.cybertek.repository.UserRepository;
import com.cybertek.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;
    TaskMapper taskMapper;
    ProjectMapper projectMapper;
    UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userRepository = userRepository;
    }



    @Override
    public TaskDTO findByID(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            return taskMapper.convertToDto(task.get());
        }
        return null;
    }


    //list all existing TASKs
    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> list = taskRepository.findAll();

        return list.stream().map(taskMapper::convertToDto).collect(Collectors.toList()); //method reference

        //double colon operator came with Java 8
        //return list.stream().map(obj ->{return taskMapper.convertToDto(obj);}).collect(Collectors.toList());

    }

    @Override
    public Task save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);                 //add STATUS
        dto.setAssignedDate(LocalDate.now());           //add assign date
        Task task = taskMapper.convertToEntity(dto);    //CONVERT it
        return taskRepository.save(task);               //SAVE it
    }

    @Override
    public void update(TaskDTO dto) {
        Optional<Task> task = taskRepository.findById(dto.getId());
        Task convertedTask = taskMapper.convertToEntity(dto);

        if(task.isPresent()){
            convertedTask.setId(task.get().getId());
            convertedTask.setTaskStatus(task.get().getTaskStatus());
            convertedTask.setAssignedDate(task.get().getAssignedDate());
            taskRepository.save(convertedTask);
        }

    }

    @Override
    public void delete(long id) {
        Optional<Task> foundTask =  taskRepository.findById(id);
        if(foundTask.isPresent()){
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }
    }

    @Override
    public int totalNonCompletedTasks(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode); // we need to add some derived queries / join tables


    }

    @Override
    public int totalCompletedTasks(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    //Find all the tasks on the certain project
    // and DELETE each of them
    @Override
    public void deleteByProject(ProjectDTO project) {
        List<TaskDTO> taskDTOS = listAllByProject(project);
        taskDTOS.forEach(taskDTO ->delete(taskDTO.getId()));
    }


    //write a custom method to list all tasks under a project
    public List<TaskDTO> listAllByProject(ProjectDTO project) {
        List<Task> list = taskRepository.findAllByProject(projectMapper.convertToEntity(project));
        return list.stream().map(obj -> taskMapper.convertToDto(obj)).collect(Collectors.toList());

        //return list.stream().map(obj -> {     //hoover over RETURN and accept LAMBDA replacement
        //            return taskMapper.convertToDto(obj);
        //        }).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
                                                //email of the employee
        User user = userRepository.findByUserName("gezotudu");                  //this part will be gone by SECURITY
        List<Task> list = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status, user);
        return list.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByProjectManager() {
        User user = userRepository.findByUserName("samikaratas2000@gmail.com");
        List<Task> tasks = taskRepository.findAllByProjectAssignedManager(user);
        return tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void updateStatus(TaskDTO dto) {
        Optional<Task> task = taskRepository.findById(dto.getId());  //go to DB and bring the task

        if(task.isPresent()){
            task.get().setTaskStatus(dto.getTaskStatus());  //change ths status whatever we receive from the UI
            taskRepository.save(task.get());                //save the final STATUS
        }
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {

        User user = userRepository.findByUserName("gezotudu");      //bring all the tasks for a certain employee
        List<Task> list = taskRepository.findAllByTaskStatusAndAssignedEmployee(status, user);
        return list.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> readAllByEmployee(User assignedEmployee) {

        List<Task> tasks = taskRepository.findAllByAssignedEmployee(assignedEmployee);
        return  tasks.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }
}
