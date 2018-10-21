package com.saskcow.bowling.controller

import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.saskcow.bowling.domain.Score
import com.saskcow.bowling.repository.PlayerGameRepository
import com.saskcow.bowling.repository.ScoreRepository
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotContain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ScoreControllerTest {

    private val baseUrl = "http://localhost"

    @Mock
    private val repo: ScoreRepository? = null
    @Mock
    private val playerGameRepository: PlayerGameRepository? = null
    private var mockMvc: MockMvc? = null
    private val testData = TestData()

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ScoreController(repo!!, playerGameRepository!!)).build()
    }

    @Test
    fun `Adding a score to no player returns 400`() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/score")
                .content(
                    """{
                    |    "scratch": 200,
                    |    "handicap": 20
                    |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a score with no scratch returns 400`() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/score")
                .content(
                    """{
                    |    "playerGameId": ${testData.playerGames[0].id},
                    |    "handicapped": 230
                    |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a score with differing handicap + scratch and handicapped returns 400`() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/score")
                .content(
                    """{
                    |    "playerGameId": ${testData.playerGames[0].id},
                    |    "scratch": 200,
                    |    "handicap": 20,
                    |    "handicapped": 230
                    |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a score with too high a score returns 400`() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/score")
                .content(
                    """{
                    |    "playerGameId": ${testData.playerGames[0].id},
                    |    "scratch": 400,
                    |    "handicap": 20,
                    |    "handicapped": 420
                    |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a score with too high a handicap returns 400`() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/score")
                .content(
                    """{
                    |    "playerGameId": ${testData.playerGames[0].id},
                    |    "scratch": 200,
                    |    "handicap": 220,
                    |    "handicapped": 420
                    |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a valid score with both returns location of game`() {
        whenever(playerGameRepository!!.findById(testData.playerGames[0].id)).thenReturn(Optional.of(testData.playerGames[0]))
        whenever(repo!!.save(isA<Score>())).thenReturn(testData.scores[0])

        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/score")
                .content(
                    """{
                    |    "playerGameId": ${testData.playerGames[0].id},
                    |    "scratch": 200,
                    |    "handicap": 20,
                    |    "handicapped": 220
                    |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("location", "$baseUrl/api/game/${testData.playerGames[0].game?.game?.id}"))
    }

    @Test
    fun `Adding a valid score with handicap returns location of game`() {
        whenever(playerGameRepository!!.findById(testData.playerGames[0].id)).thenReturn(Optional.of(testData.playerGames[0]))
        whenever(repo!!.save(isA<Score>())).thenReturn(testData.scores[0])

        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/score")
                .content(
                    """{
                    |    "playerGameId": ${testData.playerGames[0].id},
                    |    "scratch": 200,
                    |    "handicap": 20
                    |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("location", "$baseUrl/api/game/${testData.playerGames[0].game?.game?.id}"))
    }

    @Test
    fun `Adding a valid score with handicapped returns location of game`() {
        whenever(playerGameRepository!!.findById(testData.playerGames[0].id)).thenReturn(Optional.of(testData.playerGames[0]))
        whenever(repo!!.save(isA<Score>())).thenReturn(testData.scores[0])

        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/score")
                .content(
                    """{
                    |    "playerGameId": ${testData.playerGames[0].id},
                    |    "scratch": 200,
                    |    "handicapped": 220
                    |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("location", "$baseUrl/api/game/${testData.playerGames[0].game?.game?.id}"))
    }

    @Test
    fun `Can add a score with no handicap and it will use player handicap`() {
        whenever(playerGameRepository!!.findById(testData.playerGames[0].id)).thenReturn(Optional.of(testData.playerGames[0]))
        whenever(repo!!.save(isA<Score>())).thenReturn(testData.scores[0])

        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/score")
                .content(
                    """{
                    |    "playerGameId": ${testData.playerGames[0].id},
                    |    "scratch": 200
                    |}""".trimMargin()
                )
                .contentType("application/json")
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("location", "$baseUrl/api/game/${testData.playerGames[0].game?.game?.id}"))
    }

    @Test
    fun `Attempting to delete a nonexistent score returns 404`() {
        mockMvc!!.perform(MockMvcRequestBuilders.delete("/api/score/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Can delete scores by id`() {
        testData.playerGames[4].scores.map { it.id } shouldContain testData.scores[5].id

        whenever(repo!!.findById(testData.scores[5].id)).thenReturn(Optional.of(testData.scores[5]))
        doNothing().whenever(repo).deleteById(testData.scores[5].id)

        mockMvc!!.perform(MockMvcRequestBuilders.delete("/api/score/${testData.scores[5].id}"))
            .andExpect(status().isNoContent)

        verify(repo, times(1)).deleteById(testData.scores[5].id)
        testData.playerGames[4].scores.map { it.id } shouldNotContain testData.scores[5].id
    }
}
