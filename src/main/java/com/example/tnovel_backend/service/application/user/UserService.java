package com.example.tnovel_backend.service.application.user;

import com.example.tnovel_backend.configuration.PhoneEncryptor;
import com.example.tnovel_backend.controller.user.dto.request.LocalSignUpRequestDto;
import com.example.tnovel_backend.controller.user.dto.response.JwtResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.SignUpResponseDto;
import com.example.tnovel_backend.exception.domain.UserException;
import com.example.tnovel_backend.exception.error.UserErrorCode;
import com.example.tnovel_backend.repository.user.AuthAccountRepository;
import com.example.tnovel_backend.repository.user.LocalCredentialRepository;
import com.example.tnovel_backend.repository.user.UserRepository;
import com.example.tnovel_backend.repository.user.entity.AuthAccount;
import com.example.tnovel_backend.repository.user.entity.LocalCredential;
import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.repository.user.entity.vo.Provider;
import com.example.tnovel_backend.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


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
            throw new UserException(UserErrorCode.DUPLICATE_USERNAME);
        }

        if (userRepository.findByName(request.getName()).isPresent()) {
            throw new UserException(UserErrorCode.DUPLICATE_NICKNAME);
        }

        String encryptedPhone = phoneEncryptor.encrypt(request.getPhoneNumber());

        User user = User.create(
                request.getUsername(),
                request.getName(),
                encryptedPhone,
                request.getBirthDate()
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

        Authentication authentication = new UsernamePasswordAuthenticationToken(credential, null, credential.getAuthorities());
        String accessToken = jwtProvider.generate(authentication);

        return SignUpResponseDto.from(user, accessToken);

    }




    @Override
    public UserDetails loadUserByUsername(String phoneNumberEncode) throws UsernameNotFoundException {
        return localCredentialRepository.findByAuthAccount_User_PhoneNumberEncode(phoneNumberEncode)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }




}
