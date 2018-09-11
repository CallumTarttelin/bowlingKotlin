package com.saskcow.bowling.controller

import com.saskcow.bowling.domain.League
import com.saskcow.bowling.repository.LeagueRepository
import com.saskcow.bowling.repository.TeamRepository
import com.saskcow.bowling.rest.LeagueRest
import com.saskcow.bowling.view.LeagueView
import com.saskcow.bowling.view.LeagueViewSummary
import com.saskcow.bowling.view.TeamViewSummaryLeague
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@Controller
class LeagueController(private val repo: LeagueRepository, private val teamRepository: TeamRepository) {

    @GetMapping("/api/league")
    fun findAll(): ResponseEntity<List<LeagueViewSummary>> =
        ResponseEntity.ok(
            repo.findAll().map(::LeagueViewSummary)
        )

    @GetMapping("/api/league", params = ["name"])
    fun findByName(@RequestParam name: String): ResponseEntity<List<LeagueViewSummary>> =
        ResponseEntity.ok(
            repo.findByNameContainingIgnoreCase(name).map(::LeagueViewSummary)
        )

    @GetMapping("/api/league/{id}")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<LeagueView> {
        val optionalLeague = repo.findById(id)
        return if (! optionalLeague.isPresent) ResponseEntity.notFound().build()
        else ResponseEntity.ok(LeagueView(optionalLeague.get(),
            optionalLeague.get().teams.map {
                TeamViewSummaryLeague(
                    it,
                    teamRepository.pinsFor(id),
                    teamRepository.pinsAgainst(id),
                    teamRepository.highHandicapGame(id),
                    teamRepository.highHandicapSeries(id),
                    teamRepository.teamPoints(id),
                    teamRepository.playerPoints(id)
                )
            }
        ))
    }

    @PostMapping("/api/league")
    fun saveLeague(@RequestBody leagueRest: LeagueRest): ResponseEntity<*> {

        val saved = repo.save(League(name = leagueRest.name ?: return ResponseEntity.badRequest().body("No name")))
        val location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(saved.id).toUri()
        return ResponseEntity.created(location).build<URI>()
    }

    @DeleteMapping("/api/league/{id}")
    fun deleteById(@PathVariable("id") id: Long): ResponseEntity<*> =
        try {
            repo.deleteById(id)
            ResponseEntity.noContent().build<Void>()
        } catch (e: EmptyResultDataAccessException) {
            ResponseEntity.notFound().build<Void>()
        }
}