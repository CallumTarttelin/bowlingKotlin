package com.saskcow.bowling.controller

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.saskcow.bowling.domain.Game
import com.saskcow.bowling.domain.League
import com.saskcow.bowling.domain.Team
import com.saskcow.bowling.repository.GameRepository
import com.saskcow.bowling.repository.LeagueRepository
import com.saskcow.bowling.repository.TeamRepository
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqual
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class GameControllerTest {

    private val baseUrl = "http://localhost"

    @Mock
    private val repo: GameRepository? = null
    @Mock
    private val leagueRepository: LeagueRepository? = null
    @Mock
    private val teamRepository: TeamRepository? = null
    private var mockMvc: MockMvc? = null

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(GameController(repo!!, leagueRepository!!, teamRepository!!)).build()
    }

    // Things to test with
    private val league = League(1L, "City Watch")
    private val teams = listOf(
        Team(2L, "The Night Watch", league).init(),
        Team(3L, "Cable Street Particulars", league).init()
    )
    private val time = LocalDateTime.now()
    private val game = Game(4L, time, "Pseudopolis Yard", league, teams).init()
    private val timeString = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    val testData = TestData()

    @Test
    fun `Adding a game with no league or teams returns 400`() {
        mockMvc!!.perform(
            post("/api/game")
                .content(
                    """{
                |    "garbage": "You don't have to test with the garbage. It's garbage.",
                |    "time": "$timeString",
                |    "venue": "Pseudopolis Yard"
                |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a game with invalid league returns 400`() {
        mockMvc!!.perform(
            post("/api/game")
                .content(
                    """{
                |    "teamIds": [2, 3],
                |    "time": "$timeString",
                |    "venue": "Pseudopolis Yard"
                |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a game with the same team twice returns 400`() {
        mockMvc!!.perform(
            post("/api/game")
                .content(
                    """{
                |    "leagueId": 1,
                |    "teamIds": [2, 2],
                |    "time": "$timeString",
                |    "venue": "Pseudopolis Yard"
                |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a game with an invalid team returns 400`() {
        mockMvc!!.perform(
            post("/api/game")
                .content(
                    """{
                |    "leagueId": 1,
                |    "teamIds": [2, 4],
                |    "time": "$timeString",
                |    "venue": "Pseudopolis Yard"
                |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a game without location or time returns 400`() {
        mockMvc!!.perform(
            post("/api/game")
                .content(
                    """{
                |    "leagueId": 1,
                |    "teamIds": [2, 3]
                |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a game with an invalid time format returns 400`() {
        mockMvc!!.perform(
            post("/api/game")
                .content(
                    """{
                |    "leagueId": 1,
                |    "teamIds": [2, 3],
                |    "time": "Good weather today",
                |    "venue": "Pseudopolis Yard"
                |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a valid game creates a game and returns location`() {
        teamRepository!!
        whenever(leagueRepository!!.findById(league.id)).thenReturn(Optional.of(league))
        doReturn(Optional.of(teams[0])).whenever(teamRepository).findById(teams[0].id)
        doReturn(Optional.of(teams[1])).whenever(teamRepository).findById(teams[1].id)
        whenever(repo!!.save(isA<Game>())).thenReturn(game)

        mockMvc!!.perform(
            post("/api/game")
                .content(
                    """{
                |    "leagueId": 1,
                |    "teamIds": [2, 3],
                |    "time": "$timeString",
                |    "venue": "Pseudopolis Yard"
                |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isCreated)
            .andExpect(MockMvcResultMatchers.header().string("Location", "$baseUrl/api/game/${game.id}"))
    }

    @Test
    fun `Getting a non existent game returns 404`() {
        mockMvc!!.perform(get("/api/game/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Can retrieve a game by ID`() {
        whenever(repo!!.findById(game.id)).thenReturn(Optional.of(game))
        mockMvc!!.perform(get("/api/game/4"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("venue", Matchers.equalTo("Pseudopolis Yard")))
    }

    @Test
    fun `Attempts to delete non existent games returns 404`() {
        mockMvc!!.perform(delete("/api/game/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Can delete a game from ID`() {
        whenever(repo!!.findById(game.id)).thenReturn(Optional.of(game))

        game.teamGames.map { it.id } shouldContainAll teams.map { it.games[0].id }

        mockMvc!!.perform(delete("/api/game/4"))
            .andExpect(status().isNoContent)

        teams.map { it.games }.all { it.size == 0 }.shouldBeTrue()

        verify(repo, times(1)).deleteById(4L)
    }

    @Test
    fun `Attempting to complete a missing game returns 404`() {
        mockMvc!!.perform(post("/api/game/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Attempting to complete an incomplete game returns 400`() {
        whenever(repo!!.findById(testData.game.id)).thenReturn(Optional.of(testData.game))
        mockMvc!!.perform(post("/api/game/${testData.game.id}"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Completing game adds totals and scores`() {
        val completeGame = testData.completeGame
        whenever(repo!!.findById(completeGame.id)).thenReturn(Optional.of(completeGame))
        mockMvc!!.perform(post("/api/game/${completeGame.id}"))
            .andExpect(status().isNoContent)

        // Check total scores exist
        completeGame.teamGames.forEach { it.teamPlayerGame.scores.size shouldEqual 4 }
        completeGame.teamGames.forEach { teamGame -> teamGame.players.forEach { it.scores.size shouldEqual 4 } }

        // Check additions
        completeGame.teamGames[0].teamPlayerGame.scores[0].handicapped shouldEqual 689
        completeGame.teamGames[0].teamPlayerGame.scores[1].scratch shouldEqual 273
        completeGame.teamGames[0].teamPlayerGame.scores[3].handicapped shouldEqual 1833
        completeGame.teamGames[0].teamPlayerGame.scores[3].total shouldEqual true
        completeGame.teamGames[0].teamPlayerGame.scores[2].total shouldEqual false

        completeGame.teamGames[1].teamPlayerGame.scores[3].scratch shouldEqual 1452
        completeGame.teamGames[1].players[0].scores[3].handicapped shouldEqual 920
        completeGame.teamGames[1].players[1].scores[3].scratch shouldEqual 384
        completeGame.teamGames[1].players[2].scores[3].total shouldEqual true
    }
}