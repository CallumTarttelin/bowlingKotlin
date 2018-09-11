package com.saskcow.bowling.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
data class Player (
    @Id @GeneratedValue
    val id: Long = -1,
    val name: String = "",
    @ManyToOne
    val team: Team? = null,
    @OneToMany(mappedBy = "player", cascade = [CascadeType.ALL])
    val games: MutableList<PlayerGame> = mutableListOf()
) {
    fun init(): Player {
        team?.addPlayer(this)
        return this
    }

    fun unlink() {
        this.team?.removePlayer(this)
    }

    fun getHandicap(): Int {
        if (this.games.isEmpty()) return 0
        val last24 = this.games.filter { it.scores.size >= 3 }.subList(0, 24)
        if (last24.isEmpty()) return 0
        val adgustedAverage = (0.8 * last24.fold(0) { a, b ->
            a + (b.scores.subList(0, 3).sumBy { it.scratch } / 3)
        })
        return when {
            adgustedAverage < 0 -> 0
            adgustedAverage > 80 -> 80
            else -> adgustedAverage.toInt()
        }
    }

    fun addGame(game: PlayerGame) {
        this.games.add(game)
    }

    fun removeGame(game: PlayerGame) {
        this.games.remove(game)
    }
}