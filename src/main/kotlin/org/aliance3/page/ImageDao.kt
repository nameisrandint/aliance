package org.aliance3.page

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.io.File
import java.io.FileOutputStream

@Repository
class ImageDao(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

//    fun insertImage(name: String, file: File) {
//        insertImage(name, Files.readAllBytes(file.toPath()))
//    }

    fun insertImage(name: String, byteArray: ByteArray) {
        val sql = """
            insert into image (name, content)
            values (:name, :content)
            on conflict (name) do nothing 
        """.trimIndent()
        val params = MapSqlParameterSource()
            .addValue("name", name)
            .addValue("content", byteArray)
        namedParameterJdbcTemplate.update(sql, params)
    }

//    fun selectImages(): List<File> {
//        val sql = """
//            select content from image
//        """.trimIndent()
//        val bytes = namedParameterJdbcTemplate.query(sql, MapSqlParameterSource()) {
//                rs, _ -> rs.getBytes(1)
//        }
//        val files = ArrayList<File>()
//        for (byteArr in bytes) {
//            val file = toFile(byteArr)
//            files.add(file)
//        }
//        return files
//    }

    fun selectImageIdFromCurrent(current: Int, shift: Int): Int? {
        val sql = """
            with current_updated_at (current_updated_at_var) AS (
                select updated_at
                from image
                where id = $current
            ), next_shift AS (
                select id, updated_at
                from image, current_updated_at
                where image.updated_at > current_updated_at_var
                limit $shift
            )
            select id
            from next_shift
            order by updated_at desc
            limit 1
        """.trimIndent()
        return try {
            namedParameterJdbcTemplate.jdbcTemplate.queryForObject(sql, Int::class.java)
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    fun selectFirstImageId(): Int? {
        val sql = """
            select id
            from image
            order by updated_at
            limit 1
        """.trimIndent()
        return namedParameterJdbcTemplate.jdbcTemplate.queryForObject(sql, Int::class.java)
    }

    fun selectImageById(id: Int): File? {
        val sql = """
            select content from image
            where id = :id
        """.trimIndent()
        val params = MapSqlParameterSource()
            .addValue("id", id)
        return try {
            namedParameterJdbcTemplate.queryForObject(sql, params) { rs, _ ->
                toFile(rs.getBytes(1))
            }!!
        } catch (e: Exception) {
            null
        }
    }

    private fun toFile(byteArr: ByteArray): File {
        val file = File.createTempFile("super", ".png")
        FileOutputStream(file, false).use { outputStream ->
            outputStream.write(byteArr, 0, byteArr.size)
        }
        return file
    }
}