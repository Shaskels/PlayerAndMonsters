package com.example.playerandmonsters.domain.usecase

import com.example.playerandmonsters.domain.Constants
import com.example.playerandmonsters.domain.Monster
import javax.inject.Inject
import kotlin.random.Random

class GetNewMonster @Inject constructor() {
    operator fun invoke(): Monster {
        return Monster(
                Random.nextInt(Constants.ATTACK_MIN, Constants.ATTACK_MAX),
                Random.nextInt(Constants.DEFENSE_MIN, Constants.DEFENSE_MAX),
                Random.nextInt(Constants.HEALTH_MIN + 1, Constants.HEALTH_MAX),
                Constants.DAMAGE_MIN to Constants.DAMAGE_MAX
            )
    }
}