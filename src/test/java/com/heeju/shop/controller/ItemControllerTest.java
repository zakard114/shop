package com.heeju.shop.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Test product registration page permissions")
    @WithMockUser(username = "admin", roles="ADMIN") // This is an annotation that allows testing while the current member's name is admin and the user role is ADMIN is logged in.
    public void itemFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new")) // Send a get request to the product registration page.
                .andDo(print()) // Prints the request and response messages to the console window so that you can check them.
                .andExpect(status().isOk()); // Check if the response status code is OK.
    }

    @Test
    @DisplayName("Product registration page general member access test")
    @WithMockUser(username = "user", roles="USER") // Set the currently authenticated user's Role to USER.
    public void itemFormNotAdminTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(print())
                .andExpect(status().isForbidden()); // If a Forbidden exception occurs when requesting entry to the product registration page, the test passes successfully.

    }




}
