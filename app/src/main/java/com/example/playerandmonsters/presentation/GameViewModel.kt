package com.example.playerandmonsters.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playerandmonsters.domain.Constants
import com.example.playerandmonsters.domain.Player
import com.example.playerandmonsters.domain.usecase.GetNewMonster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val getNewMonster: GetNewMonster) : ViewModel() {

    private var player = Player(
        Constants.PLAYER_ATTACK_DEFAULT,
        Constants.PLAYER_DEFENSE_DEFAULT,
        Constants.PLAYER_HEALTH_DEFAULT,
        Constants.DAMAGE_MIN to Constants.DAMAGE_MAX
    )
    private var monster = getNewMonster()

    private val _viewState = MutableStateFlow(
        ViewState(
            player,
            monster,
            0,
            Constants.LIVES_COUNT,
            false,
            false,
            false,
            null,
            null,
            false,
            false,
            false,
            false,
            false,
        )
    )
    val viewState: StateFlow<ViewState> = _viewState

    fun restartGame() {
        player = Player(
            Constants.PLAYER_ATTACK_DEFAULT,
            Constants.PLAYER_DEFENSE_DEFAULT,
            Constants.PLAYER_HEALTH_DEFAULT,
            Constants.DAMAGE_MIN to Constants.DAMAGE_MAX
        )
        monster = getNewMonster()
        _viewState.value = ViewState(
            player,
            monster,
            0,
            Constants.LIVES_COUNT,
            false,
            false,
            false,
            null,
            null,
            false,
            false,
            false,
            false,
            false,
        )
    }

    fun attackMonster() {
        viewModelScope.launch(Dispatchers.Default) {
            val res = player.hitAnotherCreature(monster)
            _viewState.update { currentState ->
                currentState.copy(
                    monster = monster,
                    killedMonstersCount = if (monster.health == 0) currentState.killedMonstersCount + 1 else currentState.killedMonstersCount,
                    isMonsterDead = monster.health == 0,
                    isHitFailure = res == null,
                    playerHitValue = res,
                    isAttackPressed = true,
                )
            }
            delay(1000)
            if (monster.health != 0) {
                prepareMonsterAttack()
                delay(1000)
                attackPlayer()
            } else {
                appearNewMonster()
            }
            delay(1000)
            resetState()
        }
    }

    private suspend fun attackPlayer() {
        val res = monster.hitAnotherCreature(player)
        _viewState.update { currentState ->
            currentState.copy(
                player = player,
                isPlayerDead = player.health == 0,
                isHitFailure = res == null,
                monsterHitValue = res,
            )
        }
        if (player.health == 0) {
            delay(800)
            _viewState.update { currentState ->
                currentState.copy(
                    isHealUpAvailable = currentState.lives != 0,
                    isGameOver = currentState.lives == 0,
                )
            }
        }
    }

    private fun resetState() {
        _viewState.update { currentState ->
            currentState.copy(
                isAttackPressed = currentState.isHealUpAvailable,
                monsterHitValue = null,
                playerHitValue = null,
                isHitFailure = false,
                isMonsterAttacking = false,
                isMonsterNew = false
            )
        }
    }

    private fun prepareMonsterAttack() {
        _viewState.update { currentState ->
            currentState.copy(
                monsterHitValue = null,
                playerHitValue = null,
                isHitFailure = false,
                isMonsterAttacking = true,
            )
        }
    }

    fun healUpPlayer() {
        player.healUp()
        _viewState.update { currentState ->
            currentState.copy(
                player = player,
                lives = currentState.lives - 1,
                isPlayerDead = false,
                isHealUpAvailable = false,
                isAttackPressed = false,
            )
        }
    }

    private fun appearNewMonster() {
        monster = getNewMonster()
        _viewState.update { currentState ->
            currentState.copy(
                playerHitValue = null,
                isHitFailure = false,
                monster = monster,
                isMonsterDead = false,
                isMonsterNew = true,
            )
        }
    }
}