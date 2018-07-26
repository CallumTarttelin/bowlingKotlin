package com.saskcow.bowling.repository

import com.saskcow.bowling.domain.League
import org.springframework.data.repository.CrudRepository

interface LeagueRepository : CrudRepository<League, Long> {

    fun findByNameContainingIgnoreCase(name: String): List<League>
}