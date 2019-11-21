package com.squarehelp.squarehelp.controllers;

import com.squarehelp.squarehelp.models.SmokerInfo;
import com.squarehelp.squarehelp.models.User;
import com.squarehelp.squarehelp.repositories.SmokerInfoRepository;
import com.squarehelp.squarehelp.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@Controller
public class RegisterController {

    private final UserRepository userDao;
    private final SmokerInfoRepository smokerDao;

    public RegisterController(UserRepository userDao, SmokerInfoRepository smokerDao) {
        this.userDao = userDao;
        this.smokerDao = smokerDao;
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("smokeInfo", new SmokerInfo());
        return "register";
    }

    @PostMapping("/register")
        public String RegisterNewUser(@ModelAttribute User user, @ModelAttribute SmokerInfo smokerInfo ){
//        String hash = passwordEncoder.encode(user.getPassword());
//        user.setPassword(hash);
//        userDao.save(user);
        System.out.println("username = " + user.getUsername());
        System.out.println("password = " + user.getPassword());
        System.out.println("user email = " + user.getEmail());
        System.out.println("user State = " + user.getState());
        System.out.println("user City = " + user.getCity());
        System.out.println("user DOB = " + user.getDob());
        System.out.println("user phoneNumber = " + user.getPhoneNumber());
        System.out.println("user Gender = " + user.getGender());
        System.out.println("user day_quit_smoking = " + smokerInfo.getDay_quit_smoking());
        System.out.println("user cost_of_cigs_saved = " + smokerInfo.getCost_of_cigs_saved());

//        Add new user to users table
//        userDao.save(new User(user.getUsername(), user.getPassword(), user.getEmail(),  ) );

//        public User(String username, String password, String email, String state, String city, String dob, String phoneNumber, Date dateCreated, String lastLogin, String gender) {

        return "redirect:/login";


    }

}
