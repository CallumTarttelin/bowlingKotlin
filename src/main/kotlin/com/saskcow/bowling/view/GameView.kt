package com.saskcow.bowling.view

import com.saskcow.bowling.domain.Game
import com.saskcow.bowling.domain.League
import java.time.format.DateTimeFormatter

class GameView(game: Game) {
    val id: Long = game.id
    val venue: String = game.venue
    val time: String = game.time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val league: LeagueViewSummary = LeagueViewSummary(game.league ?: League(-42L, "NO LEAGUE"))
    val teams: List<TeamGameViewSummary> = game.teamGames.map(::TeamGameViewSummary)
}