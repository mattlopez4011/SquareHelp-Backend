package com.squarehelp.squarehelp.controllers;

import com.squarehelp.squarehelp.models.Messages;
import com.squarehelp.squarehelp.models.Notification;
import com.squarehelp.squarehelp.models.SmokerInfo;
import com.squarehelp.squarehelp.models.User;
import com.squarehelp.squarehelp.repositories.MessagesRepository;
import com.squarehelp.squarehelp.repositories.NotificationRepository;
import com.squarehelp.squarehelp.repositories.SmokerInfoRepository;
import com.squarehelp.squarehelp.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.UserDataHandler;
import java.util.List;
import static com.squarehelp.squarehelp.util.Calculator.avgPointsCalculator;
import static com.squarehelp.squarehelp.util.Calculator.calcMoneySaved;
import static com.squarehelp.squarehelp.util.UnreadNotifications.unreadNotificationsCount;

@Controller
public class NotificationController {

    private final UserRepository userDao;
    private final NotificationRepository notiDao;
    private final SmokerInfoRepository smokeDao;
    private final MessagesRepository messageDao;

    public NotificationController(UserRepository userDao, NotificationRepository notiDao, SmokerInfoRepository smokeDao, MessagesRepository messageDao){
        this.notiDao = notiDao;
        this.userDao = userDao;
        this.smokeDao = smokeDao;
        this.messageDao = messageDao;
    }

    @GetMapping("/notifications")
    public String passingDashboard(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = user.getId();

        List<Notification> n = notiDao.findNotificationsByRecipient_user_idIs(id);
        List<Messages> m = messageDao.findMessagesByRecipient_user_idIs(id);

        // Mark all notifications read and save them.
        for (Notification noti : n) {
            noti.setIs_viewed(true);
            notiDao.save(noti);
        }

        //========= Gets the count of unread notifications
        int unreadNotifications = unreadNotificationsCount(notiDao, id);

        model.addAttribute("alertCount", unreadNotifications); // shows count for unread notifications
        model.addAttribute("smoke", smokeDao.getOne(id));
        model.addAttribute("users", user);
        model.addAttribute("authorId", userDao.getOne(id));
        model.addAttribute("uid", String.valueOf(user.getId()));
        model.addAttribute("notifications", n);
        model.addAttribute("messages", m);
        return "notification";
    }

    // Get number of unread notifications
    public int getUnreadNotifications(Long id) {
        List<Notification> n = notiDao.findNotificationsUnread(id);
        return n.size();
    }



}
