package com.saskcow.bowling.controller

import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.saskcow.bowling.domain.Player
import com.saskcow.bowling.domain.Team
import com.saskcow.bowling.repository.PlayerRepository
import com.saskcow.bowling.repository.TeamRepository
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotContain
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class PlayerControllerTest {
    private val baseUrl = "http://localhost"

    @Mock
    private val repo: PlayerRepository? = null
    @Mock
    private val teamRepository: TeamRepository? = null
    private var mockMvc: MockMvc? = null

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(PlayerController(repo!!, teamRepository!!)).build()
    }

    @Test
    fun `Adding a player with no team returns 400`() {
        mockMvc!!.perform(
            post("/api/player")
                .content("""{"name": "Sam Vimes", "garbage": "You don't have to test with the garbage. It's garbage."}""")
                .contentType("application/json"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a player with an invalid team returns 400`() {
        whenever(teamRepository!!.findById(1L)).thenReturn(Optional.empty())
        mockMvc!!.perform(
            post("/api/player")
                .content("""{"name": "City Watch", "teamId": 1}""")
                .contentType("application/json"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a player with no name returns 400`() {
        mockMvc!!.perform(
            post("/api/player")
                .content("""{"teamId": 1}""")
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a valid player adds a player and returns location`() {
        val team = Team(1L, "The Night Watch").init()
        val player = Player(2L, "Sam Vimes", team).init()

        whenever(teamRepository!!.findById(1L)).thenReturn(Optional.of(team))
        whenever(repo!!.save(isA<Player>())).thenReturn(player)

        mockMvc!!.perform(
            post("/api/player")
                .content("""{"name": "Sam Vimes", "teamId": 1}""")
                .contentType("application/json"))
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "$baseUrl/api/player/${player.id}"))
    }

    @Test
    fun `Getting a nonexistent team returns 404`() {
        whenever(repo!!.findById(1L)).thenReturn(Optional.empty())
        mockMvc!!.perform(get("/api/player/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Can retrieve a team by ID`() {
        val team = Player(1L, "Sam Vimes", Team(2L, "The Night Watch")).init()

        whenever(repo!!.findById(1L)).thenReturn(Optional.of(team))

        mockMvc!!.perform(get("/api/player/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("name", Matchers.equalTo("Sam Vimes")))
            .andExpect(jsonPath("$.team.name", Matchers.equalTo("The Night Watch")))
    }

    @Test
    fun `Attempts to delete nonexistent team returns 404`() {
        mockMvc!!.perform(delete("/api/player/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Can delete a team from ID`() {
        val team = Team(2L, "The Night Watch").init()
        val player = Player(1L, "Sam Vimes", team).init()

        team.players shouldContain player

        whenever(repo!!.findById(1L)).thenReturn(Optional.of(player))
        doNothing().whenever(repo).deleteById(1L)

        mockMvc!!.perform(delete("/api/player/1"))
            .andExpect(status().isNoContent)

        verify(repo, times(1)).deleteById(1L)
        team.players shouldNotContain player
    }
}