package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.AddRegularIncomeDTO;
import com.cashmanagerbackend.dtos.requests.DeletePayment;
import com.cashmanagerbackend.dtos.requests.PatchRegularIncomeDTO;
import com.cashmanagerbackend.dtos.responses.RegularIncomeResponseDTO;
import com.cashmanagerbackend.entities.RegularIncome;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.mappers.RegularIncomeMapper;
import com.cashmanagerbackend.repositories.IncomeCategoryRepository;
import com.cashmanagerbackend.repositories.RegularIncomeRepository;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.services.RegularIncomeService;
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
public class RegularIncomeServiceImpl implements RegularIncomeService {
    private final UserRepository userRepository;
    private final RegularIncomeRepository regularIncomeRepository;
    private final RegularIncomeMapper regularIncomeMapper;
    private final IncomeCategoryRepository incomeCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<RegularIncomeResponseDTO> getRegularIncome(String id, Pageable pageable) {
        return regularIncomeRepository.findAllByUser(findUserById(id), pageable).map(regularIncomeMapper::entityToDto);
    }

    @Override
    @Transactional
    public RegularIncomeResponseDTO postRegularIncome(String id, AddRegularIncomeDTO addRegularIncomeDTO) {
        User user = findUserById(id);
        RegularIncome regularIncome = regularIncomeMapper.dtoToEntity(addRegularIncomeDTO);

        if (regularIncomeRepository.findByUserAndTitle(user, addRegularIncomeDTO.title()).isEmpty()) {
            regularIncome.setCreateDate(OffsetDateTime.now());
            regularIncome.setUser(user);
            regularIncome.setCategory(incomeCategoryRepository.findByTitle(addRegularIncomeDTO.category().title())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with this title not found")));
            regularIncome = regularIncomeRepository.save(regularIncome);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Regular income with this title already exist");
        }

        return regularIncomeMapper.entityToDto(regularIncome);        }

    @Override
    @Transactional
    public RegularIncomeResponseDTO patchRegularIncome(String id, PatchRegularIncomeDTO patchRegularIncomeDTO) {
        User user = findUserById(id);

        RegularIncome regularIncome = regularIncomeRepository.findByUserAndTitle(user, patchRegularIncomeDTO.oldTitle()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Regular income with this title don`t exist"));
        regularIncomeMapper.updateEntityFromDto(patchRegularIncomeDTO, regularIncome);
        return regularIncomeMapper.entityToDto(regularIncome);
    }

    @Override
    @Transactional
    public RegularIncomeResponseDTO deleteRegularIncome(String id, DeletePayment deletePayment) {
        RegularIncome regularIncome = regularIncomeRepository.findByUserIdAndTitle(UUID.fromString(id), deletePayment.title()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Regular income with this title not found"));
        RegularIncomeResponseDTO regularIncomeResponseDTO = regularIncomeMapper.entityToDto(regularIncome);
        regularIncomeRepository.delete(regularIncome);
        return regularIncomeResponseDTO;
    }

    private User findUserById(String id) {
        return userRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this ID doesn't exist")
        );
    }
}
