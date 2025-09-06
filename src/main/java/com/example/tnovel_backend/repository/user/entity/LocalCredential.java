package com.example.tnovel_backend.repository.user.entity;

import com.example.tnovel_backend.repository.user.entity.vo.Provider;
import com.example.tnovel_backend.repository.user.entity.vo.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalCredential implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String password;
    private LocalDateTime passwordUpdatedAt;
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "auth_account_id", unique = true)
    private AuthAccount authAccount;

    public User getUser() {
        return authAccount.getUser();
    }


    public static LocalCredential create(String password, AuthAccount authAccount) {
        return new LocalCredential(
                null,
                password,
                LocalDateTime.now(),
                LocalDateTime.now(),
                authAccount
        );
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.passwordUpdatedAt == null) {
            this.passwordUpdatedAt = this.createdAt;
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + authAccount.getUser().getRole().name()));
    }

    @Override
    public String getUsername() {
        return authAccount.getUser().getUsername();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return authAccount.getUser().getStatus() == Status.ACTIVE;
    }
}
