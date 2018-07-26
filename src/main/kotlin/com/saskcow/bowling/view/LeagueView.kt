package com.saskcow.bowling.view

import com.saskcow.bowling.domain.League

class LeagueView(league: League) {
    val id: Long = league.id
    val name: String = league.name
    val teams: List<TeamViewSummary> = league.teams.map(::TeamViewSummary)
}
