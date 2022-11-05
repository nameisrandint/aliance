package org.aliance3.common

import org.postgresql.ds.PGSimpleDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.sql.DataSource

@Profile("prod")
@Configuration
class ProdConfig {

    @Bean
    fun dataSource(cred: CredentialsProvider): DataSource {
        val props = cred.load()
        val ds = PGSimpleDataSource()
        ds.serverNames = arrayOf(props["serverName"])
        ds.portNumbers = arrayOf(props["portNumber"]!!.toInt()).toIntArray()
        ds.databaseName = props["databaseName"]
        ds.user = props["user"]
        ds.password = props["password"]
        return ds
    }
}