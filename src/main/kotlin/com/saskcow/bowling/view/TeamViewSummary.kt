package com.saskcow.bowling.view

import com.saskcow.bowling.domain.Team

class TeamViewSummary(team: Team) {
    val id: Long = team.id
    val name: String = team.name
}
