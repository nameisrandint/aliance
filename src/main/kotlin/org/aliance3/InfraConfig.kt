package org.aliance3

import liquibase.integration.spring.SpringLiquibase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
class InfraConfig {

    @Bean
    fun jdbcTemplate(dataSource: DataSource?): JdbcTemplate {
        return JdbcTemplate(dataSource!!)
    }

    @Bean
    fun liquibase(dataSource: DataSource?): SpringLiquibase? {
        val liquibase = SpringLiquibase()
        liquibase.dataSource = dataSource
        liquibase.changeLog = "classpath:liquibase-changelog.xml"
        return liquibase
    }
}