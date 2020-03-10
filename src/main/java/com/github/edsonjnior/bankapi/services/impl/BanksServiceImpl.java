package com.github.edsonjnior.bankapi.services.impl;

import com.github.edsonjnior.bankapi.entities.BankEntity;
import com.github.edsonjnior.bankapi.exceptions.EntitiesServiceException;
import com.github.edsonjnior.bankapi.repositories.BankRepository;
import com.github.edsonjnior.bankapi.services.BanksService;
import com.github.edsonjnior.bankapi.shared.dto.BankDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BanksServiceImpl implements BanksService {

    @Autowired
    private BankRepository bankRepository;

    @Override
    public BankDto save(BankDto bankDto) {
        BankEntity storedBank = bankRepository.findByName(bankDto.getName());
        if(storedBank != null) throw new EntitiesServiceException("This bank already exists");

        var mapper = new ModelMapper();

        var bankEntity = mapper.map(bankDto, BankEntity.class);
        var createdBank = bankRepository.save(bankEntity);

        var responseDto = mapper.map(createdBank, BankDto.class);

        return responseDto;
    }

    @Override
    public BankDto findByName(String name) {

        return null;

    }

    @Override
    public List<BankDto> findAll() {
        var mapper = new ModelMapper();
        var banksDto = new ArrayList<BankDto>();

        var bankEntities = bankRepository.findAll();
        if (bankEntities != null && !bankEntities.isEmpty()) {
            var listType = new TypeToken<List<BankDto>>() {
            }.getType();

            banksDto = mapper.map(bankEntities, listType);
        }

        return banksDto;
    }
}
