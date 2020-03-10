package com.github.edsonjnior.bankapi.controllers;

import com.github.edsonjnior.bankapi.domains.SecurityConstants;
import com.github.edsonjnior.bankapi.services.BranchesService;
import com.github.edsonjnior.bankapi.shared.dto.BankDto;
import com.github.edsonjnior.bankapi.shared.dto.BranchDto;
import com.github.edsonjnior.bankapi.ui.model.request.BranchRequestModel;
import com.github.edsonjnior.bankapi.ui.model.response.BranchRest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/branches")
public class BranchesController {

    @Autowired
    private BranchesService branchService;

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BranchRest> getBranches() {
        var response = new ArrayList<BranchRest>();

        var branchesDto = branchService.findAll();
        if (branchesDto != null && !branchesDto.isEmpty()) {
            var listType = new TypeToken<List<BranchRest>>() {
            }.getType();
            response = new ModelMapper().map(branchesDto, listType);
        }

        return response;
    }

    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({SecurityConstants.ROLE_ADMIN})
    public BranchRest create(@RequestBody BranchRequestModel requestModel){
        var mapper = new ModelMapper();

        var branchDto = mapper.map(requestModel, BranchDto.class);
        branchDto.setBank(new BankDto(requestModel.getBank()));

        var createdBranchDto = branchService.save(branchDto);
        var response = mapper.map(createdBranchDto, BranchRest.class);

        return response;
    }
}
