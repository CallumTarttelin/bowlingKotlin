package com.saskcow.bowling.rest

data class ScoreRest(
    val playerGameId: Long? = null,
    val scratch: Int? = null,
    val handicap: Int? = null,
    val handicapped: Int? = null
)