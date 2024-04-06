package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.dtos.requests.UserPasswordUpdateDTO;
import com.cashmanagerbackend.dtos.requests.UserRegisterDTO;
import com.cashmanagerbackend.dtos.requests.UserUpdateDTO;
import com.cashmanagerbackend.dtos.responses.UserResponseDTO;
import com.cashmanagerbackend.services.UserService;
import com.cashmanagerbackend.utils.Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserResponseDTO getUser(Principal principal) {
        return userService.getUser(principal.getName());
    }

    @PatchMapping
    public UserResponseDTO patchUser(Principal principal, @RequestBody UserUpdateDTO userUpdateDTO,
                                     @RequestParam(required = false) String redirectUrl,
                                     @RequestParam(required = false) String locale,
                                     HttpServletRequest request) {
        Map<String, Object> variables = Util.createObjectVariables(redirectUrl, request);

        return userService.patchUser(principal.getName(), userUpdateDTO, variables, locale);
    }

    @DeleteMapping
    public void deleteUser(Principal principal) {
        userService.deleteUser(principal.getName());
    }

    @PatchMapping("/password")
    public void patchUserPassword(Principal principal, @RequestBody UserPasswordUpdateDTO userPasswordUpdateDTO) {
        userService.patchUserPassword(principal.getName(), userPasswordUpdateDTO);
    }

    @PatchMapping("/restore-user")
    public void restoreUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        userService.restoreUser(userRegisterDTO);
    }
}
