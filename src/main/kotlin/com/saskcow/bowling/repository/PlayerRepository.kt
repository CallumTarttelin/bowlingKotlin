package com.saskcow.bowling.repository

import com.saskcow.bowling.domain.Player
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface PlayerRepository : CrudRepository<Player, Long> {
    @Query("SELECT COALESCE(MAX(s.scratch), 0) FROM Player p JOIN p.games g JOIN g.scores s WHERE p.id = :id AND s.total = FALSE")
    fun highGame(id: Long): Int

    @Query("SELECT COALESCE(MIN(s.scratch), 0) FROM Player p JOIN p.games g JOIN g.scores s WHERE p.id = :id AND s.total = FALSE")
    fun lowGame(id: Long): Int

    @Query("SELECT COALESCE(MAX(s.scratch), 0) FROM Player p JOIN p.games g JOIN g.scores s WHERE p.id = :id AND s.total = TRUE")
    fun highSeries(id: Long): Int

    @Query("SELECT COALESCE(MIN(s.scratch), 0) FROM Player p JOIN p.games g JOIN g.scores s WHERE p.id = :id AND s.total = TRUE")
    fun lowSeries(id: Long): Int
}
