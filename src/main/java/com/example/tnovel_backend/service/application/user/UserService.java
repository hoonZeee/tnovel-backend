package com.example.tnovel_backend.service.application.user;

import com.example.tnovel_backend.configuration.PhoneEncryptor;
import com.example.tnovel_backend.controller.user.dto.request.LocalSignUpRequestDto;
import com.example.tnovel_backend.controller.user.dto.response.SignUpResponseDto;
import com.example.tnovel_backend.repository.user.AuthAccountRepository;
import com.example.tnovel_backend.repository.user.LocalCredentialRepository;
import com.example.tnovel_backend.repository.user.UserRepository;
import com.example.tnovel_backend.repository.user.entity.AuthAccount;
import com.example.tnovel_backend.repository.user.entity.LocalCredential;
import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.repository.user.entity.vo.Provider;
import com.example.tnovel_backend.repository.user.entity.vo.Role;
import com.example.tnovel_backend.repository.user.entity.vo.Status;
import com.example.tnovel_backend.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthAccountRepository authAccountRepository;
    private final LocalCredentialRepository localCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneEncryptor phoneEncryptor;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignUpResponseDto signupLocal(LocalSignUpRequestDto request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 아이디(username)입니다.");
        }

        if (userRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임(name)입니다.");
        }

        String encryptedPhone = phoneEncryptor.encrypt(request.getPhoneNumber());

        User user = User.create(
                request.getUsername(),
                request.getName(),
                encryptedPhone
        );
        userRepository.save(user);

        AuthAccount authAccount = AuthAccount.create(
                user,
                request.getUsername(),  // 로컬에서는 username을 providerUserId로 씀
                Provider.LOCAL
        );
        authAccountRepository.save(authAccount);

        LocalCredential credential = LocalCredential.create(
                passwordEncoder.encode(request.getPassword()),
                authAccount
        );
        localCredentialRepository.save(credential);

        String accessToken = jwtProvider.generate(user.getUsername(), user.getRole().name());

        return SignUpResponseDto.from(user, accessToken);

    }




    @Override
    public UserDetails loadUserByUsername(String phoneNumberEncode) throws UsernameNotFoundException {
        return localCredentialRepository.findByAuthAccount_User_PhoneNumberEncode(phoneNumberEncode)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "존재하지 않는 유저입니다 - phoneNumberEncode : " + phoneNumberEncode
                ));
    }




}
