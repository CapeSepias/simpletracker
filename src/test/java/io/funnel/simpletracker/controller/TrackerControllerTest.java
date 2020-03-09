package io.funnel.simpletracker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TrackerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //Set referer to avoid MalformedURLException when testing tracker.gif
    public static MockHttpServletRequestBuilder myFactoryRequest(String url) {
        return MockMvcRequestBuilders.get(url)
                .header("referer", "http://localhost:8080/home.html");
    }

    @Test
    public void shouldFindHomeHTML() throws Exception {
        this.mockMvc.perform(get("/home.html")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("You've reached home.html")));
    }
    @Test
    public void shouldFindContactHTML() throws Exception {
        this.mockMvc.perform(get("/contact.html")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("You've reached contact.html")));
    }
    @Test
    public void shouldFindAboutHTML() throws Exception {
        this.mockMvc.perform(get("/about.html")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("You've reached about.html")));
    }

    @Test
    public void isCookieSet() throws Exception {
        this.mockMvc.perform(myFactoryRequest("/tracker.gif")).andExpect(status().isOk())
            // prints "Cookies = []"
            .andDo(MockMvcResultHandlers.print())
            .andExpect(cookie().exists("userId"));
    }

    @Test
    public void isCookieValue16AlpanumericalCharacters() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(myFactoryRequest("/tracker.gif")).andExpect(status().isOk()).andReturn();
        String userIdFromCookie = mvcResult.getResponse().getCookie("userId").getValue();
        assertTrue(userIdFromCookie.matches("\\w{16}"));
    }
}
