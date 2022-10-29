package org.aliance3

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class KeyValueDao {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    fun get(key: String): String? {
        val sql = "select value from key_value where key = '$key'"
        return jdbcTemplate.queryForObject(sql, String::class.java)
    }

    fun set(key: String, value: String) {
        val sql = "insert into key_value (key, value) values ('$key', '$value')"
        jdbcTemplate.update(sql)
    }
}