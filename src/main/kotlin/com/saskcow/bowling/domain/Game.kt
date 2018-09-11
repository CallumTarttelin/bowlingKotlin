package com.saskcow.bowling.domain

import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class Game(
    @Id @GeneratedValue
    val id: Long = -1,
    var time: LocalDateTime = LocalDateTime.now(),
    var venue: String = "",
    @ManyToOne
    val league: League? = null,
    teams: List<Team> = listOf()

) {
    @OneToMany(mappedBy = "game", cascade = [CascadeType.ALL])
    var teamGames: List<TeamGame> = teams.map { team -> TeamGame(game = this, team = team) }

    fun init(): Game {
        league?.addGame(this)
        teamGames.forEach { it.init() }
        return this
    }

    fun unlink() {
        this.league?.removeGame(this)
        this.teamGames.forEach { it.unlink() }
    }

    fun removeGame(teamGame: TeamGame) {
        this.teamGames = this.teamGames.filterNot { it == teamGame }
    }

    fun completeGame() {
        verification()
        addPlayerTotals()
        addTeamTotals()
        scorePlayers()
        scoreTeams()
    }

    private fun addPlayerTotals() {
        teamGames.forEach {
            it.players.forEach { playerGame ->
                val total = playerGame.scores.reduce { acc, score ->
                    Score(
                        playerGame = playerGame,
                        scratch = acc.scratch + score.scratch,
                        handicapped = acc.handicapped + score.handicapped,
                        total = true
                    )
                }
                playerGame.addScore(total)
            }
        }
    }

    private fun addTeamTotals() {
        teamGames.forEach {
            for (i in 0..3) {
                val total = it.players.fold(Pair(0, 0)) { sum, player ->
                    Pair(sum.first + player.scores[i].scratch, sum.second + player.scores[i].handicapped)
                }
                it.teamPlayerGame.addScore(
                    TeamPlayerGameScore(
                        teamPlayerGame = it.teamPlayerGame,
                        scratch = total.first,
                        handicapped = total.second,
                        total = i == 3
                    )
                )
            }
        }
    }

    private fun scorePlayers() {
        val scoresZipped = mutableListOf<Pair<Score, Score>>()
        for (i in 0..2) {
            for (j in 0..3) {
                scoresZipped.add(
                    Pair(
                        teamGames[0].players[i].scores[j],
                        teamGames[1].players[i].scores[j]
                    )
                )
            }
        }

        scoresZipped.forEach {
            when {
                it.first.handicapped > it.second.handicapped -> it.first.score = 2
                it.second.handicapped > it.first.handicapped -> it.second.score = 2
                else -> {
                    it.first.score = 1
                    it.second.score = 1
                }
            }
        }
    }

    private fun scoreTeams() {
        val scoresZipped = mutableListOf<Pair<TeamPlayerGameScore, TeamPlayerGameScore>>()
        for (i in 0..3) {
            scoresZipped.add(
                Pair(
                    teamGames[0].teamPlayerGame.scores[i],
                    teamGames[1].teamPlayerGame.scores[i]
                )
            )
        }

        scoresZipped.forEach {
            when {
                it.first.handicapped > it.second.handicapped -> it.first.score = 2
                it.second.handicapped > it.first.handicapped -> it.second.score = 2
                else -> {
                    it.first.score = 1
                    it.second.score = 1
                }
            }
        }
    }

    private fun verification() {
        if (teamGames.size != 2) throw Exception("Wrong number of teams in game Expected 2, got ${teamGames.size}")
        teamGames.forEach {
            if (it.players.size != 3) throw Exception("Not enough players in team ${it.team?.name} Expected 3, got ${it.players.size}")
            it.players.forEach { playerGame ->
                if (playerGame.scores.size != 3) throw Exception("Not enough scores added to player ${playerGame.player?.name} Expected 3, got ${playerGame.scores.size}")
            }
        }
    }
}