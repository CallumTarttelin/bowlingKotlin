package com.saskcow.bowling.controller

import com.saskcow.bowling.domain.PlayerGame
import com.saskcow.bowling.repository.GameRepository
import com.saskcow.bowling.repository.PlayerGameRepository
import com.saskcow.bowling.repository.PlayerRepository
import com.saskcow.bowling.rest.PlayerGameRest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
class PlayerGameController(
    private val repo: PlayerGameRepository,
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository
) {
    @PostMapping("/api/playergame")
    fun addPlayerToGame(@RequestBody playerGameRest: PlayerGameRest): ResponseEntity<*> {
        val (playerIds, teamId, gameId) = playerGameRest
        val optionalGame =
            gameRepository.findById(gameId ?: return ResponseEntity.badRequest().body("Game is not given"))
        val optionalPlayers = playerIds.map { playerRepository.findById(it) }.filter { it.isPresent }

        if (!optionalGame.isPresent) {
            return ResponseEntity.badRequest().body("Game does not exist")
        }
        if (optionalPlayers.isEmpty()) {
            return ResponseEntity.badRequest().body("No Valid players given")
        }

        val game = optionalGame.get()
        val players = optionalPlayers.map { it.get() }

        val validTeams = game.teamGames.filter { it.team?.id == teamId }
        if (validTeams.size != 1) {
            return ResponseEntity.badRequest().body("No team matches ID or team in game twice")
        }
        val teamGame = validTeams[0]
        if (teamGame.players.size + players.size > 3) {
            return ResponseEntity.badRequest().body("Too many players for team")
        }

        if (players.isEmpty()) return ResponseEntity.badRequest().body("No players to be added")
        players.forEach {
            val playerGame = repo.save(PlayerGame(game = teamGame, player = it))
            teamGame.addPlayer(playerGame)
            repo.save(playerGame)
        }

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .replacePath("/api/game/{id}")
            .buildAndExpand(game.id).toUri()
        return ResponseEntity.created(location).build<URI>()
    }

    @DeleteMapping("/api/playergame/{id}")
    fun deleteById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val optionalPlayerGame = repo.findById(id)
        return if (!optionalPlayerGame.isPresent) ResponseEntity.notFound().build()
        else {
            val playerGame = optionalPlayerGame.get()
            playerGame.unlink()
            repo.deleteById(playerGame.id)
            ResponseEntity.noContent().build()
        }
    }
}