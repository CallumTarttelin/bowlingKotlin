package com.saskcow.bowling.repository

import com.saskcow.bowling.domain.Player
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
class PlayerRepositoryTest {

    @Autowired
    private val repo: PlayerRepository? = null

    @Test
    @Sql("/test-schema.sql")
    fun highGame() {
        repo!!.highGame(4) shouldEqual 281
        repo.highGame(9) shouldEqual 287
        repo.highGame(5) shouldEqual 262
    }

    @Test
    @Sql("/test-schema.sql")
    fun lowGame() {
        repo!!.lowGame(4) shouldEqual 100
        repo.lowGame(8) shouldEqual 166
        repo.lowGame(6) shouldEqual 181
    }

    @Test
    @Sql("/test-schema.sql")
    fun highSeries() {
        repo!!.highSeries(5) shouldEqual 682
        repo.highSeries(9) shouldEqual 734
        repo.highSeries(10) shouldEqual 603
    }

    @Test
    @Sql("/test-schema.sql")
    fun lowSeries() {
        repo!!.lowSeries(5) shouldEqual 682
        repo.lowSeries(6) shouldEqual 631
        repo.lowSeries(9) shouldEqual 582
        repo.lowSeries(10) shouldEqual 551
    }

    @Test
    fun nullsOk() {
        val plr = repo!!.save(Player())
        repo.highGame(plr.id) shouldEqual 0
        repo.lowGame(plr.id) shouldEqual 0
        repo.highSeries(plr.id) shouldEqual 0
        repo.lowSeries(plr.id) shouldEqual 0
    }
}