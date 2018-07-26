package com.saskcow.bowling.controller

import com.saskcow.bowling.domain.Player
import com.saskcow.bowling.repository.PlayerRepository
import com.saskcow.bowling.repository.TeamRepository
import com.saskcow.bowling.rest.PlayerRest
import com.saskcow.bowling.view.PlayerView
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@Controller
class PlayerController(private val repo: PlayerRepository, private val teamRepository: TeamRepository) {

    @GetMapping("/api/player/{id}")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<PlayerView> {
        val optionalPlayer = repo.findById(id)
        return if (! optionalPlayer.isPresent) ResponseEntity.notFound().build()
        else ResponseEntity.ok(PlayerView(optionalPlayer.get()))
    }

    @PostMapping("/api/player")
    fun savePlayer(@RequestBody playerRest: PlayerRest): ResponseEntity<*> {
        val (name, teamId) = playerRest
        name ?: return ResponseEntity.badRequest().build<Void>()
        val optionalTeam = teamRepository.findById(teamId ?: return ResponseEntity.badRequest().build<Void>())
        if (! optionalTeam.isPresent) return ResponseEntity.badRequest().build<Void>()
        val team = optionalTeam.get()

        val player = repo.save(Player(name = name, team = team))
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(player.id).toUri()
        return ResponseEntity.created(location).build<URI>()
    }

    @DeleteMapping("/api/player/{id}")
    fun deleteById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val optionalPlayer = repo.findById(id)
        return if (! optionalPlayer.isPresent) ResponseEntity.notFound().build()
        else {
            optionalPlayer.get().unlink()
            repo.deleteById(optionalPlayer.get().id)
            ResponseEntity.noContent().build()
        }
    }
}