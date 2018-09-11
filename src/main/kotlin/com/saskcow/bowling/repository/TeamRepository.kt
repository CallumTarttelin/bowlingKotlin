package com.saskcow.bowling.repository

import com.saskcow.bowling.domain.Team
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface TeamRepository : CrudRepository<Team, Long> {
    @Query("SELECT COALESCE(MAX(s.scratch), 0) FROM Team t JOIN t.games g JOIN g.teamPlayerGame p JOIN p.scores s WHERE t.id = :id AND s.total = FALSE")
    fun highHandicapGame(id: Long): Int

    @Query("SELECT COALESCE(MAX(s.scratch), 0) FROM Team t JOIN t.games g JOIN g.teamPlayerGame p JOIN p.scores s WHERE t.id = :id AND s.total = TRUE")
    fun highHandicapSeries(id: Long): Int

    @Query("SELECT COALESCE(SUM(s.scratch), 0) FROM Team t JOIN t.games g JOIN g.teamPlayerGame p JOIN p.scores s WHERE t.id = :id AND s.total = TRUE")
    fun pinsFor(id: Long): Int

    @Query("SELECT COALESCE(SUM(s.scratch), 0) / 2 FROM Team t JOIN t.games tg JOIN tg.game g JOIN g.teamGames tgs JOIN tgs.teamPlayerGame pg JOIN pg.scores s WHERE tgs.team.id <> :id AND s.total = TRUE")
    fun pinsAgainst(id: Long): Int

    @Query("SELECT COALESCE(SUM(s.score), 0) FROM Team t JOIN t.games g JOIN g.teamPlayerGame p JOIN p.scores s WHERE t.id = :id")
    fun teamPoints(id: Long): Int

    @Query("SELECT COALESCE(SUM(s.score), 0) FROM Team t JOIN t.games g JOIN g.players p JOIN p.scores s WHERE t.id = :id")
    fun playerPoints(id: Long): Int
}
