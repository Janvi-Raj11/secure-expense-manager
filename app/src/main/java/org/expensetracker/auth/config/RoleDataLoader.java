package org.expensetracker.auth.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.expensetracker.auth.entity.UserRole;
import org.expensetracker.auth.repository.UserRoleRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDataLoader {

    private final UserRoleRepository roleRepository;

    @PostConstruct
    public void loadRoles() {
        if (roleRepository.findByRoleName("ROLE_USER").isEmpty()) {
            roleRepository.save(
                    UserRole.builder()
                            .roleName("ROLE_USER")
                            .build()
            );
        }
    }
}
