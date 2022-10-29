package org.aliance3

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AlianceDao(private val jdbcTemplate: JdbcTemplate) {

    fun selectPageState(userId: Int): PageState {
        return PageState(
            selectLevelIds(userId).asSequence()
                .map { selectLevel(it) }
                .toList()
        )
    }

    fun selectLevelIds(userId: Int): List<Int> {
        val sql = """
            select l.id as id
            from level l
            join aliance_user au on au.id = l.user_id
            where au.id = $userId
        """.trimIndent()
        return jdbcTemplate.query(sql) { rs, n -> rs.getInt("id")}
    }

    fun selectLevel(levelId: Int): Level {
        val isActive = isLevelActive(levelId)
        val cards = selectCards(levelId)

        val all_subtasks = cards.asSequence()
            .flatMap { it.subTasks }
            .count()

        val completed_subtasks = cards.asSequence()
            .flatMap { it.subTasks }
            .filter { it.idDone }
            .count()

        return Level(
            isActive!!,
            cards,
           (completed_subtasks * 100.0 / all_subtasks).toInt()
        )
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
                ct.icon,
                ct.block,
                ct.color,
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
                is_done
            from sub_task
            where card_id = $cardId
        """.trimIndent()
        return jdbcTemplate.query(sql) { rs, n -> SubTask(
            rs.getString("header"),
            rs.getString("main_body"),
            rs.getBoolean("is_done")
        )}
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
            join status s on s.id = c.status_id
            where c.id = $cardId
                and name != 'DONE'
        """.trimIndent()
        return jdbcTemplate.queryForObject(sql) { rs, n ->  rs.getBoolean("cond") }!!
    }

    fun isAnySubTaskInProgress(cardId: Int): Boolean {
        val sql = """
            select count(1) > 0 as cond
            from card c
            join status s on s.id = c.status_id
            where c.id = $cardId
                and status_id = 2
        """.trimIndent()
        return jdbcTemplate.queryForObject(sql) { rs, n ->  rs.getBoolean("cond") }!!
    }

    fun cardIdBySubTask(taskId: Int): Int {
        val sql = "select card_id from sub_task where id = $taskId"
        return jdbcTemplate.queryForObject(sql) { rs, n ->  rs.getInt("card_id") }!!
    }
}