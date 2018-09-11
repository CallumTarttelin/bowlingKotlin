package com.saskcow.bowling.repository

import com.saskcow.bowling.domain.Score
import org.springframework.data.repository.CrudRepository

interface ScoreRepository : CrudRepository<Score, Long>