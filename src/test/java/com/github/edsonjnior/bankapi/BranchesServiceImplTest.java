package com.github.edsonjnior.bankapi;

import com.github.edsonjnior.bankapi.entities.BankEntity;
import com.github.edsonjnior.bankapi.entities.BranchEntity;
import com.github.edsonjnior.bankapi.exceptions.EntitiesServiceException;
import com.github.edsonjnior.bankapi.repositories.BankRepository;
import com.github.edsonjnior.bankapi.repositories.BranchRepository;
import com.github.edsonjnior.bankapi.services.impl.BranchesServiceImpl;
import com.github.edsonjnior.bankapi.shared.dto.BankDto;
import com.github.edsonjnior.bankapi.shared.dto.BranchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class BranchesServiceImplTest {

    @InjectMocks
    BranchesServiceImpl branchesService;

    @Mock
    BranchRepository branchRepository;

    @Mock
    BankRepository bankRepository;

    @BeforeEach
    void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    final void saveBranchTest(){
        var bankEntity = new BankEntity(1L, "001", "Banco do Brasil SA");
        var branchEntity = new BranchEntity(1L,"Ag Jockey Club", "3178", bankEntity);

        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bankEntity));
        when(branchRepository.save(any(BranchEntity.class))).thenReturn(branchEntity);

        var branchDto = new BranchDto();
        branchDto.setBank(new BankDto(1L));
        BranchDto createdBranch = branchesService.save(branchDto);

        assertNotNull(createdBranch);
        assertNotNull(createdBranch.getBank());
        assertEquals("Ag Jockey Club", createdBranch.getName());
    }

    @Test
    final void saveBranch_BankNotFoundTest(){
        when(bankRepository.findById(anyLong())).thenReturn(null);

        var branchDto = new BranchDto();
        branchDto.setBank(new BankDto());

        assertThrows(EntitiesServiceException.class, () -> {
            branchesService.save(branchDto);
        });
    }

}
