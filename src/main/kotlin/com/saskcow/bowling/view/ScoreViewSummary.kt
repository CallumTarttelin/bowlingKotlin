package com.saskcow.bowling.view

import com.saskcow.bowling.domain.Score
import com.saskcow.bowling.domain.TeamPlayerGameScore

class ScoreViewSummary private constructor() {
    var id = -1L
    var scratch = -1
    var handicapped = -1
    var score = 0
    var total = false

    constructor(score: TeamPlayerGameScore) : this() {
        this.id = score.id
        this.scratch = score.scratch
        this.handicapped = score.handicapped
        this.score = score.score
        this.total = score.total
    }

    constructor(score: Score) : this() {
        this.id = score.id
        this.scratch = score.scratch
        this.handicapped = score.handicapped
        this.score = score.score
        this.total = score.total
    }
}