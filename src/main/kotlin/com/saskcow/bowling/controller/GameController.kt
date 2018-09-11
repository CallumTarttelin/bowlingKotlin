package com.saskcow.bowling.controller

import com.saskcow.bowling.domain.Game
import com.saskcow.bowling.repository.GameRepository
import com.saskcow.bowling.repository.LeagueRepository
import com.saskcow.bowling.repository.TeamRepository
import com.saskcow.bowling.rest.GameRest
import com.saskcow.bowling.view.GameView
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Controller
class GameController(
    val repo: GameRepository,
    val leagueRepository: LeagueRepository,
    val teamRepository: TeamRepository
) {
    @GetMapping("/api/game/{id}")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<GameView> {
        val optional = repo.findById(id)
        return if (optional.isPresent) ResponseEntity.ok(GameView(optional.get()))
        else ResponseEntity.notFound().build()
    }

    @PostMapping("/api/game")
    fun saveGame(@RequestBody gameRest: GameRest): ResponseEntity<*> {
        // Check everything is present and not null
        val (leagueId, teamIds, time, venue) = gameRest
        // Check datetime is valid
        val localDateTime = try {
            LocalDateTime.parse(
                time ?: return ResponseEntity.badRequest().body("Local date time is null"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
            )
        } catch (e: DateTimeParseException) {
            return ResponseEntity.badRequest().body("Datetime not in ISO local date time format")
        }
        venue ?: return ResponseEntity.badRequest().build<Void>()
        if (teamIds.size != 2 || teamIds[0] == teamIds[1]) {
            return ResponseEntity.badRequest().body("Incorrect number of unique teams")
        }
        // Check optionals from database
        val optionalLeague = leagueRepository.findById(leagueId ?: return ResponseEntity.badRequest().build<Void>())
        val optionalTeams = teamIds.map { teamRepository.findById(it) }.filter { it.isPresent }
        if (!optionalLeague.isPresent || optionalTeams.size != 2) {
            return ResponseEntity.badRequest().build<Void>()
        }
        val league = optionalLeague.get()
        val teams = optionalTeams.map { it.get() }

        // Make game and return
        val game = repo.save(Game(time = localDateTime, venue = venue, league = league, teams = teams))
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(game.id).toUri()
        return ResponseEntity.created(location).build<URI>()
    }

    @PostMapping("/api/game/{id}")
    fun completeGame(@PathVariable("id") id: Long): ResponseEntity<*> {
        val optionalGame = repo.findById(id)
        if (!optionalGame.isPresent) return ResponseEntity.notFound().build<Void>()
        val game = optionalGame.get()
        return try {
            game.completeGame()
            repo.save(game)
            ResponseEntity.noContent().build<Void>()
        } catch (err: Exception) {
            ResponseEntity.badRequest().body(err.message)
        }
    }

    @DeleteMapping("/api/game/{id}")
    fun deleteById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val optionalGame = repo.findById(id)
        return if (!optionalGame.isPresent) ResponseEntity.notFound().build()
        else {
            optionalGame.get().unlink()
            repo.deleteById(optionalGame.get().id)
            ResponseEntity.noContent().build()
        }
    }
}
