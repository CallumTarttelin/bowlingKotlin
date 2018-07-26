package com.saskcow.bowling.view

import com.saskcow.bowling.domain.League
import com.saskcow.bowling.domain.Team

class TeamView(team: Team ) {
    val id: Long = team.id
    val name: String = team.name
    val league: LeagueViewSummary = LeagueViewSummary(team.league ?: League(-42L, "NO LEAGUE"))
    val players: List<PlayerViewSummary> = team.players.map(::PlayerViewSummary)
}
