package com.saskcow.bowling.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Score(
    @GeneratedValue @Id
    val id: Long = -1,
    @ManyToOne
    val playerGame: PlayerGame? = null,
    val scratch: Int = 0,
    val handicapped: Int = 0,
    var score: Int = 0,
    val total: Boolean = false
) {
    fun init(): Score {
        this.playerGame?.addScore(this)
        return this
    }

    fun unlink() {
        this.playerGame?.scores?.remove(this)
    }
}
