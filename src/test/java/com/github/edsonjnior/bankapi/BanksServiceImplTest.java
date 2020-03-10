package com.github.edsonjnior.bankapi;

import com.github.edsonjnior.bankapi.entities.BankEntity;
import com.github.edsonjnior.bankapi.exceptions.EntitiesServiceException;
import com.github.edsonjnior.bankapi.repositories.BankRepository;
import com.github.edsonjnior.bankapi.services.impl.BanksServiceImpl;
import com.github.edsonjnior.bankapi.shared.dto.BankDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


public class BanksServiceImplTest {

    @InjectMocks
    BanksServiceImpl banksService;

    @Mock
    BankRepository bankRepository;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    final void saveBankTest() {
        BankEntity bank = new BankEntity(1L, "Banco do Brasil SA","001");

        when(bankRepository.save(any(BankEntity.class))).thenReturn(bank);

        BankDto bankDto = banksService.save(new BankDto());

        assertNotNull(bankDto);
        assertEquals("Banco do Brasil SA", bankDto.getName());
    }

    @Test
    void saveBank_BankNameDuplicated() {
        BankEntity bank = new BankEntity(1L, "Banco do Brasil SA", "001");

        ModelMapper mapper = new ModelMapper();
        BankDto bankDto = mapper.map(bank, BankDto.class);

        when(bankRepository.findByName(anyString())).thenReturn(bank);

        assertThrows(EntitiesServiceException.class, () -> {
            banksService.save(bankDto);
        });
    }
}
