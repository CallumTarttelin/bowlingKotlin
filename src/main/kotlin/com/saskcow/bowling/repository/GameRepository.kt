package com.saskcow.bowling.repository

import com.saskcow.bowling.domain.Game
import org.springframework.data.repository.CrudRepository

interface GameRepository : CrudRepository<Game, Long>