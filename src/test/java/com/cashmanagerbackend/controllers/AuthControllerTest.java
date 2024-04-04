package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.EmailDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.responses.AccessRefreshTokenDTO;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.cashmanagerbackend.utils.TestConstants.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("Should return UUID of created user and created status code when register user")
    void testRegister() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("login", "12345678", "email@email.com");
        String locale = "en_US";
        String redirectUrl = "http://example.com/register";

        UUID uuid = UUID.randomUUID();

        Map<String, Object> variables = new HashMap<>();
        variables.put("redirectUrl", redirectUrl);
        variables.put("isFrontendRequest", true);

        when(authService.registerUser(userRegisterDTO, locale, variables))
                .thenReturn(uuid);

        mockMvc.perform(post(URL_AUTH_REGISTER)
                                .contentType("application/json")
                                .param("locale", locale)
                                .param("redirectUrl", redirectUrl)
                                .content(objectMapper.writeValueAsString(userRegisterDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("\"" + uuid + "\""));

        verify(authService, times(1)).registerUser(userRegisterDTO, locale, variables);
    }

    @Test
    @DisplayName("Should return ok status code when activate user")
    void testActivate() throws Exception {
        UUID uuid = UUID.randomUUID();
        String activationToken = UUID.randomUUID().toString();

        mockMvc.perform(post(URL_AUTH_ACTIVATE)
                                .contentType("application/json")
                                .param("userId", uuid.toString())
                                .param("activationToken", activationToken))
                .andExpect(status().isOk());

        verify(authService, times(1)).activateUser(uuid, activationToken);
    }

    @Test
    @DisplayName("Should return ok status code when send activation email")
    void testSendActivationEmail() throws Exception {
        EmailDTO emailDTO = new EmailDTO("email@email.com");
        String locale = "en_US";
        String redirectUrl = "http://example.com/register";

        Map<String, Object> variables = new HashMap<>();
        variables.put("redirectUrl", redirectUrl);
        variables.put("isFrontendRequest", true);

        mockMvc.perform(post(URL_AUTH_SEND_ACTIVATION_EMAIL)
                                .contentType("application/json")
                                .param("locale", locale)
                                .param("redirectUrl", redirectUrl)
                                .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isOk());

        verify(authService, times(1)).sendActivationEmail(emailDTO, locale, variables);
    }

    @Test
    @DisplayName("Should throw ResponseStatusException with forbidden status when login without authentication")
    void testLoginWithoutAuthentication() throws Exception {
        Principal principal = mock(Principal.class);

        mockMvc.perform(post(URL_AUTH_LOGIN)
                                .principal(principal))
                .andExpect(status().isForbidden())
                .andExpect(result -> {
                    if (!(result.getResolvedException() instanceof ResponseStatusException)) {
                        throw new AssertionError("Expected ResponseStatusException");
                    }
                });

        verify(authService, never()).loginUser(any());
    }

    @Test
    @DisplayName("Should return tokens when login with authentication")
    void testLoginWithAuthentication() throws Exception {
        User user = new User();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null);

        AccessRefreshTokenDTO tokenDTO = new AccessRefreshTokenDTO("accessToken", "refreshToken");

        when(authService.loginUser(user)).thenReturn(tokenDTO);

        mockMvc.perform(post(URL_AUTH_LOGIN)
                                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(tokenDTO.accessToken()))
                .andExpect(jsonPath("$.refreshToken").value(tokenDTO.refreshToken()));

        verify(authService, times(1)).loginUser(user);
    }
}