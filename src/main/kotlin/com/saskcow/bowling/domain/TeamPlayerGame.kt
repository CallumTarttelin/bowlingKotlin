package com.saskcow.bowling.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class TeamPlayerGame(
    @Id @GeneratedValue
    val id: Long = -1,
    @OneToOne
    val teamGame: TeamGame? = null,
    @OneToMany(mappedBy = "teamPlayerGame", cascade = [CascadeType.ALL])
    val scores: MutableList<TeamPlayerGameScore> = mutableListOf()
) {
    fun addScore(score: TeamPlayerGameScore) {
        this.scores.add(score)
    }
}