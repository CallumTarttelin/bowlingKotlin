package com.saskcow.bowling.rest

data class PlayerGameRest(
    val playerIds: List<Long> = listOf(),
    val teamId: Long? = null,
    val gameId: Long? = null
)