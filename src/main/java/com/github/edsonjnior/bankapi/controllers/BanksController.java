package com.github.edsonjnior.bankapi.controllers;

import com.github.edsonjnior.bankapi.domains.SecurityConstants;
import com.github.edsonjnior.bankapi.services.BanksService;
import com.github.edsonjnior.bankapi.shared.dto.BankDto;
import com.github.edsonjnior.bankapi.ui.model.request.BankRequestModel;
import com.github.edsonjnior.bankapi.ui.model.response.BankRest;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/banks")
public class BanksController {

    @Autowired
    private BanksService bankService;

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BankRest> getBanks() {
        var response = new ArrayList<BankRest>();

        var banksDto = bankService.findAll();
        if (banksDto != null && !banksDto.isEmpty()) {
            var listType = new TypeToken<List<BankRest>>() {
            }.getType();
            response = new ModelMapper().map(banksDto, listType);
        }

        return response;
    }

    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({SecurityConstants.ROLE_ADMIN})
    public BankRest create(@RequestBody BankRequestModel requestModel){
        var mapper = new ModelMapper();

        var bankDto = mapper.map(requestModel, BankDto.class);
        var createdBankDto = bankService.save(bankDto);
        var response = mapper.map(createdBankDto, BankRest.class);

        return response;
    }




}
