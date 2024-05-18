package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.*;
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
    public Page<SingleIncomeResponseDTO> getSingleIncomes(String id, Pageable pageable) {
        User user = findUserById(id);
        return singleIncomeRepository.findAllByUser(user, pageable).map(singleIncomeMapper::entityToDTO);
    }

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
    public Page<SingleIncomeResponseDTO> getSingleIncomesBySize(String id, Pageable pageable, SizeDTO sizeDTO) {
        User user = findUserById(id);

        if (sizeDTO.from() != null && sizeDTO.to() != null) {
            if (sizeDTO.from().compareTo(sizeDTO.to()) <= 0) {
                return singleIncomeRepository.findAllByUserAndProfitGreaterThanEqualAndProfitLessThanEqual(user,
                        sizeDTO.from(), sizeDTO.to(), pageable).map(singleIncomeMapper::entityToDTO);
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From bigger than to");
        } else if (sizeDTO.from() != null) {
            return singleIncomeRepository.findAllByUserAndProfitGreaterThanEqual(user, sizeDTO.from(), pageable)
                    .map(singleIncomeMapper::entityToDTO);
        } else if (sizeDTO.to() != null) {
            return singleIncomeRepository.findAllByUserAndProfitLessThanEqual(user, sizeDTO.to(), pageable)
                    .map(singleIncomeMapper::entityToDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Whoops something went wrong");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SingleIncomeResponseDTO> getSingleIncomesByCategory(String id, Pageable pageable, CategoryDTO categoryDTO) {
        IncomeCategory incomeCategory = incomeCategoryRepository.findByTitle(categoryDTO.title()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with this title not found"));
        return singleIncomeRepository.findAllByUserIdAndCategory(UUID.fromString(id), incomeCategory, pageable).map(singleIncomeMapper::entityToDTO);
    }
    @Override
    @Transactional
    public Page<SingleIncomeResponseDTO> getSingleIncomesByIncomeDate(String id, Pageable pageable, RangeDatesDTO rangeDatesDTO) {
        User user = findUserById(id);

        if (rangeDatesDTO.from() != null && rangeDatesDTO.to() != null) {
            if (rangeDatesDTO.from().compareTo(rangeDatesDTO.to()) <= 0) {
                return singleIncomeRepository.findAllByUserAndIncomeDateGreaterThanEqualAndIncomeDateLessThanEqual(user,
                        rangeDatesDTO.from(), rangeDatesDTO.to(), pageable).map(singleIncomeMapper::entityToDTO);
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From bigger than to");
        } else if (rangeDatesDTO.from() != null) {
            return singleIncomeRepository.findAllByUserAndIncomeDateGreaterThanEqual(user, rangeDatesDTO.from(), pageable)
                    .map(singleIncomeMapper::entityToDTO);
        } else if (rangeDatesDTO.to() != null) {
            return singleIncomeRepository.findAllByUserAndIncomeDateLessThanEqual(user, rangeDatesDTO.to(), pageable)
                    .map(singleIncomeMapper::entityToDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Whoops something went wrong");
        }
    }

    @Override
    @Transactional
    public Page<SingleIncomeResponseDTO> getSingleIncomesByDescription(String id, Pageable pageable, DescriptionDTO descriptionDTO) {
        User user = findUserById(id);

        return singleIncomeRepository.findAllByUserAndDescriptionContains(user, descriptionDTO.description(), pageable).map(singleIncomeMapper::entityToDTO);
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
