package com.saskcow.bowling.repository

import com.saskcow.bowling.domain.TeamGame
import org.springframework.data.repository.CrudRepository

interface TeamGameRepository : CrudRepository<TeamGame, Long>