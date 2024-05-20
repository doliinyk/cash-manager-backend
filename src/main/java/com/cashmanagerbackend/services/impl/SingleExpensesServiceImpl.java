package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleExpenseResponseDTO;
import com.cashmanagerbackend.entities.ExpenseCategory;
import com.cashmanagerbackend.entities.SingleExpense;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.SingleExpenseMapper;
import com.cashmanagerbackend.repositories.ExpenseCategoryRepository;
import com.cashmanagerbackend.repositories.SingleExpenseRepository;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.SingleExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "user")
@RequiredArgsConstructor
public class SingleExpensesServiceImpl implements SingleExpensesService {
    private final SingleExpenseRepository singleExpenseRepository;
    private final SingleExpenseMapper singleExpenseMapper;
    private final UserRepository userRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<SingleExpenseResponseDTO> getSingleExpenses(String id, Pageable pageable, OffsetDateTime fromByDate, OffsetDateTime toByDate, String description, Double fromBySize, Double toBySize, String categoryTitle) {
        User user = findUserById(id);

        if (fromByDate == null && toByDate == null && StringUtils.isEmptyOrWhitespace(description) && fromBySize == null && toBySize == null && StringUtils.isEmptyOrWhitespace(categoryTitle)){
            return singleExpenseRepository.findAllByUser(user, pageable).map(singleExpenseMapper::entityToDTO);
        } else if (fromByDate != null || toByDate != null){
            if (fromByDate != null && toByDate != null) {
                if (fromByDate.compareTo(toByDate) <= 0) {
                    return singleExpenseRepository.findAllByUserAndExpensesDateGreaterThanEqualAndExpensesDateLessThanEqual(user,
                            fromByDate, toByDate, pageable).map(singleExpenseMapper::entityToDTO);
                } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From bigger than to");
            } else if (fromByDate != null) {
                return singleExpenseRepository.findAllByUserAndExpensesDateGreaterThanEqual(user, fromByDate, pageable)
                        .map(singleExpenseMapper::entityToDTO);
            } else {
                return singleExpenseRepository.findAllByUserAndExpensesDateLessThanEqual(user, toByDate, pageable)
                        .map(singleExpenseMapper::entityToDTO);
            }
        } else if (!StringUtils.isEmptyOrWhitespace(description)) {
            return singleExpenseRepository.findAllByUserAndDescriptionContains(user, description, pageable).map(singleExpenseMapper:: entityToDTO);
        } else if (fromBySize != null || toBySize != null) {
            if (fromBySize != null && toBySize != null) {
                if (fromBySize.compareTo(toBySize) <= 0) {
                    return singleExpenseRepository.findAllByUserAndCostGreaterThanEqualAndCostLessThanEqual(user,
                            fromBySize, toBySize, pageable).map(singleExpenseMapper::entityToDTO);
                } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From bigger than to");
            } else if (fromBySize != null) {
                return singleExpenseRepository.findAllByUserAndCostGreaterThanEqual(user, fromBySize, pageable)
                        .map(singleExpenseMapper::entityToDTO);
            } else {
                return singleExpenseRepository.findAllByUserAndCostLessThanEqual(user, toBySize, pageable)
                        .map(singleExpenseMapper::entityToDTO);
            }
        } else {
            ExpenseCategory expenseCategory = expenseCategoryRepository.findByTitle(categoryTitle).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with this title not found"));
            return singleExpenseRepository.findAllByUserIdAndCategory(UUID.fromString(id), expenseCategory, pageable).map(singleExpenseMapper::entityToDTO);
        }
    }

    @Override
    @CacheEvict(key = "#id")
    @Transactional
    public SingleExpenseResponseDTO postSingleExpenses(String id, AddSingleExpenseDTO addSingleExpenseDTO) {
        User user = findUserById(id);

        SingleExpense singleExpense = singleExpenseMapper.dtoToEntity(addSingleExpenseDTO);
        singleExpense.setUser(user);
        singleExpense.setCategory(
                expenseCategoryRepository.findCategoryInUserByTitle(user, addSingleExpenseDTO.category().title())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User don't have this category"))
        );
        user.setAccount(user.getAccount() - addSingleExpenseDTO.cost());

        return singleExpenseMapper.entityToDTO(singleExpenseRepository.save(singleExpense));
    }

    @Override
    @CacheEvict(key = "#id")
    @Transactional
    public SingleExpenseResponseDTO patchSingleExpenses(String id, PatchSingleExpenseDTO patchSingleExpenseDTO) {
        User user = findUserById(id);
        SingleExpense expense = findSingleExpenseByIdAndUser(patchSingleExpenseDTO.id(), user);

        singleExpenseMapper.updateEntityFromDto(patchSingleExpenseDTO, expense);
        user.setAccount((user.getAccount() + expense.getCost()) - patchSingleExpenseDTO.cost());

        return singleExpenseMapper.entityToDTO(expense);
    }

    @Override
    @CacheEvict(key = "#id")
    @Transactional
    public void deleteSingleExpenses(String id, DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO) {
        User user = findUserById(id);
        SingleExpense singleExpense = findSingleExpenseByIdAndUser(deleteSingleExpenseIncomeDTO.id(), user);
        user.setAccount(user.getAccount() + singleExpense.getCost());

        singleExpenseRepository.delete(singleExpense);
    }

    private User findUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with this ID doesn't exist"));
    }

    private SingleExpense findSingleExpenseByIdAndUser(String id, User user) {
        return singleExpenseRepository.findByIdAndUser(UUID.fromString(id), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User doesn't have expense with this ID"));
    }
}
