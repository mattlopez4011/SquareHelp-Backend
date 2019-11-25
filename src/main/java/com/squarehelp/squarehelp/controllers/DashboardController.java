package com.squarehelp.squarehelp.controllers;

import com.squarehelp.squarehelp.models.SmokerInfo;
import com.squarehelp.squarehelp.models.User;
import com.squarehelp.squarehelp.repositories.SmokerInfoRepository;
import com.squarehelp.squarehelp.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.squarehelp.squarehelp.util.Calculator.avgPointsCalculator;
import static com.squarehelp.squarehelp.util.Calculator.calcMoneySaved;

@Controller
public class DashboardController {
    private final SmokerInfoRepository smokeDao;
    private final UserRepository userDao;

    public DashboardController(SmokerInfoRepository smokeDao, UserRepository userDao) {
        this.smokeDao = smokeDao;
        this.userDao = userDao;
    }

    @GetMapping("/dashboard")
    public String passingDashboard(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = user.getId();
        int totalUsers = (int)userDao.count();
        SmokerInfo smokerInfo = smokeDao.getOne(id);
        int totalCommunityUsers = avgPointsCalculator(smokerInfo.getPoints(),totalUsers);

        model.addAttribute("users", userDao.getOne(id));
        model.addAttribute("smoke", smokerInfo);
        model.addAttribute("moneySaved", calcMoneySaved(smokerInfo.getCost_of_cigs_saved(), smokerInfo.getTotal_days_smoke_free()));
        model.addAttribute("communityCount", totalCommunityUsers);
        return "dashboard";
    }

    @PostMapping("/dashboard/{user_id}")
    public String searchUser(Model model, @RequestParam String searchQuery, @PathVariable long user_id) {
        List<User> searchResults;
        searchResults = userDao.findByUsernameContaining(searchQuery);

        model.addAttribute("ListOfusers", searchResults ) ;
        model.addAttribute("users", userDao.getOne(user_id));
        model.addAttribute("smoke", smokeDao.getOne(user_id));

        return "dashboard";
    }

    @GetMapping("/searchAll")
    @ResponseBody
    public List<User> sendAllUsers(){
//        System.out.println(userDao.findByUsernameContaining(username));
        System.out.println(userDao.findAll());
        System.out.println("Json of all USERS sent to JS!");
        return userDao.findAll();
    }
}