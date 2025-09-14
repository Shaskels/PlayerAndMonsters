package com.example.playerandmonsters.presentation

import com.example.playerandmonsters.domain.Monster
import com.example.playerandmonsters.domain.Player

data class ViewState(
    val player: Player,
    val monster: Monster,
    val killedMonstersCount: Int,
    val lives: Int,
    val isMonsterDead: Boolean,
    val isPlayerDead: Boolean,
    val isHitFailure: Boolean,
    val playerHitValue: Int?,
    val monsterHitValue: Int?,
    val isAttackPressed: Boolean,
    val isHealUpAvailable: Boolean,
    val isMonsterAttacking: Boolean,
    val isMonsterNew: Boolean,
    val isGameOver: Boolean,
    )