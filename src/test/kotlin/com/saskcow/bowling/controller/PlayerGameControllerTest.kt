package com.saskcow.bowling.controller

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.saskcow.bowling.domain.Game
import com.saskcow.bowling.domain.League
import com.saskcow.bowling.domain.Player
import com.saskcow.bowling.domain.PlayerGame
import com.saskcow.bowling.domain.Team
import com.saskcow.bowling.repository.GameRepository
import com.saskcow.bowling.repository.PlayerGameRepository
import com.saskcow.bowling.repository.PlayerRepository
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldNotContain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class PlayerGameControllerTest {

    private val baseUrl = "http://localhost"

    @Mock
    private val repo: PlayerGameRepository? = null
    @Mock
    private val gameRepository: GameRepository? = null
    @Mock
    private val playerRepository: PlayerRepository? = null
    private var mockMvc: MockMvc? = null

    @BeforeEach
    fun setUp() {
        mockMvc =
            MockMvcBuilders.standaloneSetup(PlayerGameController(repo!!, gameRepository!!, playerRepository!!)).build()
    }

    // Things to test with
    private val league = League(1L, "City Watch")
    private val teams = listOf(
        Team(2L, "The Night Watch", league).init(),
        Team(3L, "Cable Street Particulars", league).init(),
        Team(17L, "The Day Watch", league).init()
    )
    private val players = listOf(
        Player(5L, "Sam Vimes", teams[0]).init(),
        Player(6L, "Carrot Ironfoundersson", teams[0]).init(),
        Player(7L, "Nobby Nobbs", teams[0]).init(),
        Player(8L, "Findthee Swing", teams[1]).init(),
        Player(9L, "Carcer", teams[1]).init(),
        Player(10L, "Henry 'The Hamster' Higgins", teams[1]).init()
    )
    private val time = LocalDateTime.now()
    private val game = Game(4L, time, "Pseudopolis Yard", league, teams).init()
    private val playerGames = listOf(
        PlayerGame(11L, game.teamGames[0], players[0]),
        PlayerGame(12L, game.teamGames[0], players[1]),
        PlayerGame(13L, game.teamGames[0], players[2]),
        PlayerGame(14L, game.teamGames[1], players[3]).init(),
        PlayerGame(15L, game.teamGames[1], players[4]).init(),
        PlayerGame(16L, game.teamGames[1], players[5]).init()
    )

    @Test
    fun `Adding a player to no game returns 400`() {
        mockMvc!!.perform(
            post("/api/playergame")
                .content(
                    """
                {
                    "playerIds": [5, 6, 7],
                    "teamId": 2,
                }
            """.trimIndent()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding no players to a game returns 400`() {
        mockMvc!!.perform(
            post("/api/playergame")
                .content(
                    """
                {
                    "teamId": 2,
                    "gameId": 4
                }
            """.trimIndent()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding too many players to a team in a Game returns 400`() {
        mockMvc!!.perform(
            post("/api/playergame")
                .content(
                    """
                {
                    "playerIds": [5, 6, 7],
                    "teamId": 3,
                    "gameId": 4
                }
            """.trimIndent()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `No duplicate players for team in game`() {
        mockMvc!!.perform(
            post("/api/playergame")
                .content(
                    """
                {
                    "playerIds": [5, 5, 7],
                    "teamId": 2,
                    "gameId": 4
                }
            """.trimIndent()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding players to a team not in the Game returns 400`() {
        mockMvc!!.perform(
            post("/api/playergame")
                .content(
                    """
                {
                    "playerIds": [5, 6, 7],
                    "teamId": 17,
                    "gameId": 4
                }
            """.trimIndent()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding players to no team in the Game returns 400`() {
        mockMvc!!.perform(
            post("/api/playergame")
                .content(
                    """
                {
                    "playerIds": [5, 6, 7],
                    "gameId": 4
                }
            """.trimIndent()
                )
                .contentType("application/json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Adding a valid team adds a team and returns location of game`() {
        playerRepository!!
        whenever(gameRepository!!.findById(4L)).thenReturn(Optional.of(game))
        doReturn(Optional.of(players[0])).whenever(playerRepository).findById(players[0].id)
        doReturn(Optional.of(players[1])).whenever(playerRepository).findById(players[1].id)
        doReturn(Optional.of(players[2])).whenever(playerRepository).findById(players[2].id)
        whenever(repo!!.save(isA<PlayerGame>()))
            .thenReturn(playerGames[0], playerGames[1], playerGames[2])

        mockMvc!!.perform(
            post("/api/playergame")
                .content(
                    """
                {
                    "playerIds": [5, 6, 7],
                    "teamId": 2,
                    "gameId": 4
                }
            """.trimIndent()
                )
                .contentType("application/json")
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "$baseUrl/api/game/${game.id}"))
    }

    @Test
    fun `Attempts to delete nonexistent playerGame returns 404`() {
        mockMvc!!.perform(MockMvcRequestBuilders.delete("/api/playergame/52"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Can delete players from a game from ID`() {
        whenever(repo!!.findById(playerGames[3].id)).thenReturn(Optional.of(playerGames[3]))

        game.teamGames[1].players.map { it.id } shouldContainAll playerGames.subList(3, 5).map { it.id }

        mockMvc!!.perform(MockMvcRequestBuilders.delete("/api/playergame/14"))
            .andExpect(status().isNoContent)

        game.teamGames[1].players.map { it.id } shouldNotContain playerGames[3].id

        verify(repo, times(1)).deleteById(playerGames[3].id)
    }

//    @Test
//    fun `Can mark player in game as blind`() {
//        // probably by put request on a playerGame, removes player from game and adds a blind somehow
//    }
}