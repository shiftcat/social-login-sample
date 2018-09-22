package com.example.web.oauth;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

/**
 * Created by KimYJ on 2017-05-18.
 *
 * https://github.com/young891221/spring-boot-social-comment
 */
public class ClientResources
{

    @NestedConfigurationProperty
    private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

    @NestedConfigurationProperty
    private ResourceServerProperties resource = new ResourceServerProperties();


    public AuthorizationCodeResourceDetails getClient()
    {
        return client;
    }


    public ResourceServerProperties getResource()
    {
        return resource;
    }

}
