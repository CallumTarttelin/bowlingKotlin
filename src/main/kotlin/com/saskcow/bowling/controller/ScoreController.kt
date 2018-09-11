package com.saskcow.bowling.controller

import com.saskcow.bowling.domain.Score
import com.saskcow.bowling.repository.PlayerGameRepository
import com.saskcow.bowling.repository.ScoreRepository
import com.saskcow.bowling.rest.ScoreRest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@Controller
class ScoreController(
    private val repo: ScoreRepository,
    private val playerGameRepository: PlayerGameRepository
) {
    @PostMapping("/api/score")
    fun saveScore(@RequestBody scoreRest: ScoreRest): ResponseEntity<*> {
        val (playerGameId, scratch, handicap, handicapped) = scoreRest
        val optionalPlayerGame = playerGameRepository.findById(
            playerGameId
                ?: return ResponseEntity.badRequest().body("No playerGame ID provided")
        )
        if (!optionalPlayerGame.isPresent) return ResponseEntity.badRequest().body("Invalid playerGame ID")
        val playerGame = optionalPlayerGame.get()
        scratch ?: return ResponseEntity.badRequest().body("No scratch score provided")
        val handicappedForScore = when {
            handicap == null && handicapped == null -> return ResponseEntity.badRequest()
                .body("No handicap or handicapped score")
            handicap == null -> handicapped
            handicapped == null -> scratch + handicap
            handicap + scratch == handicapped -> handicapped
            else -> return ResponseEntity.badRequest().body("handicapped score not equal to scratch + handicap")
        }
        // Bounds checking
        handicappedForScore!!
        if (0 > scratch || scratch > 300) return ResponseEntity.badRequest().body("Invalid scratch score")
        if (handicappedForScore - scratch > 80 || handicappedForScore - scratch < 0) return ResponseEntity.badRequest()
            .body("Invalid handicap")
        if (playerGame.scores.size > 2) return ResponseEntity.badRequest()
            .body("playerGame has quite enough scores already")

        repo.save(Score(playerGame = playerGame, scratch = scratch, handicapped = handicappedForScore))

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .replacePath("/api/game/{id}")
            .buildAndExpand(playerGame.game?.game?.id).toUri()
        return ResponseEntity.created(location).build<URI>()
    }

    @DeleteMapping("/api/score/{id}")
    fun deleteById(@PathVariable("id") id: Long): ResponseEntity<Void> {
        val optionalScore = repo.findById(id)
        return if (!optionalScore.isPresent) ResponseEntity.notFound().build()
        else {
            optionalScore.get().unlink()
            repo.deleteById(optionalScore.get().id)
            ResponseEntity.noContent().build()
        }
    }
}