package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.AddSingleIncomeDTO;
import com.cashmanagerbackend.dtos.requests.DeleteSingleExpenseIncomeDTO;
import com.cashmanagerbackend.dtos.requests.PatchSingleIncomeDTO;
import com.cashmanagerbackend.dtos.responses.SingleIncomeResponseDTO;
import com.cashmanagerbackend.entities.IncomeCategory;
import com.cashmanagerbackend.entities.SingleIncome;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.SingleIncomeMapper;
import com.cashmanagerbackend.repositories.IncomeCategoryRepository;
import com.cashmanagerbackend.repositories.SingleIncomeRepository;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.SingleIncomeService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SingleIncomeServiceImpl implements SingleIncomeService {
    private final SingleIncomeRepository singleIncomeRepository;
    private final SingleIncomeMapper singleIncomeMapper;
    private final UserRepository userRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;

    @Override
    @Transactional
    public SingleIncomeResponseDTO postSingleIncomes(String id, AddSingleIncomeDTO addSingleIncomeDTO) {
        User user = findUserById(id);

        SingleIncome singleIncome = singleIncomeMapper.dtoToEntity(addSingleIncomeDTO);
        singleIncome.setUser(user);
        singleIncome.setCategory(
                incomeCategoryRepository.findCategoryInUserByTitle(user, addSingleIncomeDTO.category().title())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User don't have this category"))
        );
        user.setAccount(user.getAccount() + addSingleIncomeDTO.profit());

        return singleIncomeMapper.entityToDTO(singleIncomeRepository.save(singleIncome));
    }

    @Override
    @Transactional
    public SingleIncomeResponseDTO patchSingleIncomes(String id, PatchSingleIncomeDTO patchSingleIncomeDTO) {
        User user = findUserById(id);
        SingleIncome income = findSingleIncomeByIdAndUser(patchSingleIncomeDTO.id(), user);

        singleIncomeMapper.updateEntityFromDto(patchSingleIncomeDTO, income);
        user.setAccount((user.getAccount() - income.getProfit()) + patchSingleIncomeDTO.profit());

        return singleIncomeMapper.entityToDTO(income);
    }

    @Override
    @Transactional
    public void deleteSingleIncomes(String id, DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO) {
        User user = findUserById(id);
        SingleIncome singleIncome = findSingleIncomeByIdAndUser(deleteSingleExpenseIncomeDTO.id(), user);
        user.setAccount(user.getAccount() - singleIncome.getProfit());

        singleIncomeRepository.delete(singleIncome);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SingleIncomeResponseDTO> getSingleIncomes(String id, Pageable pageable, OffsetDateTime fromByDate, OffsetDateTime toByDate, String description, Double fromBySize, Double toBySize, String categoryTitle) {
        User user = findUserById(id);

        if (fromByDate == null && toByDate == null && StringUtils.isEmptyOrWhitespace(description) && fromBySize == null && toBySize == null && StringUtils.isEmptyOrWhitespace(categoryTitle)){
            return singleIncomeRepository.findAllByUser(user, pageable).map(singleIncomeMapper::entityToDTO);
        } else if (fromByDate != null || toByDate != null){
            if (fromByDate != null && toByDate != null) {
                if (fromByDate.compareTo(toByDate) <= 0) {
                    return singleIncomeRepository.findAllByUserAndIncomeDateGreaterThanEqualAndIncomeDateLessThanEqual(user,
                            fromByDate, toByDate, pageable).map(singleIncomeMapper::entityToDTO);
                } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From bigger than to");
            } else if (fromByDate != null) {
                return singleIncomeRepository.findAllByUserAndIncomeDateGreaterThanEqual(user, fromByDate, pageable)
                        .map(singleIncomeMapper::entityToDTO);
            } else {
                return singleIncomeRepository.findAllByUserAndIncomeDateLessThanEqual(user, toByDate, pageable)
                        .map(singleIncomeMapper::entityToDTO);
            }
        } else if (!StringUtils.isEmptyOrWhitespace(description)) {
            return singleIncomeRepository.findAllByUserAndDescriptionContains(user, description, pageable).map(singleIncomeMapper:: entityToDTO);
        } else if (fromBySize != null || toBySize != null) {
            if (fromBySize != null && toBySize != null) {
                if (fromBySize.compareTo(toBySize) <= 0) {
                    return singleIncomeRepository.findAllByUserAndProfitGreaterThanEqualAndProfitLessThanEqual(user,
                            fromBySize, toBySize, pageable).map(singleIncomeMapper::entityToDTO);
                } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From bigger than to");
            } else if (fromBySize != null) {
                return singleIncomeRepository.findAllByUserAndProfitGreaterThanEqual(user, fromBySize, pageable)
                        .map(singleIncomeMapper::entityToDTO);
            } else {
                return singleIncomeRepository.findAllByUserAndProfitLessThanEqual(user, toBySize, pageable)
                        .map(singleIncomeMapper::entityToDTO);
            }
        } else {
            IncomeCategory incomeCategory = incomeCategoryRepository.findByTitle(categoryTitle).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with this title not found"));
            return singleIncomeRepository.findAllByUserIdAndCategory(UUID.fromString(id), incomeCategory, pageable).map(singleIncomeMapper::entityToDTO);
        }
    }

    private User findUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with this ID doesn't exist"));
    }

    private SingleIncome findSingleIncomeByIdAndUser(String id, User user) {
        return singleIncomeRepository.findByIdAndUser(UUID.fromString(id), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User doesn't have income with this ID"));
    }
}
