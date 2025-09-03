package com.example.tnovel_backend.repository.user;

import com.example.tnovel_backend.repository.user.entity.AuthAccount;
import com.example.tnovel_backend.repository.user.entity.vo.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthAccountRepository extends JpaRepository<AuthAccount, Integer> {
    Optional<AuthAccount> findByProviderAndProviderUserId(Provider provider, String providerUserId);

    Optional<AuthAccount> findByProviderAndUser_PhoneNumberEncode(Provider provider, String encryptedPhone);
}
