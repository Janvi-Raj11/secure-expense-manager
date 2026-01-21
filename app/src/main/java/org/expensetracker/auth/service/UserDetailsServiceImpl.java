package org.expensetracker.auth.service;

import lombok.RequiredArgsConstructor;
import org.expensetracker.auth.entity.UserInfo;
import org.expensetracker.auth.repository.UserInfoRepository;
import org.expensetracker.auth.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserInfoRepository userRepository;

    private static final Logger log =
            LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserInfo user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
