server :
  port : 8000

debug : true

facebook :
    client :
        clientId : 
        clientSecret : 
        accessTokenUri : https://graph.facebook.com/oauth/access_token
        userAuthorizationUri : https://www.facebook.com/dialog/oauth?display=popup
        tokenName : oauth_token
        authenticationScheme : query
        clientAuthenticationScheme : form
        # scope : email, profile
    resource :
        userInfoUri : https://graph.facebook.com/me?fields=id,name,email,link

google :
    client :
        clientId : 
        clientSecret : 
        accessTokenUri : https://accounts.google.com/o/oauth2/token
        userAuthorizationUri : https://accounts.google.com/o/oauth2/auth
        clientAuthenticationScheme : form
        scope : 
        - email
        - profile
    resource :
        userInfoUri : https://www.googleapis.com/oauth2/v2/userinfo

kakao :
    client :
        clientId : 
        clientSecret : 
        accessTokenUri : https://kauth.kakao.com/oauth/token
        userAuthorizationUri : https://kauth.kakao.com/oauth/authorize
    resource :
        userInfoUri : https://kapi.kakao.com/v2/user/me

naver :
    client :
        clientId : 
        clientSecret : 
        accessTokenUri : https://nid.naver.com/oauth2.0/token
        userAuthorizationUri : https://nid.naver.com/oauth2.0/authorize
    resource :
        userInfoUri : https://openapi.naver.com/v1/nid/me
