package com.saskcow.bowling.repository

import com.saskcow.bowling.BowlingApplication
import com.saskcow.bowling.domain.League
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [(BowlingApplication::class)])
@ActiveProfiles("test")
class LeagueRepositoryTest {

    @Autowired
    private val repo: LeagueRepository? = null

    @Before
    fun clear() {
        repo!!.deleteAll()
    }

    @Test
    fun thingsSaved_canBeRetrieved() {
        var bestLeague = League(name = "brian")
        bestLeague = repo!!.save(bestLeague)
        val foundGame = repo.findById(bestLeague.id)
        assertThat(foundGame.get().name).isEqualTo(bestLeague.name)
    }

    @Test
    fun thingsSaved_canBeQueried() {
        var bestGame = League(name = "Brian")
        val worstGame = League(name = "Dave")
        bestGame = repo!!.save(bestGame)
        repo.save(worstGame)
        var foundGame = repo.findByNameContainingIgnoreCase("Brian")
        assertThat(foundGame.size).isOne()
        assertThat(foundGame[0].name).isEqualTo(bestGame.name)
        foundGame = repo.findByNameContainingIgnoreCase("brI")
        assertThat(foundGame.size).isOne()
        assertThat(foundGame[0].name).isEqualTo(bestGame.name)
        foundGame = repo.findByNameContainingIgnoreCase("dev")
        assertThat(foundGame.size).isZero()
    }

    @Test
    fun thingsSaved_canBeDeleted() {
        var bestLeague = League(name = "brian")
        bestLeague = repo!!.save(bestLeague)
        repo.deleteById(bestLeague.id)
        val foundGame = repo.findById(bestLeague.id)
        assertThat(foundGame).isNotPresent
    }
}