package com.example.tnovel_backend.service.application.admin;

import com.example.tnovel_backend.configuration.PhoneEncryptor;
import com.example.tnovel_backend.controller.admin.dto.request.AdminLoginRequestDto;
import com.example.tnovel_backend.controller.admin.dto.request.AdminSignUpRequestDto;
import com.example.tnovel_backend.controller.user.dto.response.LoginResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.SignUpResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.UserSimpleResponseDto;
import com.example.tnovel_backend.exception.domain.UserException;
import com.example.tnovel_backend.exception.error.UserErrorCode;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AuthAccountRepository authAccountRepository;
    private final LocalCredentialRepository localCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneEncryptor phoneEncryptor;
    private final JwtProvider jwtProvider;


    @Transactional
    public UserSimpleResponseDto updateUserStatus(Integer userId, Status newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (user.getStatus() == newStatus) {
            throw new UserException(UserErrorCode.ALREADY_IN_STATUS);
        }

        user.ban();

        return UserSimpleResponseDto.from(user);
    }

    @Transactional(readOnly = true)
    public Page<UserSimpleResponseDto> getAllUsersOrderByCreatedAtDesc(Pageable pageable) {
        return userRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(UserSimpleResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<UserSimpleResponseDto> searchByUsername(String keyword, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCaseOrderByCreatedAtDesc(keyword, pageable)
                .map(UserSimpleResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<UserSimpleResponseDto> searchByName(String keyword, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCaseOrderByCreatedAtDesc(keyword, pageable)
                .map(UserSimpleResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<UserSimpleResponseDto> searchByCreatedAt(LocalDate date, Pageable pageable) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return userRepository.findByCreatedAtBetween(start, end, pageable)
                .map(UserSimpleResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<UserSimpleResponseDto> searchByStatus(Status status, Pageable pageable) {
        return userRepository.findByStatusOrderByCreatedAtDesc(status, pageable)
                .map(UserSimpleResponseDto::from);
    }


    @Transactional
    public LoginResponseDto loginAdmin(AdminLoginRequestDto request) {
        AuthAccount authAccount = authAccountRepository.findByProviderAndProviderUserId(
                Provider.LOCAL,
                request.getUsername()
        ).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        LocalCredential credential = localCredentialRepository.findByAuthAccount(authAccount)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), credential.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        User user = authAccount.getUser();

        if (user.getStatus() != Status.ACTIVE) {
            throw new UserException(UserErrorCode.ACCOUNT_INACTIVE);
        }

        // 관리자 권한 확인
        if (user.getRole() != Role.ADMIN) {
            throw new UserException(UserErrorCode.FORBIDDEN);
        }

        user.updateLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                credential,
                null,
                credential.getAuthorities()
        );
        String accessToken = jwtProvider.generate(authentication);

        return new LoginResponseDto(accessToken);
    }


    @Transactional
    public SignUpResponseDto signupAdmin(AdminSignUpRequestDto request) {
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
                null,
                Role.ADMIN
        );

        userRepository.save(user);

        AuthAccount authAccount = AuthAccount.create(
                user,
                request.getUsername(),
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


}
