package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.UserPasswordUpdateDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.requests.UserUpdateDTO;
import com.cashmanagerbackend.dtos.responses.UserResponseDTO;
import com.cashmanagerbackend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static com.cashmanagerbackend.utils.TestConstants.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private Principal principal;

    @Test
    @DisplayName("Should return data about user like login, email, create date and account")
    void getUser() throws Exception {

        UserResponseDTO userResponseDTO = new UserResponseDTO("bash", "email@email.com", LocalDateTime.of(2015,
                Month.JULY, 29, 19, 30, 44, 470471944), 0);

        when(userService.getUser(principal.getName())).thenReturn(userResponseDTO);

        mockMvc.perform(get(URL_USER)
                        .principal(principal))
                .andExpect(jsonPath("$.login").value(userResponseDTO.login()))
                .andExpect(jsonPath("$.email").value(userResponseDTO.email()))
                .andExpect(jsonPath("$.account").value(userResponseDTO.account()))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUser(principal.getName());
    }

    @Test
    @DisplayName("Should return data about user who change his email or login this must look like login, email, create date and account")
    void patchUser() throws Exception {
        String locale = "en";
        String redirectUrl = "http://example.com/auth/activate";
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("rash", "email@email.com");
        UserResponseDTO userResponseDTO = new UserResponseDTO("rash", "email@email.com", LocalDateTime.of(2015,
                Month.JULY, 29, 19, 30, 44, 470471944), 0);
        Map<String, Object> variables = new HashMap<>();
        variables.put("redirectUrl", redirectUrl);
        variables.put("isFrontendRequest", true);

        when(userService.patchUser(principal.getName(), userUpdateDTO, variables, locale)).thenReturn(userResponseDTO);

        mockMvc.perform(patch(URL_USER)
                        .principal(principal)
                        .contentType("application/json")
                        .param("locale", locale)
                        .param("redirectUrl", redirectUrl)
                        .content(objectMapper.writeValueAsString(userUpdateDTO)))
                .andExpect(jsonPath("$.login").value(userUpdateDTO.login()))
                .andExpect(jsonPath("$.email").value(userUpdateDTO.email()))
                .andExpect(status().isOk());

        verify(userService, times(1)).patchUser(principal.getName(), userUpdateDTO, variables, locale);
    }

    @Test
    @DisplayName("Should return is OK status when delete user")
    void deleteUser() throws Exception {
        mockMvc.perform(delete(URL_USER)
                .principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return is OK status when restore patch user password")
    void patchUserPassword() throws Exception {
        UserPasswordUpdateDTO userPasswordUpdateDTO =
                new UserPasswordUpdateDTO("password", "password2");

        mockMvc.perform(patch(URL_USER_PATCH_PASSWORD)
                .principal(principal)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userPasswordUpdateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return is OK status when restore user")
    void restoreUser() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("bash", "password", "email@email.com");

        mockMvc.perform(patch(URL_USER_RESTORE)
                .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRegisterDTO)))
                .andExpect(status().isOk());
    }
}