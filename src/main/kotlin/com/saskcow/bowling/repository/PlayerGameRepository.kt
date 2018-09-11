package com.saskcow.bowling.repository

import com.saskcow.bowling.domain.PlayerGame
import org.springframework.data.repository.CrudRepository

interface PlayerGameRepository : CrudRepository<PlayerGame, Long>
