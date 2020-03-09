package io.funnel.simpletracker.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
    The ViewController is included for testing purpose.
    It is not necessary for the tracking implementation.
 */

public class ViewController {

    private static final Logger log = LogManager.getLogger(TrackerController.class);

    @RequestMapping("/contact.html")
    public String contact() {
        log.info("contact.html accessed");
        return "contact.html";
    }

    @RequestMapping("/about.html")
    public String about() {
        log.info("about.html accessed");
        return "about";
    }

    @RequestMapping("/home.html")
    public String home() {
        log.info("home.html accessed");
        return "home";
    }

    @RequestMapping("/")
    public @ResponseBody
    String greeting() {
        return "Hello, World";
    }

}
