package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.*;
import com.cashmanagerbackend.dtos.responses.SingleIncomeResponseDTO;
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
    private static final String SORT_BY = "incomeDate";
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
    public SingleIncomeResponseDTO postSingleIncomes(String id, AddSingleExpenseIncomeDTO addSingleExpenseIncomeDTO) {
        User user = findUserById(id);

        SingleIncome singleIncome = singleIncomeMapper.dtoToEntity(addSingleExpenseIncomeDTO);
        singleIncome.setUser(user);
        singleIncome.setCategory(
                incomeCategoryRepository.findCategoryInUserByTitle(user, addSingleExpenseIncomeDTO.category().title())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User don't have this category"))
        );

        return singleIncomeMapper.entityToDTO(singleIncomeRepository.save(singleIncome));
    }

    @Override
    @Transactional
    public SingleIncomeResponseDTO patchSingleIncomes(String id, PatchSingleExpenseIncomeDTO patchSingleExpenseIncomeDTO) {
        SingleIncome income = findSingleIncomeByIdAndUser(patchSingleExpenseIncomeDTO.id(), findUserById(id));

        singleIncomeMapper.updateEntityFromDto(patchSingleExpenseIncomeDTO, income);

        return singleIncomeMapper.entityToDTO(income);
    }

    @Override
    @Transactional
    public void deleteSingleIncomes(String id, DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO) {
        User user = findUserById(id);
        SingleIncome singleIncome = findSingleIncomeByIdAndUser(deleteSingleExpenseIncomeDTO.id(), user);

        singleIncomeRepository.delete(singleIncome);
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

        return singleIncomeRepository.findAllByUserAndDescriptionContains(user, descriptionDTO.description(), pageable).map(singleIncomeMapper :: entityToDTO);
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
