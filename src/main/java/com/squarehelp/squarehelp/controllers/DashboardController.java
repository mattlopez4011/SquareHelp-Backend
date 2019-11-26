package com.squarehelp.squarehelp.controllers;

import com.squarehelp.squarehelp.models.SmokerInfo;
import com.squarehelp.squarehelp.models.User;
import com.squarehelp.squarehelp.repositories.SmokerInfoRepository;
import com.squarehelp.squarehelp.repositories.UserRepository;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.base.BaseLocal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
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
        System.out.println("totalUsers = " + totalUsers);
//        System.out.println("totalPoints = " + totalPoints);
        SmokerInfo smokerInfo = smokeDao.getOne(id);

        // Get lapse of days (from day quit smoking to current date)
        DateTime start = new DateTime(smokerInfo.getDay_quit_smoking());
        DateTime end = new DateTime(DateTime.now());
        int days = Days.daysBetween(start, end).getDays();
        System.out.println("days = " + days);
        
        // Get points for user (5 points per day)
        int userPoints = 5 * days;
        System.out.println("userPoints = " + userPoints);
        
        

    
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

}