package com.saskcow.bowling.controller

import com.saskcow.bowling.domain.Team
import com.saskcow.bowling.repository.LeagueRepository
import com.saskcow.bowling.repository.TeamRepository
import com.saskcow.bowling.rest.TeamRest
import com.saskcow.bowling.view.TeamView
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
class TeamController(private val repo: TeamRepository, private val leagueRepository: LeagueRepository) {

    @GetMapping("/api/team/{id}")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<TeamView> {
        val optionalTeam = repo.findById(id)
        return if (! optionalTeam.isPresent) ResponseEntity.notFound().build()
        else ResponseEntity.ok(
            TeamView(
                optionalTeam.get(),
                repo.pinsFor(id),
                repo.pinsAgainst(id),
                repo.highHandicapGame(id),
                repo.highHandicapSeries(id),
                repo.teamPoints(id),
                repo.playerPoints(id)
            )
        )
    }

    @PostMapping("/api/team")
    fun saveTeam(@RequestBody teamRest: TeamRest): ResponseEntity<*> {
        val (name, leagueId) = teamRest
        name ?: return ResponseEntity.badRequest().build<Void>()
        val optionalLeague = leagueRepository.findById(leagueId ?: return ResponseEntity.badRequest().build<Void>())
        if (! optionalLeague.isPresent) return ResponseEntity.badRequest().build<Void>()
        val league = optionalLeague.get()

        val team = repo.save(Team(name = name, league = league))

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(team.id).toUri()
        return ResponseEntity.created(location).build<URI>()
    }

    @DeleteMapping("/api/team/{id}")
    fun deleteById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val optionalTeam = repo.findById(id)
        return if (! optionalTeam.isPresent) ResponseEntity.notFound().build()
        else {
            optionalTeam.get().unlink()
            repo.deleteById(optionalTeam.get().id)
            ResponseEntity.noContent().build()
        }
    }
}