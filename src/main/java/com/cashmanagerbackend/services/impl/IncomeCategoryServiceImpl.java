package com.cashmanagerbackend.services.impl;

import com.cashmanagerbackend.dtos.requests.AddCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.DeleteCategoryRequestDTO;
import com.cashmanagerbackend.dtos.requests.PatchCategoryRequestDTO;
import com.cashmanagerbackend.dtos.responses.CategoryResponseDTO;
import com.cashmanagerbackend.entities.IncomeCategory;
import com.cashmanagerbackend.entities.User;
import com.cashmanagerbackend.entities.UsersIncomeCategory;
import com.cashmanagerbackend.mappers.IncomeCategoryMapper;
import com.cashmanagerbackend.repositories.IncomeCategoryRepository;
import com.cashmanagerbackend.repositories.UserRepository;
import com.cashmanagerbackend.repositories.UsersIncomeCategoryRepository;
import com.cashmanagerbackend.services.IncomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class IncomeCategoryServiceImpl implements IncomeCategoryService {
    private final UserRepository userRepository;
    private final UsersIncomeCategoryRepository usersIncomeCategoryRepository;
    private final IncomeCategoryMapper incomeCategoryMapper;
    private final IncomeCategoryRepository incomeCategoryRepository;

    @Override
    @Transactional
    public Map<String, CategoryResponseDTO> getUserIncomesCategory(String name) {
        User user = findUserById(name);
        SortedSet<UsersIncomeCategory> usersIncomeCategories = usersIncomeCategoryRepository.findAllByUser(user);
        SortedSet<IncomeCategory> incomeCategories = user.getIncomeCategories();

        return packColorCodeIncomeCategoryMap(incomeCategories, usersIncomeCategories);
    }

    @Override
    @Transactional
    public Map<String, CategoryResponseDTO> postUserIncomesCategory(String name, AddCategoryRequestDTO addCategoryRequestDTO) {
        User user = findUserById(name);
        Optional<IncomeCategory> incomeCategoryOptional = incomeCategoryRepository.findByTitle(addCategoryRequestDTO.title());
        SortedSet<UsersIncomeCategory> usersIncomeCategories = usersIncomeCategoryRepository.findAllByUser(user);
        SortedSet<IncomeCategory> incomeCategories = user.getIncomeCategories();
        UsersIncomeCategory usersIncomeCategory;
        IncomeCategory incomeCategory;

        for (IncomeCategory i :
                user.getIncomeCategories()) {
            if (i.getTitle().equals(addCategoryRequestDTO.title())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User already have this category");
            }
        }
        if (incomeCategoryOptional.isPresent()) {
            incomeCategories.add(incomeCategoryOptional.get());
            usersIncomeCategory = new UsersIncomeCategory();
            usersIncomeCategory.setUser(user);
            usersIncomeCategory.setCategory(incomeCategoryOptional.get());
            usersIncomeCategory.setColorCode(addCategoryRequestDTO.colorCode());
            usersIncomeCategoryRepository.save(usersIncomeCategory);
        } else {
            incomeCategory = new IncomeCategory();
            incomeCategory.setTitle(addCategoryRequestDTO.title());
            incomeCategory = incomeCategoryRepository.save(incomeCategory);

            usersIncomeCategory = new UsersIncomeCategory();
            usersIncomeCategory.setColorCode(addCategoryRequestDTO.colorCode());
            usersIncomeCategory.setUser(user);
            usersIncomeCategory.setCategory(incomeCategory);
            usersIncomeCategory = usersIncomeCategoryRepository.save(usersIncomeCategory);

            incomeCategories.add(incomeCategory);
        }

        usersIncomeCategories.add(usersIncomeCategory);
        return packColorCodeIncomeCategoryMap(incomeCategories, usersIncomeCategories);
    }

    @Override
    @Transactional
    public Map<String, CategoryResponseDTO> patchUserIncomesCategory(String name,
                                                                       PatchCategoryRequestDTO patchCategoryRequestDTO) {
        User user = findUserById(name);
        IncomeCategory incomeCategory = incomeCategoryRepository.findCategoryInUserByTitle(user, patchCategoryRequestDTO.oldTitle())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "User doesn't have this category"));
        UsersIncomeCategory usersIncomeCategory = usersIncomeCategoryRepository.findByUserAndCategory(user, incomeCategory).get();
        IncomeCategory newIncomeCategory = incomeCategory;

        if (!usersIncomeCategory.getColorCode().equals(patchCategoryRequestDTO.colorCode())
                && patchCategoryRequestDTO.colorCode() != null && !patchCategoryRequestDTO.colorCode().isEmpty()) {
            usersIncomeCategory.setColorCode(patchCategoryRequestDTO.colorCode());
        }
        if (!incomeCategory.getTitle().equals(patchCategoryRequestDTO.newTitle())
                && patchCategoryRequestDTO.newTitle() != null && !patchCategoryRequestDTO.newTitle().isEmpty()) {
            Optional<IncomeCategory> incomeCategoryOptional = incomeCategoryRepository.findByTitle(patchCategoryRequestDTO.newTitle());
            if (incomeCategoryOptional.isPresent()) {
                usersIncomeCategory.setCategory(incomeCategoryOptional.get());
                incomeCategory.getUsers().remove(user);
                newIncomeCategory = new IncomeCategory();
                newIncomeCategory.setTitle(patchCategoryRequestDTO.newTitle());
            } else {
                newIncomeCategory = new IncomeCategory();
                newIncomeCategory.setTitle(patchCategoryRequestDTO.newTitle());
                newIncomeCategory = incomeCategoryRepository.save(newIncomeCategory);
                usersIncomeCategory.setCategory(newIncomeCategory);
                usersIncomeCategoryRepository.save(usersIncomeCategory);
                incomeCategory.getUsers().remove(user);
            }
            ArrayList<String> standardCategoryTitles =
                    new ArrayList<>(Arrays.asList("salary","gift", "percentages","other"));
            if (incomeCategory.getUsers().isEmpty() && !standardCategoryTitles.contains(incomeCategory.getTitle())){
                incomeCategoryRepository.deleteById(incomeCategory.getId());
            }
        }

        HashMap<String, CategoryResponseDTO> map = new HashMap<>();
        map.put(usersIncomeCategory.getColorCode(), incomeCategoryMapper.entityToDTO(newIncomeCategory));

        return map;
    }

    @Override
    @Transactional
    public void deleteUserIncomesCategory(String name, DeleteCategoryRequestDTO deleteCategoryRequestDTO) {
        User user = findUserById(name);
        IncomeCategory incomeCategory = incomeCategoryRepository.findCategoryInUserByTitle(user, deleteCategoryRequestDTO.title())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "User doesn't have this category"));


        incomeCategory.getUsers().remove(user);
        ArrayList<String> standardCategoryTitles =
                new ArrayList<>(Arrays.asList("salary","gift", "percentages","other"));
        if (incomeCategory.getUsers().isEmpty() && !standardCategoryTitles.contains(incomeCategory.getTitle())){
            incomeCategoryRepository.deleteById(incomeCategory.getId());
        }
    }

    private User findUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with this ID doesn't exist"));
    }

    private Map<String, CategoryResponseDTO> packColorCodeIncomeCategoryMap(SortedSet<IncomeCategory> incomeCategories, SortedSet<UsersIncomeCategory> usersIncomeCategories) {
        HashMap<String, CategoryResponseDTO> map = new HashMap<>();
        Iterator<IncomeCategory> incomeCategory = incomeCategories.iterator();

        for (UsersIncomeCategory usersIncomeCategory : usersIncomeCategories) {
            map.put(usersIncomeCategory.getColorCode(), incomeCategoryMapper.entityToDTO(incomeCategory.next()));
        }
        return map;
    }
}
