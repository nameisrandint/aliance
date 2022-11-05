package org.aliance3.common


import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import javax.sql.DataSource

@Configuration
@Profile("local", "test")
class LocalConfig {

    @Bean
    @Primary
    fun dataSource(): DataSource {
        return EmbeddedPostgres.builder()
            .setPort(5432)
            .start()
            .postgresDatabase
    }
}