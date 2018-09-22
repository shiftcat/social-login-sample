package com.example.web.aop;

import com.example.web.entities.User;
import com.example.web.oauth.SocialType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KimYJ on 2017-06-01.
 *
 * https://github.com/young891221/spring-boot-social-comment
 */
@Component
@Aspect
@Slf4j
public class UserAspect
{

    @Around("execution(* *(.., @com.example.web.annotation.SocialUser (*), ..))")
    public Object convertUser(ProceedingJoinPoint joinPoint) throws Throwable
    {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        User user = (User) session.getAttribute("user");
        if(user == null) {
            OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
            Map<String, String> map = (HashMap<String, String>) authentication.getUserAuthentication().getDetails();
            user = checkSocialType(String.valueOf(authentication.getAuthorities().toArray()[0]), map);
        }

        User finalUser = user;
        Object[] args =
                Arrays.stream(joinPoint.getArgs())
                        .map(data -> {
                                if(data instanceof User) {
                                    data = finalUser;
                                }
                                return data;
                            })
                        .toArray();

        return joinPoint.proceed(args);
    }


    private User checkSocialType(String authority, Map<String, String> map)
    {
        if(SocialType.FACEBOOK.isEquals(authority)) return saveFacebook(map);
        else if(SocialType.GOOGLE.isEquals(authority)) return saveGoogle(map);
        else if(SocialType.KAKAO.isEquals(authority)) return saveKakao(map);
        else if(SocialType.NAVER.isEquals(authority)) return saveNaver(map);
        return null;
    }



    private User saveFacebook(Map<String, String> map)
    {
        log.info("Save facebook => {}", map);
        return User.builder()
                .userPrincipal(map.get("id"))
                .userName(map.get("name"))
                .userEmail(map.get("email"))
                .userImage("http://graph.facebook.com/" + map.get("id") + "/picture?type=square")
                .socialType(SocialType.FACEBOOK)
                .build();
    }

    private User saveGoogle(Map<String, String> map)
    {
        log.info("Save google => {}", map);
        return User.builder()
                .userPrincipal(map.get("id"))
                .userName(map.get("name"))
                .userEmail(map.get("email"))
                .userImage(map.get("picture"))
                .socialType(SocialType.GOOGLE)
                .build();
    }


    private User saveKakao(Map<String, String> map)
    {
        log.info("Save kakao => {}", map);
        HashMap<String, String> propertyMap = (HashMap<String, String>)(Object) map.get("properties");
        HashMap<String, String> accountMap = (HashMap<String, String>)(Object) map.get("kakao_account");
        return User.builder()
                .userPrincipal(String.valueOf(map.get("id")))
                .userName(propertyMap.get("nickname"))
                .userEmail(accountMap.get("email"))
                .userImage("")
                .socialType(SocialType.KAKAO)
                .build();
    }


    private User saveNaver(Map<String, String> map)
    {
        log.info("Save naver => {}", map);
        HashMap<String, String> propertyMap = (HashMap<String, String>)(Object) map.get("response");
        return User.builder()
                .userPrincipal(String.valueOf(propertyMap.get("id")))
                .userName(propertyMap.get("nickname"))
                .userEmail(propertyMap.get("email"))
                .userImage(propertyMap.get("profile_image"))
                .socialType(SocialType.NAVER)
                .build();
    }
}

