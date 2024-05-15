package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.AddRegularExpensesDTO;
import com.cashmanagerbackend.dtos.requests.DeletePayment;
import com.cashmanagerbackend.dtos.requests.PatchRegularExpensesDTO;
import com.cashmanagerbackend.dtos.responses.RegularExpenseResponseDTO;
import com.cashmanagerbackend.entities.RegularExpense;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.RegularExpensesMapper;
import com.cashmanagerbackend.repositories.ExpenseCategoryRepository;
import com.cashmanagerbackend.repositories.RegularExpenseRepository;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.RegularExpensesServices;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegularExpensesServicesImpl implements RegularExpensesServices {
    private final UserRepository userRepository;
    private final RegularExpenseRepository regularExpenseRepository;
    private final RegularExpensesMapper regularExpensesMapper;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    @Override
    @Transactional
    public RegularExpenseResponseDTO postRegularExpenses(String id, AddRegularExpensesDTO addRegularExpensesDTO) {
        User user = findUserById(id);
        RegularExpense regularExpense = regularExpensesMapper.dtoToEntity(addRegularExpensesDTO);

        if (regularExpenseRepository.findByUserAndTitle(user, addRegularExpensesDTO.title()).isEmpty()) {
            regularExpense.setCreateDate(OffsetDateTime.now());
            regularExpense.setUser(user);
            regularExpense.setCategory(expenseCategoryRepository.findByTitle(addRegularExpensesDTO.category().title())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with this title not found")));
            regularExpense = regularExpenseRepository.save(regularExpense);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Regular expense with this title already exist");
        }

        return regularExpensesMapper.entityToDto(regularExpense);
    }

    @Override
    @Transactional
    public RegularExpenseResponseDTO patchRegularExpenses(String id, PatchRegularExpensesDTO patchRegularExpensesDTO) {
        User user = findUserById(id);

        RegularExpense regularExpense = regularExpenseRepository.findByUserAndTitle(user, patchRegularExpensesDTO.oldTitle()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Regular expense with this title don`t exist"));
        regularExpensesMapper.updateEntityFromDto(patchRegularExpensesDTO, regularExpense);
        return regularExpensesMapper.entityToDto(regularExpense);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegularExpenseResponseDTO> getRegularExpenses(String id, Pageable pageable) {
        return regularExpenseRepository.findAllByUser(findUserById(id), pageable).map(regularExpensesMapper::entityToDto);
    }

    @Override
    @Transactional
    public RegularExpenseResponseDTO deleteRegularExpenses(String id, DeletePayment deletePayment) {
        RegularExpense regularExpense = regularExpenseRepository.findByUserIdAndTitle(UUID.fromString(id), deletePayment.title()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with this title not found"));
        RegularExpenseResponseDTO regularExpenseResponseDTO = regularExpensesMapper.entityToDto(regularExpense);
        regularExpenseRepository.delete(regularExpense);
        return regularExpenseResponseDTO;
    }

    private User findUserById(String id) {
        return userRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this ID doesn't exist")
        );
    }
}
