package com.cybertek.repository;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.Project;
import com.cybertek.entity.Task;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    //write query
    //JPQL : find number of tasks under a certain projectCode which are not COMPLETED
    @Query("select count(t) from Task t where t.project.projectCode = ?1 and t.taskStatus<>'COMPLETE'")
    int totalNonCompletedTasks(String projectCode);
/*
     //JPQL : find number of tasks under a certain projectCode which are COMPLETED
    @Query("select count(t) from Task t where t.project.projectCode = ?1 and t.taskStatus='COMPLETE'")
    int totalCompletedTasks(String projectCode);
 */

    //Native query
    @Query(value = "select count (*) " +
            "from tasks t join projects p on t.project_id=p.id " +
            "where p.project_code = ?1 and t.task_status='COMPLETE'",nativeQuery = true
    )
    int totalCompletedTasks(String projectCode);



  //  List<Task> listAllByProject(Project project);


    List<Task> findAllByProject(Project project);

    //Write a derived query to find all tasks for a certain Status and assigned to a certain employee
    List<Task> findAllByTaskStatusIsNotAndAssignedEmployee(Status status, User user);

    List<Task> findAllByProjectAssignedManager(User manager);

    List<Task> findAllByTaskStatusAndAssignedEmployee(Status status, User user);

    List<Task> findAllByAssignedEmployee(User user);

}
