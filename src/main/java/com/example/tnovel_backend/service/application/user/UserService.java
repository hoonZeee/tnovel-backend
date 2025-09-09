package com.example.tnovel_backend.service.application.user;

import com.example.tnovel_backend.configuration.PhoneEncryptor;
import com.example.tnovel_backend.controller.user.dto.request.LocalLoginRequestDto;
import com.example.tnovel_backend.controller.user.dto.request.LocalSignUpRequestDto;
import com.example.tnovel_backend.controller.user.dto.response.LoginResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.SignUpResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.UserSimpleResponseDto;
import com.example.tnovel_backend.exception.domain.UserException;
import com.example.tnovel_backend.exception.error.UserErrorCode;
import com.example.tnovel_backend.repository.user.AuthAccountRepository;
import com.example.tnovel_backend.repository.user.LocalCredentialRepository;
import com.example.tnovel_backend.repository.user.UserConsentRepository;
import com.example.tnovel_backend.repository.user.UserRepository;
import com.example.tnovel_backend.repository.user.entity.AuthAccount;
import com.example.tnovel_backend.repository.user.entity.LocalCredential;
import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.repository.user.entity.UserConsent;
import com.example.tnovel_backend.repository.user.entity.vo.Provider;
import com.example.tnovel_backend.repository.user.entity.vo.Status;
import com.example.tnovel_backend.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthAccountRepository authAccountRepository;
    private final LocalCredentialRepository localCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneEncryptor phoneEncryptor;
    private final JwtProvider jwtProvider;
    private final UserConsentRepository userConsentRepository;

    @Transactional
    public UserSimpleResponseDto makeDormant(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.dormant();
        userRepository.save(user);
        return UserSimpleResponseDto.from(user);
    }

    @Transactional
    public UserSimpleResponseDto withdraw(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.withdraw();
        userRepository.save(user);
        return UserSimpleResponseDto.from(user);
    }


    @Transactional
    public LoginResponseDto loginLocal(LocalLoginRequestDto request) {
        AuthAccount authAccount;

        if (request.getUsername() != null) {
            authAccount = authAccountRepository.findByProviderAndProviderUserId(Provider.LOCAL, request.getUsername())
                    .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        } else if (request.getPhoneNumber() != null) {
            String encryptedPhone = phoneEncryptor.encrypt(request.getPhoneNumber());
            authAccount = authAccountRepository.findByProviderAndUser_PhoneNumberEncode(Provider.LOCAL, encryptedPhone)
                    .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        } else {
            throw new UserException(UserErrorCode.LOGIN_ID_REQUIRED);
        }

        LocalCredential credential = localCredentialRepository.findByAuthAccount(authAccount)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), credential.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        User user = authAccount.getUser();
        if (user.getStatus() != Status.ACTIVE) {
            throw new UserException(UserErrorCode.ACCOUNT_INACTIVE);
        }

        user.updateLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(credential, null, credential.getAuthorities());
        String accessToken = jwtProvider.generate(authentication);
        return new LoginResponseDto(accessToken);

    }


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

        if (request.getConsents() != null) {
            List<UserConsent> consents = request.getConsents().stream()
                    .map(type -> UserConsent.create(user, type))
                    .toList();
            userConsentRepository.saveAll(consents);
        }


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
