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

    fun setInt(key: String, value: Int) {
        jdbcTemplate.update(
            """
                update key_value
                set value = '$value'
                where key = '$key'
            """.trimIndent()
        )
    }

    fun getInt(key: String): Int? = get(key)?.toInt()

    fun getBool(key: String) = get(key).toBoolean()

    fun switchBool(key: String) {
        val sql = """
            update key_value
            set value = CASE
                when value = 'true' then 'false'
                when value = 'false' then 'true'
                else value
            end
            where key = '$key'
        """.trimIndent()
        jdbcTemplate.update(sql)
    }

    fun set(key: String, value: String) {
        val sql = """
            insert into key_value (key, value) 
            values ('$key', '$value')
            on conflict (key) do update
            set value = '$value'
            """
        jdbcTemplate.update(sql)
    }

    fun set(key: String, value: Boolean) {
        val asStr = value.toString();
        set(key, asStr)
    }

    fun setFalse(key: String) {
        set(key, false)
    }

    fun setTrue(key: String) {
        set(key, true)
    }
}