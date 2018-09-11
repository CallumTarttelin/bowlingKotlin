package com.saskcow.bowling.view

import com.saskcow.bowling.domain.League

class LeagueView(league: League, val teams: List<TeamViewSummaryLeague>) {
    val id: Long = league.id
    val name: String = league.name
    val games: List<GameViewSummary> = league.games.map(::GameViewSummary)
}
