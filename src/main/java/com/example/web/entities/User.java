package com.example.web.entities;

import com.example.web.oauth.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by KimYJ on 2017-05-23.
 * https://github.com/young891221/spring-boot-social-comment
 */
@Getter
@NoArgsConstructor
public class User implements Serializable
{
    private static final long serialVersionUID = -1790606364187219092L;

    private long userIdx;

    private String userPrincipal;

    private String userName;

    private String userEmail;

    private String userImage;

    private SocialType socialType;

    private LocalDateTime createDate;

    @Builder
    public User(String userPrincipal, String userName, String userEmail, String userImage, SocialType socialType) {
        this.userPrincipal = userPrincipal;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImage = userImage;
        this.socialType = socialType;
        this.createDate = LocalDateTime.now();
    }

}
