package com.example.web.oauth;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by KimYJ on 2017-05-18.
 *
 * https://github.com/young891221/spring-boot-social-comment
 */
@Slf4j
public class UserTokenService extends UserInfoTokenServices
{


    public UserTokenService(ClientResources resources, SocialType socialType)
    {
        super(resources.getResource().getUserInfoUri(), resources.getClient().getClientId());
        setAuthoritiesExtractor(new OAuth2AuthoritiesExtractor(socialType));
        setPrincipalExtractor(principalExtractor(socialType));
    }



    private PrincipalExtractor principalExtractor(SocialType type)
    {
        return new FixedPrincipalExtractor() {
            @Override
            public Object extractPrincipal(Map<String, Object> map) {
                log.info("extractPrincipal => {}", map);
                return super.extractPrincipal(map);
            }
        };
    }


    public static class OAuth2AuthoritiesExtractor implements AuthoritiesExtractor
    {
        private String socialType;

        public OAuth2AuthoritiesExtractor(SocialType socialType)
        {
            this.socialType = socialType.getRoleType();
        }


        @Override
        public List<GrantedAuthority> extractAuthorities(Map<String, Object> map)
        {
            log.info("extractAuthorities => {}", map);
            return AuthorityUtils.createAuthorityList(this.socialType);
        }
    }

}
