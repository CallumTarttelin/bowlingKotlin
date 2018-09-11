package com.saskcow.bowling.view

import com.saskcow.bowling.domain.TeamPlayerGame

class TeamPlayerGameViewSummary(teamPlayerGame: TeamPlayerGame) {
    val id = teamPlayerGame.id
    val scores = teamPlayerGame.scores.map(::ScoreViewSummary)
}
