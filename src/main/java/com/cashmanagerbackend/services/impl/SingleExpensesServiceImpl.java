package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleExpenseResponseDTO;
import com.cashmanagerbackend.entities.SingleExpense;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.SingleExpenseMapper;
import com.cashmanagerbackend.repositories.ExpenseCategoryRepository;
import com.cashmanagerbackend.repositories.SingleExpenseRepository;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.SingleExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SingleExpensesServiceImpl implements SingleExpensesService {
    private final SingleExpenseRepository singleExpenseRepository;
    private final SingleExpenseMapper singleExpenseMapper;
    private final UserRepository userRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    @Override
    @Transactional
    public Page<SingleExpenseResponseDTO> getSingleExpenses(String id, Pageable pageable) {
        User user = findUserById(id);
        return singleExpenseRepository.findAllByUser(user, pageable).map(singleExpenseMapper::entityToDTO);
    }

    @Override
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
    @Transactional
    public SingleExpenseResponseDTO patchSingleExpenses(String id, PatchSingleExpenseDTO patchSingleExpenseDTO) {
        User user = findUserById(id);
        SingleExpense expense = findSingleExpenseByIdAndUser(patchSingleExpenseDTO.id(), user);

        singleExpenseMapper.updateEntityFromDto(patchSingleExpenseDTO, expense);
        user.setAccount((user.getAccount() + expense.getCost()) - patchSingleExpenseDTO.cost());

        return singleExpenseMapper.entityToDTO(expense);
    }

    @Override
    @Transactional
    public void deleteSingleExpenses(String id, DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO) {
        User user = findUserById(id);
        SingleExpense singleExpense = findSingleExpenseByIdAndUser(deleteSingleExpenseIncomeDTO.id(), user);
        user.setAccount(user.getAccount() + singleExpense.getCost());

        singleExpenseRepository.delete(singleExpense);
    }
    @Override
    @Transactional
    public Page<SingleExpenseResponseDTO> getSingleExpensesByExpensesDate(String id, Pageable pageable, RangeDatesDTO rangeDatesDTO){
        User user = findUserById(id);

        if (rangeDatesDTO.from() != null && rangeDatesDTO.to() != null) {
            if (rangeDatesDTO.from().compareTo(rangeDatesDTO.to()) <= 0) {
                return singleExpenseRepository.findAllByUserAndExpensesDateGreaterThanEqualAndExpensesDateLessThanEqual(user,
                        rangeDatesDTO.from(), rangeDatesDTO.to(), pageable).map(singleExpenseMapper::entityToDTO);
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From bigger than to");
        } else if (rangeDatesDTO.from() != null) {
            return singleExpenseRepository.findAllByUserAndExpensesDateGreaterThanEqual(user, rangeDatesDTO.from(), pageable)
                    .map(singleExpenseMapper::entityToDTO);
        } else if (rangeDatesDTO.to() != null) {
            return singleExpenseRepository.findAllByUserAndExpensesDateLessThanEqual(user, rangeDatesDTO.to(), pageable)
                    .map(singleExpenseMapper::entityToDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Whoops something went wrong");
        }
    }

    @Override
    @Transactional
    public Page<SingleExpenseResponseDTO> getSingleExpensesByDescription(String id, Pageable pageable, DescriptionDTO descriptionDTO) {
        User user = findUserById(id);

        return singleExpenseRepository.findAllByUserAndDescriptionContains(user, descriptionDTO.description(), pageable).map(singleExpenseMapper:: entityToDTO);
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
