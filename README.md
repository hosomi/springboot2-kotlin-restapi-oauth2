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

