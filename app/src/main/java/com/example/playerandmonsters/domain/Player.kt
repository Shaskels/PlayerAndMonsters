package com.example.playerandmonsters.domain

import kotlin.math.min

class Player(attack: Int, defense: Int, health: Int, damage: Pair<Int, Int>) :
    Creature(attack, defense, health, damage) {

    fun healUp() {
        health = min((0.3 * Constants.HEALTH_MAX).toInt() + health, Constants.HEALTH_MAX)
    }
}