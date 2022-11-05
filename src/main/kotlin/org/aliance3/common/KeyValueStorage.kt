package org.aliance3.common

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class KeyValueStorage {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    fun get(key: String): String? {
        return try {
            val sql = "select value from key_value where key = '$key'"
            jdbcTemplate.queryForObject(sql, String::class.java)
        } catch (e: IncorrectResultSizeDataAccessException) {
            null
        }
    }

    fun getInt(key: String): Int? = get(key)?.toInt()

    fun set(key: String, value: String) {
        val sql = """
            insert into key_value (key, value) 
            values ('$key', '$value')
            on conflict (key) do update
            set value = '$value'
            """
        jdbcTemplate.update(sql)
    }
}