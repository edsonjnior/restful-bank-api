package com.github.edsonjnior.bankapi.services.impl;

import com.github.edsonjnior.bankapi.entities.BankEntity;
import com.github.edsonjnior.bankapi.entities.BranchEntity;
import com.github.edsonjnior.bankapi.enums.ErrorMessages;
import com.github.edsonjnior.bankapi.exceptions.EntitiesServiceException;
import com.github.edsonjnior.bankapi.repositories.BankRepository;
import com.github.edsonjnior.bankapi.repositories.BranchRepository;
import com.github.edsonjnior.bankapi.services.BranchesService;
import com.github.edsonjnior.bankapi.shared.dto.BranchDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BranchesServiceImpl implements BranchesService {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private BankRepository bankRepository;


    @Override
    public BranchDto save(BranchDto branchDto) {
        var mapper = new ModelMapper();

        BranchEntity storedBranch = branchRepository.findByCode(branchDto.getCode());
        if(storedBranch != null) throw new EntitiesServiceException(
                String.format("Branch with code %s already exists", branchDto.getCode())
        );

        BankEntity storedBankEntity = bankRepository.findById(branchDto.getBank().getId()).orElseThrow(() -> {
            throw new EntitiesServiceException(
                    String.format(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(), "Bank", branchDto.getBank().getId())
            );
        });

        var branchEntity = mapper.map(branchDto, BranchEntity.class);
        branchEntity.setBank(storedBankEntity);

        var createdBranch = branchRepository.save(branchEntity);

        var responseDto = mapper.map(createdBranch, BranchDto.class);

        return responseDto;
    }

    @Override
    public BranchDto findByName(String name) {
        return null;
    }

    @Override
    public List<BranchDto> findAll() {
        var mapper = new ModelMapper();
        var branchesDto = new ArrayList<BranchDto>();

        var branchEntities = branchRepository.findAll();
        if (branchEntities != null && !branchEntities.isEmpty()) {
            var listType = new TypeToken<List<BranchDto>>() {
            }.getType();

            branchesDto = mapper.map(branchEntities, listType);
        }

        return branchesDto;
    }
}
