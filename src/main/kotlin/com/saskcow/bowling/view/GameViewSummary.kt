package com.saskcow.bowling.view

import com.saskcow.bowling.domain.Game
import com.saskcow.bowling.domain.Team
import com.saskcow.bowling.domain.TeamGame
import java.time.format.DateTimeFormatter

class GameViewSummary(game: Game) {
    val id: Long = game.id
    val venue: String = game.venue
    val time: String = game.time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val teams: List<TeamViewSummary> = game.teamGames.map { TeamViewSummary(it.team ?: Team(-42L, "NO TEAM")) }
    val winner: Int = when {
        game.teamGames.filterNot { it.teamPlayerGame.scores.size == 4 }.isNotEmpty() -> -1
        countScore(game.teamGames[0]) > countScore(game.teamGames[1]) -> 0
        countScore(game.teamGames[1]) > countScore(game.teamGames[0]) -> 1
        else -> 2
    }

    private fun countScore(teamGame: TeamGame): Int {
        val playerScores: Int = teamGame.players
            .fold(0) { acc, plr -> acc + plr.scores
                .fold(0) { scoreAcc, score -> scoreAcc + score.score }
            }
        val teamPlayerScore = teamGame.teamPlayerGame.scores.fold(0) { acc, score -> acc + score.score }
        return playerScores + teamPlayerScore
    }
}