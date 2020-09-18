package se.ofranzen.funnel.simpletracker.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.ofranzen.funnel.simpletracker.controller.utils.CookieUtils;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class TrackerController {

    private static final Logger log = LogManager.getLogger(TrackerController.class);
    private static final Logger accessLog = LogManager.getLogger("AccessLog");

    // Use a byte array in an output stream instead of reading and sending an actual gif.
    byte[] trackingGif = { 0x47, 0x49, 0x46, 0x38, 0x39, 0x61, 0x1, 0x0, 0x1, 0x0, (byte) 0x80, 0x0, 0x0,
            (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x0, 0x0, 0x0, 0x2c, 0x0, 0x0, 0x0, 0x0, 0x1, 0x0, 0x1, 0x0,
            0x0, 0x2, 0x2, 0x44, 0x1, 0x0, 0x3b };

    @GetMapping("/tracker.gif")
    public void tracker(@CookieValue(value = "userId",
                            defaultValue = "notSet")
                            String userId,
                        @RequestHeader(value = "referer",
                            required = false)
                            String referrer,
                            HttpServletResponse response) {

        String path = "unknown";

        log.debug("Tracker image tracker.png requested");
        log.debug("Referrer is: '" + referrer + "'");
        log.debug("Value of cookie 'userId' is " + userId);

        try {
            URL url = new URL(referrer);
            log.debug("URL is: '" + url + "'");
            log.debug("path is: '" + url.getPath() + "'");
            path = url.getPath();
        } catch (MalformedURLException e) {
            log.error("Could not create URL from referer", e);
        }

        if(userId.equalsIgnoreCase("notSet")){
            userId = CookieUtils.setNewUserId(response);
            log.info("New user. Setting userId to: " + userId);
        } else {
            log.info("Returning user with userId: " + userId);
        }

        accessLog.info(path + " - " + userId);

        response.setContentType("image/gif");
        response.setContentLength(trackingGif.length);

        try {
            OutputStream out = response.getOutputStream();
            out.write(trackingGif);
            out.close();
        } catch (IOException e) {
            //This access will be logged but there may be unforseen errors on the web page using the tracking gif.
            log.error("Could not set response to gif output stream", e);
            e.printStackTrace();
        }
    }
}
