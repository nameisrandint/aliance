package org.aliance3.alliance

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicInteger

@Repository
class AlianceDao(private val jdbcTemplate: JdbcTemplate) {

    fun selectPageState(userId: Int): PageState {
        val levels = selectLevelIds(userId).asSequence()
            .map { selectLevel(it) }
            .toList()

        val all = AtomicInteger(0)
        val completed = AtomicInteger(0)

        levels.asSequence()
            .flatMap { it.cards }
            .flatMap { it.subTasks }
            .forEach { all.incrementAndGet(); if (it.isDone) completed.incrementAndGet() }

        return PageState(
            levels,
            (completed.get() * 100.0 / all.get()).toInt()
        )
    }

    fun selectLevelIds(userId: Int): List<Int> {
        val sql = """
            select l.id as id
            from level l
            join aliance_user au on au.id = l.user_id
            where au.id = $userId
        """.trimIndent()
        return jdbcTemplate.query(sql) { rs, _ -> rs.getInt("id")}
    }

    fun selectLevel(levelId: Int): Level {
        val isActive = isLevelActive(levelId)!!
        val cards = selectCards(levelId)
        return Level(isActive, cards)
    }


    fun isLevelActive(userId: Int):Boolean? {
        val sql = """
            select
                is_active
            from aliance_user au
                join level l on au.id = l.user_id
            where au.id = $userId
        """.trimIndent()
        return jdbcTemplate.queryForObject(sql, Boolean::class.java)
    }

    fun selectCards(levelId: Int): List<Card> {
        val sql = """
            select
                title,
                difficulty,
                s.name as status,
                ct.icon as icon,
                ct.block as block,
                ct.color as color,
                c.id as card_id
            from level l
            join card c on l.id = c.level_id
            join card_type ct on c.card_type_id = ct.id
            join status s on c.status_id = s.id
            where user_id = $levelId;
        """.trimIndent()
        return jdbcTemplate.query(sql) { rs, n ->
            Card(
                rs.getString("title"),
                rs.getInt("difficulty"),
                Status.from(rs.getString("status")),
                CardType(
                    rs.getString("block"),
                    rs.getString("color"),
                    rs.getString("icon")
                ),
                selectSubTasksForCard(rs.getInt("card_id"))
            )
        }
    }

    fun selectSubTasksForCard(cardId: Int): List<SubTask> {
        val sql = """
            select
                header,
                main_body,
                is_done,
                id
            from sub_task
            where card_id = $cardId
        """.trimIndent()
        return jdbcTemplate.query(sql) { rs, n -> SubTask(
            rs.getInt("id"),
            rs.getString("header"),
            rs.getString("main_body"),
            rs.getBoolean("is_done")
        )
        }
    }

    fun markSubTaskAsCompleted(subTaskId: Int) {
        val sql = """
            update sub_task
            set is_done = not is_done
            where id = $subTaskId
        """.trimIndent()
        jdbcTemplate.update(sql)
    }

    fun updateCardStatus(cardId: Int, statusId: Int) {
        val sql = """
            update card
            set status_id = $statusId
            where id = $cardId
        """.trimIndent()
        jdbcTemplate.update(sql)
    }

    fun isAllSubTasksDone(cardId: Int): Boolean {
        val sql = """
            select count(1) < 1 as cond
            from card c
            join sub_task st on st.card_id = c.id
            where is_done = false
        """.trimIndent()
        return jdbcTemplate.queryForObject(sql) { rs, n ->  rs.getBoolean("cond") }!!
    }

    fun isAnySubTaskInProgress(cardId: Int): Boolean {
        val sql = """
            select count(1) > 0 as cond
            from card c
            join sub_task st on st.card_id = c.id
            where is_done = true
        """.trimIndent()
        return jdbcTemplate.queryForObject(sql) { rs, _ ->  rs.getBoolean("cond") }!!
    }

    fun cardIdBySubTask(taskId: Int): Int {
        val sql = "select card_id from sub_task where id = $taskId"
        return jdbcTemplate.queryForObject(sql) { rs, _ ->  rs.getInt("card_id") }!!
    }
}