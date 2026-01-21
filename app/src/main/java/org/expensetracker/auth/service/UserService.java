package org.expensetracker.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.expensetracker.auth.dto.UserInfoDto;
import org.expensetracker.auth.entity.UserInfo;
import org.expensetracker.auth.entity.UserRole;
import org.expensetracker.auth.repository.UserInfoRepository;
import org.expensetracker.auth.repository.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void  signupUser(UserInfoDto userInfoDto) {

        if (userRepository.findByUsername(userInfoDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        String encodedPassword =
                passwordEncoder.encode(userInfoDto.getPassword());

        UserRole userRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));

        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);

        userRepository.save(
                new UserInfo(
                        null,
                        userInfoDto.getUsername(),
                        encodedPassword,
                        roles
                )
        );

    }


}
