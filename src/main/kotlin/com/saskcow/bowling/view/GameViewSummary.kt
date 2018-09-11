package com.saskcow.bowling.view

import com.saskcow.bowling.domain.Game
import com.saskcow.bowling.domain.Team
import java.time.format.DateTimeFormatter

class GameViewSummary(game: Game) {
    val id: Long = game.id
    val venue: String = game.venue
    val time: String = game.time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val teams: List<TeamViewSummary> = game.teamGames.map { TeamViewSummary(it.team ?: Team(-42L, "NO TEAM")) }
}