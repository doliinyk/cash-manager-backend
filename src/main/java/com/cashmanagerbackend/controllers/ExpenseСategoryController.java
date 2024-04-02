package com.cashmanagerbackend.controllers;

import com.cashmanagerbackend.services.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories/expenses")
@RequiredArgsConstructor
public class ExpenseСategoryController {
    private final ExpenseCategoryService expenseCategoryService;

    @GetMapping
    public void getUserExspenses(){

    }
}
