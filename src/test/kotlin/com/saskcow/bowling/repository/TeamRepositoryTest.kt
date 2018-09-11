package com.saskcow.bowling.repository

import com.saskcow.bowling.domain.Team
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringExtension

// Works really weird, no `functions with spaces`, save and delete requires init and unlink
@ExtendWith(SpringExtension::class)
@DataJpaTest
class TeamRepositoryTest {

    @Autowired
    private val repo: TeamRepository? = null

    @Test
    @Sql("/test-schema.sql")
    fun highHandicapGame() {
        repo!!.highHandicapGame(2) shouldEqual 715
        repo.highHandicapGame(3) shouldEqual 805
    }

    @Test
    @Sql("/test-schema.sql")
    fun highHandicapSeries() {
        repo!!.highHandicapSeries(2) shouldEqual 2051
        repo.highHandicapSeries(3) shouldEqual 1901
    }

    @Test
    @Sql("/test-schema.sql")
    fun pinsFor() {
        repo!!.pinsFor(2) shouldEqual 3848
        repo.pinsFor(3) shouldEqual 3769
    }

    @Test
    @Sql("/test-schema.sql")
    fun pinsAgainst() {
        repo!!.pinsAgainst(2) shouldEqual 3769
        repo.pinsAgainst(3) shouldEqual 3848
    }

    //
    @Test
    @Sql("/test-schema.sql")
    fun teamPoints() {
        repo!!.teamPoints(2) shouldEqual 6
        repo.teamPoints(3) shouldEqual 10
    }

    @Test
    @Sql("/test-schema.sql")
    fun playerPoints() {
        repo!!.playerPoints(2) shouldEqual 24
        repo.playerPoints(3) shouldEqual 24
    }

    @Test
    fun nullsOk() {
        val team = repo!!.save(Team())
        repo.pinsFor(team.id) shouldEqual 0
        repo.pinsAgainst(team.id) shouldEqual 0
        repo.highHandicapGame(team.id) shouldEqual 0
        repo.highHandicapSeries(team.id) shouldEqual 0
        repo.teamPoints(team.id) shouldEqual 0
        repo.playerPoints(team.id) shouldEqual 0
    }
}
