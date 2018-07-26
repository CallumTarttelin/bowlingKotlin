package com.saskcow.bowling.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
data class Team (
    @Id @GeneratedValue
    val id: Long = -1,
    val name: String = "",
    @ManyToOne
    val league: League? = null,
    @OneToMany(mappedBy = "team", cascade = [(CascadeType.ALL)])
    val players: MutableList<Player> = mutableListOf()
) {
    init {
        league?.addTeam(this)
    }

    fun unlink() {
        this.league?.removeTeam(this)
    }

    fun addPlayer(player: Player) {
        players.add(player)
    }

    fun removePlayer(player: Player) {
        players.remove(player)
    }
}