package com.saskcow.bowling.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class League(
    @Id @GeneratedValue
    val id: Long = -1,
    val name: String = "",
    @OneToMany(mappedBy = "league", cascade = [CascadeType.ALL])
    val teams: MutableList<Team> = mutableListOf(),
    @OneToMany(mappedBy = "league", cascade = [CascadeType.ALL])
    val games: MutableList<Game> = mutableListOf()
) {

    fun addTeam(team: Team) {
        teams.add(team)
    }

    fun removeTeam(team: Team) {
        teams.remove(team)
    }

    fun addGame(game: Game) {
        games.add(game)
    }

    fun removeGame(game: Game) {
        games.remove(game)
    }
}