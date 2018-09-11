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
    val totalPoints = teamPoints + playerPoints
}