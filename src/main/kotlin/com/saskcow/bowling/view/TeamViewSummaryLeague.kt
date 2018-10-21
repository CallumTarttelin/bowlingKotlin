package com.saskcow.bowling.view

import com.saskcow.bowling.domain.Team

class TeamViewSummaryLeague(
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
    val numGames = team.games.filter { it.teamPlayerGame.scores.size == 4 }.size
    val totalPoints = teamPoints + playerPoints
}