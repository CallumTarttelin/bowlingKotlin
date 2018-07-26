package com.saskcow.bowling.view

import com.saskcow.bowling.domain.League

class LeagueViewSummary(league: League) {
    val id: Long = league.id
    val name: String = league.name
}
