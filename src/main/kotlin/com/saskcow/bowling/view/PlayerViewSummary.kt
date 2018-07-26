package com.saskcow.bowling.view

import com.saskcow.bowling.domain.Player

class PlayerViewSummary(player: Player) {
    val id: Long = player.id
    val name: String = player.name
}