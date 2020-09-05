# Spring Boot + Kotlin + Gradle + Spring Security OAuth2

## Spring Initializr

[Spring Initializr](https://start.spring.io/) から雛形生成。  
 

## OAuth2

``build.gradle.kts`` に追加で定義が必要です、``Spring Initializr`` では選択できません。   


``build.gradle.kts`` :    

```kotlin
implementation("org.springframework.security.oauth:spring-security-oauth2:2.5.0.RELEASE")
```

:link: [Announcing the Spring Authorization Server](https://spring.io/blog/2020/04/15/announcing-the-spring-authorization-server)  


### API

cURL を Windows で使う場合、`（シングルクォーテーション） から "（ダブルクォーテーション）に変更してください。    
ここでのサンプルは Windows 用の cURL を利用しています。    

:link: [curl for Windows](https://curl.haxx.se/windows/)  

#### API : 認証無し

```powershell
PS > ./curl --location --request GET "http://localhost:8080/api/hello"
Hello!
PS > ./curl --location --request GET "http://localhost:8080/api/hello/hosomi"
{"name":"hosomi","message":"Hello hosomi !"}
```

#### API : 認証有り

##### token の取得と実行：  

```powershell
PS > ./curl --location --request POST "http://localhost:8080/oauth/token" --header "Authorization: Basic Y2xpZW50OnNlY3JldA==" --header "Cookie: JSESSIONID=6EB182996D193B89B2834D133394D0FC" --form "grant_type=client_credentials"
```

```powershell
PS > ./curl --location --request GET "http://localhost:8080/api/public/hello" --header "Authorization: Bearer IrN3BHhnNisaNLwn/7iKSBM23Qk="
Hello!
```


##### token の取得と実行（個別のユーザ単位）：  

```powerhsell
PS > ./curl --location --request POST "http://localhost:8080/oauth/token" --header "Authorization: Basic Y2xpZW50OnNlY3JldA==" --form "grant_type=password" --form "username=hosomi" --form "password=hosomipass"
{"access_token":"RQM9P/QOhEvsDTTBJhTcfupM22E=","token_type":"bearer","refresh_token":"CqwVCjohMR+iNheb6qw3Jl9+Ppw=","expires_in":43199,"scope":"user_info"}
```

```powerhsell
PS > ./curl --location --request GET "http://localhost:8080/api/user/hello/hosomi" --header "Authorization: Bearer RQM9P/QOhEvsDTTBJhTcfupM22E="
{"name":"hosomi","message":"Hello hosomi !"}
```

