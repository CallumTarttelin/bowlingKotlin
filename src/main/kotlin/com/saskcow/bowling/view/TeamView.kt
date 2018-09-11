package com.saskcow.bowling.view

import com.saskcow.bowling.domain.Game
import com.saskcow.bowling.domain.League
import com.saskcow.bowling.domain.Team

class TeamView(
    team: Team,
    val pinsFor: Int,
    val pinsAgainst: Int,
    val highHandicapGame: Int,
    val highHandicapSeries: Int,
    val teamPoints: Int,
    playerPoints: Int
) {
    val id: Long = team.id
    val name: String = team.name
    val league: LeagueViewSummary = LeagueViewSummary(team.league ?: League(-42L, "NO LEAGUE"))
    val players: List<PlayerViewSummary> = team.players.map(::PlayerViewSummary)
    val games: List<GameViewSummary> =
        team.games.map { GameViewSummary(it.game ?: Game(-42, venue = "TEAM GAME HAS NO GAME")) }
    val numGames = team.games.filter { it.teamPlayerGame.scores.size == 4 }.size
    val totalPoints = playerPoints + teamPoints
}
