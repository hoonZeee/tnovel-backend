package com.example.tnovel_backend.repository.user;

import com.example.tnovel_backend.repository.user.entity.AuthAccount;
import com.example.tnovel_backend.repository.user.entity.LocalCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalCredentialRepository extends JpaRepository<LocalCredential, Integer> {

    Optional<LocalCredential> findByAuthAccount_User_PhoneNumberEncode(String phoneNumberEncode);


    Optional<LocalCredential> findByAuthAccount(AuthAccount authAccount);
}
