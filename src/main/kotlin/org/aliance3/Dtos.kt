package org.aliance3

data class PageState(
    val levels: List<Level> = listOf()
)


data class Level(
    val isActive: Boolean = false,
    val cards: List<Card> = listOf(),
    val progress: Int = 0
)

data class SubTask (
    val header: String = "",
    val main_body: String = "",
    val idDone: Boolean = false
)

data class Card(
    val title: String = "",
    val difficulty: Int = 0,
    val status: Status = Status.NEW,
    val type: CardType = CardType(),
    val subTasks: List<SubTask> = listOf()
)

data class CardType(
    val block: String = "",
    val color: String = "",
    val icon: String = ""
)

enum class Status {
    NEW,
    IN_PROGRESS,
    DONE;

    companion object {
        fun from(str: String?): Status {
            return when (str) {
                "NEW" -> NEW
                "IN_PROGRESS" -> IN_PROGRESS
                "DONE" -> DONE
                else -> {
                    throw IllegalArgumentException()
                }
            }
        }
    }
}