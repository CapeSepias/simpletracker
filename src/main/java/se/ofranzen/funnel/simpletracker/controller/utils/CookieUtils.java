package se.ofranzen.funnel.simpletracker.controller.utils;

import se.ofranzen.funnel.simpletracker.controller.TrackerController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    private static final Logger log = LogManager.getLogger(TrackerController.class);

    public static String setNewUserId(HttpServletResponse response) {
        String userId = RandomString.randomString(16); //use random alphanumeric to avoid possibility of guessing others' userId's
        setVisitorIdCookie(response, userId);
        return userId;
    }

    public static void setVisitorIdCookie(HttpServletResponse response, String userId) {
        Cookie newCookie = new Cookie("userId", userId);
        newCookie.setMaxAge(24 * 60 * 60 * 365); //Set cookie persistence time to one year
        response.addCookie(newCookie);
    }


}
