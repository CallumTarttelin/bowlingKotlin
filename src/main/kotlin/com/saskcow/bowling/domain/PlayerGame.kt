package com.saskcow.bowling.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
data class PlayerGame(
    @Id @GeneratedValue
    val id: Long = -1,
    @ManyToOne
    val game: TeamGame? = null,
    @ManyToOne
    val player: Player? = null,
    @OneToMany(mappedBy = "playerGame", cascade = [CascadeType.ALL])
    val scores: MutableList<Score> = mutableListOf(),
    val handicap: Int = 0
) {
    fun init(): PlayerGame {
        this.player?.addGame(this)
        this.game?.addPlayer(this)
        return this
    }

    fun unlink() {
        this.game?.removePlayer(this)
        this.player?.removeGame(this)
    }

    fun addScore(score: Score) {
        this.scores.add(score)
    }
}