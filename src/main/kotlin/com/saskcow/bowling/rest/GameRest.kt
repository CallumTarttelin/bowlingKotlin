package com.saskcow.bowling.rest

data class GameRest(
    val leagueId: Long? = null,
    val teamIds: List<Long> = listOf(),
    val time: String? = null,
    val venue: String? = null
)