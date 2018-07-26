package com.saskcow.bowling.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Player (
    @Id @GeneratedValue
    val id: Long = -1,
    val name: String = "",
    @ManyToOne
    val team: Team? = null
) {
    init {
        team?.addPlayer(this)
    }

    fun unlink() {
        this.team?.removePlayer(this)
    }
}