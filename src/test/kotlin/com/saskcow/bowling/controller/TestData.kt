package com.saskcow.bowling.controller

import com.saskcow.bowling.domain.Game
import com.saskcow.bowling.domain.League
import com.saskcow.bowling.domain.Player
import com.saskcow.bowling.domain.PlayerGame
import com.saskcow.bowling.domain.Score
import com.saskcow.bowling.domain.Team
import java.time.LocalDateTime

class TestData {
    val league = League(1L, "City Watch")
    val teams = listOf(
        Team(2L, "The Night Watch", league).init(),
        Team(3L, "Cable Street Particulars", league).init(),
        Team(17L, "The Day Watch", league).init()
    )

    val players = listOf(
        Player(5L, "Sam Vimes", teams[0]).init(),
        Player(6L, "Carrot Ironfoundersson", teams[0]).init(),
        Player(7L, "Nobby Nobbs", teams[0]).init(),

        Player(8L, "Findthee Swing", teams[1]).init(),
        Player(9L, "Carcer", teams[1]).init(),
        Player(10L, "Henry 'The Hamster' Higgins", teams[1]).init()
    )

    val time = LocalDateTime.now()

    val completeGame = Game(24L, time.plusWeeks(1), "Treacle Mine Road ", league, teams.subList(0, 2)).init()

    val game = Game(4L, time, "Pseudopolis Yard", league, teams.subList(0, 2)).init()

    val playerGames = listOf(
        PlayerGame(11L, game.teamGames[0], players[0]).init(),
        PlayerGame(12L, game.teamGames[0], players[1]).init(),
        PlayerGame(13L, game.teamGames[0], players[2]).init(),
        PlayerGame(14L, game.teamGames[1], players[3]).init(),
        PlayerGame(15L, game.teamGames[1], players[4]).init(),
        PlayerGame(16L, game.teamGames[1], players[5]).init()
    )

    val scores = listOf(
        Score(18L, playerGames[3], 200, 220).init(),
        Score(19L, playerGames[3]).init(),
        Score(20L, playerGames[3]).init(),
        Score(21L, playerGames[4]).init(),
        Score(22L, playerGames[4]).init(),
        Score(23L, playerGames[4]).init()
    )

    val completePlayerGames = listOf(
        PlayerGame(25L, completeGame.teamGames[0], players[0]).init(),
        PlayerGame(26L, completeGame.teamGames[0], players[1]).init(),
        PlayerGame(27L, completeGame.teamGames[0], players[2]).init(),
        PlayerGame(28L, completeGame.teamGames[1], players[3]).init(),
        PlayerGame(29L, completeGame.teamGames[1], players[4]).init(),
        PlayerGame(30L, completeGame.teamGames[1], players[5]).init()
    )

    val scratchHandicaps = listOf(
        Pair(143, 155),
        Pair(35, 47),
        Pair(187, 199),

        Pair(124, 193),
        Pair(173, 242),
        Pair(239, 308),

        Pair(291, 341),
        Pair(65, 115),
        Pair(183, 233),

        Pair(219, 295),
        Pair(189, 265),
        Pair(284, 360),

        Pair(244, 278),
        Pair(100, 134),
        Pair(40, 74),

        Pair(88, 120),
        Pair(111, 143),
        Pair(177, 209)
    )

    val completeScores = completePlayerGames.map {
        val base = (it.id - 25) * 3
        listOf(
            Score(
                base + 30,
                it,
                scratchHandicaps[(base + 0).toInt()].first,
                scratchHandicaps[(base + 0).toInt()].second
            ).init(),
            Score(
                base + 31,
                it,
                scratchHandicaps[(base + 1).toInt()].first,
                scratchHandicaps[(base + 1).toInt()].second
            ).init(),
            Score(
                base + 32,
                it,
                scratchHandicaps[(base + 2).toInt()].first,
                scratchHandicaps[(base + 2).toInt()].second
            ).init()
        )
    }.flatten()
}