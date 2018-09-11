package com.saskcow.bowling.controller

import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.saskcow.bowling.domain.League
import com.saskcow.bowling.domain.Player
import com.saskcow.bowling.domain.Team
import com.saskcow.bowling.repository.LeagueRepository
import com.saskcow.bowling.repository.TeamRepository
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotContain
import org.hamcrest.Matchers.equalTo
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
class TeamControllerTest {

    private val baseUrl = "http://localhost"

    @Mock
    private val repo: TeamRepository? = null
    @Mock
    private val leagueRepository: LeagueRepository? = null
    private var mockMvc: MockMvc? = null

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(TeamController(repo!!, leagueRepository!!)).build()
    }

    @Test
    fun `Adding a team with no league returns 400`() {
        mockMvc!!.perform(post("/api/team")
            .content("""{"name": "The Night Watch", "garbage": "You don't have to test with the garbage. It's garbage."}""")
            .contentType("application/json"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a team with an invalid league returns 400`() {
        whenever(leagueRepository!!.findById(1L)).thenReturn(Optional.empty())
        mockMvc!!.perform(post("/api/team")
            .content("""{"name": "City Watch", "leagueId": 1}""")
            .contentType("application/json"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a team with no name returns 400`() {
        mockMvc!!.perform(
            post("/api/team")
                .content("""{"leagueId": 1}""")
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a valid team adds a team and returns location`() {
        val league = League(1L, "City Watch")
        val team = Team(2L, "The Night Watch", league).init()

        whenever(leagueRepository!!.findById(1L)).thenReturn(Optional.of(league))
        whenever(repo!!.save(isA<Team>())).thenReturn(team)

        mockMvc!!.perform(post("/api/team")
            .content("""{"name": "City Watch", "leagueId": 1}""")
            .contentType("application/json"))
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "$baseUrl/api/team/${team.id}"))
    }

    @Test
    fun `Getting a nonexistent team returns 404`() {
        whenever(repo!!.findById(1L)).thenReturn(Optional.empty())
        mockMvc!!.perform(get("/api/team/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Can retrieve a team by ID`() {
        val team = Team(1L, "The Night Watch", League(2L, "City Watch")).init()

        whenever(repo!!.findById(1L)).thenReturn(Optional.of(team))
        mockMvc!!.perform(get("/api/team/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("name", equalTo("The Night Watch")))
            .andExpect(jsonPath("$.league.name", equalTo("City Watch")))
    }

    @Test
    fun `Can retrieve a team with players`() {
        val team = Team(1L, "The Night Watch", League(2L, "City Watch")).init()
        Player(2L, "Sam Vimes", team)

        whenever(repo!!.findById(1L)).thenReturn(Optional.of(team))

        mockMvc!!.perform(get("/api/team/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("name", equalTo("The Night Watch")))
            .andExpect(jsonPath("$.league.name", equalTo("City Watch")))
    }

    @Test
    fun `Attempts to delete nonexistent team returns 404`() {
        mockMvc!!.perform(delete("/api/team/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Can delete a team from ID`() {
        val league = League(2L, "City Watch")
        val team = Team(1L, "The Night Watch", league).init()

        league.teams shouldContain team

        whenever(repo!!.findById(1L)).thenReturn(Optional.of(team))
        doNothing().whenever(repo).deleteById(1L)

        mockMvc!!.perform(delete("/api/team/1"))
            .andExpect(status().isNoContent)

        verify(repo, times(1)).deleteById(1L)
        league.teams shouldNotContain team
    }
}