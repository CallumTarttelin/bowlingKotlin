package com.saskcow.bowling.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class TeamGame(
    @Id @GeneratedValue
    val id: Long = -1,
    @ManyToOne
    val game: Game? = null,
    @ManyToOne
    val team: Team? = null,
    @OneToMany(cascade = [CascadeType.ALL])
    val players: MutableList<PlayerGame> = mutableListOf()
) {
    @OneToOne(mappedBy = "teamGame", cascade = [CascadeType.ALL])
    val teamPlayerGame = TeamPlayerGame(teamGame = this)

    fun init(): TeamGame {
        team?.addGame(this)
        return this
    }

    fun unlink() {
        this.team?.removeGame(this)
    }

    fun addPlayer(playerGame: PlayerGame) {
        this.players.add(playerGame)
    }

    fun removePlayer(playerGame: PlayerGame) {
        this.players.remove(playerGame)
    }
}
