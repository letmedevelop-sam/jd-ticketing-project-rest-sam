package com.cybertek.controller;

import com.cybertek.dto.UserDTO;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.service.RoleService;
import com.cybertek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    RoleService roleService;   // DI by Field Injection to create a role  list to use in the dropdown
    @Autowired
    UserService userService; // DI by Field Injection to create a users list to use in the table to print them in th:each

    @GetMapping("/create")
    public String createUser(Model model){ //to be able to save when we enter a data: We need tot send an empty object

        model.addAttribute("user",new UserDTO());  //We used DTO because
        model.addAttribute("roles",roleService.listAllRoles()); //we used roles in dropdown // we need dependency injection FIELD INJECTION
        model.addAttribute("users",userService.listAllUsers()); // we used users in table to print all users // we need DI by Field Injection

        return "/user/create";
    }

    @PostMapping("/create")
    public String insertUser(UserDTO user,Model model){  //this will activate SAVE button //We used UserDTO because after SAVE we want to see default placeholders//We need Model because we want to create new object
        userService.save(user);   //save will come from Service but we need to  implement it
        return "redirect:/user/create"; //we will again see the same page but with a new line added to our list and default place holders in all input boxes
        //instead of going to create.htlm how about if we call the createuser method?
    }

    //project ORM part2 recording
    @GetMapping("/update/{username}")  //use get mapping because we will add some data // we will use username path variable
    public String editUser(@PathVariable("username") String username, Model model){

        model.addAttribute("user",userService.findByUserName(username)); //repository will bring it // 1. write service 2. implementation 3. controller
        model.addAttribute("users",userService.listAllUsers());
        model.addAttribute("roles",roleService.listAllRoles()); //this will bring the roles to the dropdown in UI

        return "/user/update";

    }

    @PostMapping("/update/{username}")
    public String updateUser(@PathVariable("username") String username,UserDTO user,Model model){
        userService.update(user);
        return "redirect:/user/create";
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable("username") String username) throws TicketingProjectException {
        userService.delete(username);
        return "redirect:/user/create";
    }

}
