package com.saskcow.bowling.controller

import com.nhaarman.mockito_kotlin.whenever
import com.saskcow.bowling.domain.League
import com.saskcow.bowling.domain.Team
import com.saskcow.bowling.repository.LeagueRepository
import com.saskcow.bowling.repository.TeamRepository
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.isA
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.Arrays
import java.util.Optional

@RunWith(MockitoJUnitRunner::class)
class LeagueControllerTest {

    private val baseurl = "http://localhost"

    @Mock
    private val repo: LeagueRepository? = null
    @Mock
    private val teamRepository: TeamRepository? = null
    private var mockMvc: MockMvc? = null

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(LeagueController(repo!!, teamRepository!!)).build()
    }

    @Test
    @Throws(Exception::class)
    fun addLeague_shouldSaveTheLeague() {
        val league = League(1L, "Brian")
        Team(4L, "Best Team", league)

        whenever(repo!!.save(isA(League::class.java))).thenReturn(league)
        whenever(repo.findById(league.id)).thenReturn(Optional.of(league))
        whenever(repo.findAll()).thenReturn(listOf(league))

        val uri = mockMvc!!.perform(post("/api/league")
                .content("{\"name\":\"Brian\"}")
                .contentType("application/json"))
                .andExpect(status().isCreated)
                .andExpect(header().string("Location", baseurl + "/api/league/" + league.id))
                .andReturn().response.getHeader("Location")

        mockMvc!!.perform(get("/api/league"))
                .andExpect(status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize<Any>(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", equalTo("Brian")))

        uri ?: throw NullPointerException("URI is null")
        mockMvc!!.perform(get("/api/league/${league.id}"))
                .andExpect(status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo("Brian")))
                .andExpect(MockMvcResultMatchers.jsonPath("name", equalTo("Brian")))
    }

    @Test
    @Throws(Exception::class)
    fun getLeague_shouldFilter() {
        val dave = League(1L, "Dave")
        val david = League(2L, "David")
        val brian = League(3L, "Brian")
        val team1 = Team(4L, "Best Team", dave)
        dave.addTeam(team1)
        whenever(repo!!.findByNameContainingIgnoreCase("dav")).thenReturn(Arrays.asList(dave, david))
        whenever(repo.findByNameContainingIgnoreCase("Bri")).thenReturn(listOf(brian))

        mockMvc!!.perform(get("/api/league?name=dav"))
                .andExpect(status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize<Any>(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", equalTo("Dave")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", equalTo("David")))

        mockMvc!!.perform(get("/api/league?name=Bri"))
                .andExpect(status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize<Any>(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", equalTo("Brian")))
    }

    @Test
    @Throws(Exception::class)
    fun deleteLeague_shouldDeleteLeague() {
        doNothing().whenever(repo!!).deleteById(isA(Long::class.java))

        mockMvc!!.perform(delete("/api/league/1"))
                .andExpect(status().isNoContent)
        verify<LeagueRepository>(repo, times(1)).deleteById(1L)
    }
}