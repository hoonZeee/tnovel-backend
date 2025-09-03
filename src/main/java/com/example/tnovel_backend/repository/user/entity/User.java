package com.example.tnovel_backend.repository.user.entity;

import com.example.tnovel_backend.repository.payment.entity.Subscription;
import com.example.tnovel_backend.repository.post.entity.Comment;
import com.example.tnovel_backend.repository.post.entity.Post;
import com.example.tnovel_backend.repository.post.entity.PostReport;
import com.example.tnovel_backend.repository.user.entity.vo.Role;
import com.example.tnovel_backend.repository.user.entity.vo.Status;
import com.example.tnovel_backend.security.oauth2.resource.OAuth2Resource;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    private String name;

    private String profileImage;

    private String phoneNumberEncode;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    private List<AuthAccount> authAccounts = new ArrayList<>();

//    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
//    private List<UserConsent> userConsents = new ArrayList<>();
//
//    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
//    private List<Post> posts = new ArrayList<>();
//
//    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
//    private List<Comment> comments = new ArrayList<>();
//
//    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
//    private List<PostReport> postReports = new ArrayList<>();
//
//    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
//    private List<Subscription> subscriptions = new ArrayList<>();


    public static User create(String username, String name, String phoneNumberEncode, LocalDate birthDate){
        return new User(
                null,
                username,
                name,
                null,
                phoneNumberEncode,
                birthDate,
                Role.USER,
                Status.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>()
//                new ArrayList<>(),
//                new ArrayList<>(),
//                new ArrayList<>(),
//                new ArrayList<>(),
//                new ArrayList<>()
        );
    }

    public static User create(OAuth2Resource resource) {
        return new User(
                null,
                resource.getProvider().name() + "_" + resource.getProviderId(), // UNIQUE 보장
                null,
                null,
                null,
                null,
                Role.USER,
                Status.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>()
//                new ArrayList<>(),
//                new ArrayList<>(),
//                new ArrayList<>(),
//                new ArrayList<>(),
//                new ArrayList<>()
        );
    }

    @SuppressWarnings("unchecked")
    public static User createFromKakao(Map<String, Object> kakaoUserInfo) {
        String providerUserId = String.valueOf(kakaoUserInfo.get("id"));
        Map<String, Object> account = (Map<String, Object>) kakaoUserInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        String nickname = profile != null ? (String) profile.get("nickname") : null;

        return new User(
                null,
                "KAKAO_" + providerUserId,
                nickname,
                null,
                null,
                null,
                Role.USER,
                Status.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>()
//                new ArrayList<>(),
//                new ArrayList<>(),
//                new ArrayList<>(),
//                new ArrayList<>(),
//                new ArrayList<>()
        );
    }


}
