package com.example.playerandmonsters.domain

import kotlin.math.max
import kotlin.random.Random

sealed class Creature(attack: Int, defense: Int, health: Int, damage: Pair<Int, Int>) {
    val attack: Int =
        if (attack in Constants.ATTACK_MIN..Constants.ATTACK_MAX) attack
        else throw IllegalArgumentException()
    val defense: Int =
        if (defense in Constants.DEFENSE_MIN..Constants.DEFENSE_MAX) defense
        else throw IllegalArgumentException()
    var health: Int =
        if (health in Constants.HEALTH_MIN..Constants.HEALTH_MAX) health
        else throw IllegalArgumentException()
        protected set(value) {
            if (health in Constants.HEALTH_MIN..Constants.HEALTH_MAX) field = value
            else throw IllegalArgumentException()
        }
    private val damage: Pair<Int, Int> =
        if (damage.first <= damage.second) damage
        else throw IllegalArgumentException()


    private fun throwTheDice(): Int {
        return Random.nextInt(1, 6)
    }

    private fun takeTheDamage(creature: Creature): Int {
        val gotDamage = Random.nextInt(creature.damage.first, creature.damage.second)
        health = max(health - gotDamage, Constants.HEALTH_MIN)
        return gotDamage
    }

    fun hitAnotherCreature(creature: Creature): Int? {
        val attackModifier = max(attack - creature.defense + 1, 1)
        var isSuccess = false
        for (i in 1..attackModifier){
            if (throwTheDice() >= Constants.HIT_SUCCESS) {
                isSuccess = true
                break
            }
        }
        return if (isSuccess) {
            creature.takeTheDamage(this)
        } else null
    }
}