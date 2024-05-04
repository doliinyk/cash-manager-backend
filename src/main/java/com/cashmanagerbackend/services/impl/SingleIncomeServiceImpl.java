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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
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
    public Page<SingleIncomeResponseDTO> getSingleIncomes(String id, Optional<Integer> page, Optional<String> sortBy) {
        User user = findUserById(id);
        return singleIncomeRepository.findAllByUser(user,
                PageRequest.of(
                        page.orElse(0),
                        5,
                        Sort.Direction.ASC,
                        sortBy.orElse(SORT_BY)
                )
        ).map(singleIncomeMapper::entityToDTO);
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
        singleIncomeRepository.save(singleIncome);

        return singleIncomeMapper.entityToDTO(singleIncome);
    }

    @Override
    @Transactional
    public SingleIncomeResponseDTO patchSingleIncomes(PatchSingleExpenseIncomeDTO patchSingleExpenseIncomeDTO) {
        SingleIncome income = findSingleIncomeById(patchSingleExpenseIncomeDTO.id());

        singleIncomeMapper.updateEntityFromDto(patchSingleExpenseIncomeDTO, income);

        return singleIncomeMapper.entityToDTO(income);
    }

    @Override
    @Transactional
    public void deleteSingleIncomes(DeleteSingleExpenseIncomeDTO deleteSingleExpenseIncomeDTO) {
        SingleIncome singleIncome = findSingleIncomeById(deleteSingleExpenseIncomeDTO.id());

        singleIncomeRepository.delete(singleIncome);
    }

    @Override
    @Transactional
    public Page<SingleIncomeResponseDTO> getSingleIncomesByIncomeDate(String id, Optional<Integer> page, Optional<String> sortBy, RangeDatesDTO rangeDatesDTO) {
        User user = findUserById(id);
        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                5,
                Sort.Direction.ASC,
                sortBy.orElse(SORT_BY)
        );
        if (rangeDatesDTO.from() != null && rangeDatesDTO.to() != null) {
            if (rangeDatesDTO.from().compareTo(rangeDatesDTO.to()) <= 0) {
                return singleIncomeRepository.findAllByUserAndIncomeDateGreaterThanEqualAndIncomeDateLessThanEqual(user,
                        rangeDatesDTO.from(), rangeDatesDTO.to(), pageRequest).map(singleIncomeMapper::entityToDTO);
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From bigger than to");
        } else if (rangeDatesDTO.from() != null) {
            return singleIncomeRepository.findAllByUserAndIncomeDateGreaterThanEqual(user, rangeDatesDTO.from(), pageRequest)
                    .map(singleIncomeMapper::entityToDTO);
        } else if (rangeDatesDTO.to() != null) {
            return singleIncomeRepository.findAllByUserAndIncomeDateLessThanEqual(user, rangeDatesDTO.to(), pageRequest)
                    .map(singleIncomeMapper::entityToDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Whoops something went wrong");
        }
    }

    @Override
    @Transactional
    public Page<SingleIncomeResponseDTO> getSingleIncomesByDescription(String id, Optional<Integer> page, Optional<String> sortBy, DescriptionDTO descriptionDTO) {
        User user = findUserById(id);

        return singleIncomeRepository.findAllByUserAndDescriptionContains(user, descriptionDTO.description(), PageRequest.of(
                        page.orElse(0),
                        5,
                        Sort.Direction.ASC,
                        sortBy.orElse(SORT_BY)
                )).map(singleIncomeMapper :: entityToDTO);
    }

    private User findUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with this ID doesn't exist"));
    }

    private SingleIncome findSingleIncomeById(String id) {
        return singleIncomeRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Income with this ID doesn't exist"));
    }
}
