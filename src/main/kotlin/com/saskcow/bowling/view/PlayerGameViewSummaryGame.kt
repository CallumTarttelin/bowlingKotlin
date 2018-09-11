package com.saskcow.bowling.view

import com.saskcow.bowling.domain.Player
import com.saskcow.bowling.domain.PlayerGame

class PlayerGameViewSummaryGame(playerGame: PlayerGame) {
    val id: Long = playerGame.id
    val player: PlayerViewSummary = PlayerViewSummary(playerGame.player ?: Player(-42, "NO PLAYER"))
    val scores: List<ScoreViewSummary> = playerGame.scores.map(::ScoreViewSummary)
}