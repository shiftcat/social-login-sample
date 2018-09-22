package com.example.web.oauth;

/**
 * Created by KimYJ on 2017-05-18.
 *
 * https://github.com/young891221/spring-boot-social-comment
 *
 */
public enum SocialType
{

    FACEBOOK("facebook"),
    TWITTER("twitter"),
    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver");


    private final String ROLE_PREFIX = "ROLE_";

    private String name;

    SocialType(String name)
    {
        this.name = name;
    }

    public String getRoleType() { return ROLE_PREFIX + name.toUpperCase(); }

    public String getValue() { return name; }

    public boolean isEquals(String authority)
    {
        return this.getRoleType().equals(authority);
    }
}
