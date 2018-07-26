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
    @OneToMany(mappedBy = "league", cascade = [(CascadeType.ALL)])
    val teams: MutableList<Team> = mutableListOf()
) {

    fun addTeam(team: Team) {
        teams.add(team)
    }

    fun removeTeam(team: Team) {
        teams.remove(team)
    }
}