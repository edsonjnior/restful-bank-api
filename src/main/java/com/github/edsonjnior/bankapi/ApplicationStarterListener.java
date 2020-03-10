package com.github.edsonjnior.bankapi;

import com.github.edsonjnior.bankapi.domains.SecurityConstants;
import com.github.edsonjnior.bankapi.entities.BankEntity;
import com.github.edsonjnior.bankapi.entities.BranchEntity;
import com.github.edsonjnior.bankapi.entities.RoleEntity;
import com.github.edsonjnior.bankapi.entities.UserEntity;
import com.github.edsonjnior.bankapi.enums.EntryType;
import com.github.edsonjnior.bankapi.enums.TransactionType;
import com.github.edsonjnior.bankapi.repositories.BankRepository;
import com.github.edsonjnior.bankapi.repositories.BranchRepository;
import com.github.edsonjnior.bankapi.repositories.RoleRepository;
import com.github.edsonjnior.bankapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class ApplicationStarterListener implements ApplicationListener<ApplicationReadyEvent> {

    private final static Logger LOGGER = Logger.getLogger(ApplicationStarterListener.class.getName());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RoleRepository roleRepository;

    private SecurityConstants constants;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOGGER.info("Creating users...");
        createUser("Admin", "admin@email.com", "123456", constants.ROLE_ADMIN, constants.ROLE_CLIENT);
        createUser("User", "user@email.com", "123456", constants.ROLE_CLIENT);

        LOGGER.info("Creating banks...");
        BankEntity bankEntityBB = bankRepository.save(new BankEntity("Banco do Brasil SA", "001"));
        BankEntity bankEntityBra = bankRepository.save(new BankEntity("Banco Bradesco SA", "237"));

        LOGGER.info("Creating branches...");
        branchRepository.save(new BranchEntity("Agencia Jockey", "3178", bankEntityBB));
        branchRepository.save(new BranchEntity("Agencia Centro", "405", bankEntityBra));

    }

    private void createUser(String name, String email, String password, String... roles) {
        try {
            Set<RoleEntity> rolesSet = Arrays.stream(roles)
                    .map(s -> {
                        RoleEntity role = roleRepository.findByName(s);
                        if (role != null) return role;

                        return roleRepository.save(new RoleEntity(null, s));
                    }).collect(Collectors.toSet());

            userRepository.save(new UserEntity(name, email, passwordEncoder.encode(password), rolesSet));
            LOGGER.info(String.format("%s with Password: %s has been created", email, password));
        } catch (RuntimeException e) {
            LOGGER.severe(e.getLocalizedMessage());
        }
    }
}
