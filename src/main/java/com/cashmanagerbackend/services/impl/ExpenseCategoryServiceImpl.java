package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.AddCategoryRequestDTO;
import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;
import com.cashmanagerbackend.entities.ExpenseCategory;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.entities.UsersExpenseCategory;
import com.cashmanagerbackend.mappers.ExpenseCategoryMapper;
import com.cashmanagerbackend.repositories.ExpenseCategoryRepository;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.repositories.UsersExpenseCategoryRepository;
import com.cashmanagerbackend.services.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {
    private final UserRepository userRepository;
    private final UsersExpenseCategoryRepository usersExpenseCategoryRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final ExpenseCategoryMapper expenseCategoryMapper;

    @Override
    @Transactional
    public Map<String, CategoryResponseDTO> getUserExspensesCategory(String name) {
        User user = findUserById(name);
        SortedSet<UsersExpenseCategory> usersExpenseCategories = usersExpenseCategoryRepository.findAllByUser(user);
        SortedSet<ExpenseCategory> expenseCategories = user.getExpenseCategories();

        return packColorCodeExpenseCategoryMap(expenseCategories, usersExpenseCategories);
    }

    @Override
    @Transactional
    public Map<String, CategoryResponseDTO> postUserExspensesCategory(String name, AddCategoryRequestDTO addCategoryRequestDTO) {
        User user = findUserById(name);
        Optional<ExpenseCategory> expenseCategoryOptional = expenseCategoryRepository.findByTitle(addCategoryRequestDTO.title());
        SortedSet<UsersExpenseCategory> usersExpenseCategories = usersExpenseCategoryRepository.findAllByUser(user);
        SortedSet<ExpenseCategory> expenseCategories = user.getExpenseCategories();
        UsersExpenseCategory usersExpenseCategory;
        ExpenseCategory expenseCategory;

        for (ExpenseCategory i:
            user.getExpenseCategories()) {
            if (i.getTitle().equals(addCategoryRequestDTO.title())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User already have this category");
            }
        }
        if (expenseCategoryOptional.isPresent()) {
            user.getExpenseCategories().add(expenseCategoryOptional.get());
            expenseCategoryOptional.get().getUsers().add(user);

            usersExpenseCategory = new UsersExpenseCategory();
            usersExpenseCategory.setUser(user);
            usersExpenseCategory.setCategory(expenseCategoryOptional.get());
            usersExpenseCategory.setColorCode(addCategoryRequestDTO.colorCode());
            usersExpenseCategoryRepository.save(usersExpenseCategory);
        } else {
            usersExpenseCategory = new UsersExpenseCategory();
            usersExpenseCategory.setColorCode(addCategoryRequestDTO.colorCode());
            usersExpenseCategory.setUser(user);
            usersExpenseCategory = usersExpenseCategoryRepository.save(usersExpenseCategory);

            expenseCategory = new ExpenseCategory();
            expenseCategory.setTitle(addCategoryRequestDTO.title());
            expenseCategory = expenseCategoryRepository.save(expenseCategory);
            usersExpenseCategory.setCategory(expenseCategory);
            expenseCategories.add(expenseCategory);
        }

        usersExpenseCategories.add(usersExpenseCategory);
        return packColorCodeExpenseCategoryMap(expenseCategories, usersExpenseCategories);
    }

    private User findUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with this ID doesn't exist"));
    }

    private Map<String, CategoryResponseDTO> packColorCodeExpenseCategoryMap(SortedSet<ExpenseCategory> expenseCategories, SortedSet<UsersExpenseCategory> usersExpenseCategories){
        HashMap<String, CategoryResponseDTO> map = new HashMap<>();
        Iterator<ExpenseCategory> expenseCategory = expenseCategories.iterator();

        for (UsersExpenseCategory usersExpenseCategory : usersExpenseCategories) {
            map.put(usersExpenseCategory.getColorCode(), expenseCategoryMapper.entityToDTO(expenseCategory.next()));
        }
        return map;
    }
}
