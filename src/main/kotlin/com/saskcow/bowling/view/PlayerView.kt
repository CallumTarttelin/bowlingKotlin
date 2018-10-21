package com.saskcow.bowling.view

import com.saskcow.bowling.domain.Player
import com.saskcow.bowling.domain.Team

class PlayerView(
    player: Player,
    val highGame: Int,
    val lowGame: Int,
    val highSeries: Int,
    val lowSeries: Int
) {
    val id: Long = player.id
    val name: String = player.name
    val team: TeamViewSummary = TeamViewSummary(player.team ?: Team(-42L, "NO TEAM"))
    val games: List<PlayerGameViewSummaryPlayer> = player.games.map(::PlayerGameViewSummaryPlayer)
    val handicap = player.handicap
}
