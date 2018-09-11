package com.saskcow.bowling.view

import com.saskcow.bowling.domain.TeamGame

class TeamGameViewSummary(teamGame: TeamGame) {
    val id: Long = teamGame.team?.id ?: -1
    val name: String = teamGame.team?.name ?: "NO TEAM"
    val players: List<PlayerGameViewSummaryGame> = teamGame.players.map(::PlayerGameViewSummaryGame)
    val teamPlayer: TeamPlayerGameViewSummary? =
        if (teamGame.teamPlayerGame.scores.isNotEmpty()) TeamPlayerGameViewSummary(teamGame.teamPlayerGame) else null
}
