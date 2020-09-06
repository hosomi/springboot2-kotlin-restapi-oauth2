package com.hosomi.restapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class DatabaseConfig {

    @Value("\${spring.datasource.url}")
    private lateinit var dataSourceUrl: String

    @Value("\${spring.datasource.driver-class-name}")
    private lateinit var dbDriverClassName: String

    @Value("\${spring.datasource.username}")
    private lateinit var dbUserName: String

    @Value("\${spring.datasource.password}")
    private lateinit var dbPassword: String

    @Bean
    fun dataSource(): DataSource = DriverManagerDataSource().apply {
        setDriverClassName(dbDriverClassName)
            url = dataSourceUrl
            username = dbUserName
            password = dbPassword
    }
}