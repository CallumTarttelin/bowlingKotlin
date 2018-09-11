package com.saskcow.bowling.view

import com.saskcow.bowling.domain.Game
import com.saskcow.bowling.domain.PlayerGame

class PlayerGameViewSummaryPlayer(playerGame: PlayerGame) {
    val id: Long = playerGame.id
    val game: GameViewSummary = GameViewSummary(playerGame.game?.game ?: Game(-42, venue = "NO GAME"))
    val scores: List<ScoreViewSummary> = playerGame.scores.map(::ScoreViewSummary)
}