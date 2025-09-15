package com.example.playerandmonsters.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playerandmonsters.R
import com.example.playerandmonsters.domain.Constants
import com.example.playerandmonsters.domain.Creature
import com.example.playerandmonsters.presentation.GameViewModel
import com.example.playerandmonsters.presentation.ViewState
import com.example.playerandmonsters.ui.component.shake

@Composable
fun GameScreen(gameViewModel: GameViewModel, modifier: Modifier) {
    val viewSate by gameViewModel.viewState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .paint(
                painterResource(R.drawable.badfon_minimalizm_art_trava_nebo),
                contentScale = ContentScale.FillBounds
            )
            .padding(10.dp)
    ) {
        if (viewSate.isGameOver) {
            GameOverScreen(viewSate.killedMonstersCount, gameViewModel)
        } else {
            ScoreBox(viewSate.killedMonstersCount, viewSate.isMonsterDead)

            LivesBox(viewSate.lives, viewSate.isHealUpAvailable, gameViewModel)

            Game(viewSate, gameViewModel)
        }
    }
}

@Composable
fun GameOverScreen(killedMonstersCount: Int, gameViewModel: GameViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            stringResource(R.string.the_game_is_over),
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(bottom = 15.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            stringResource(R.string.you_killed_monsters_count, killedMonstersCount),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.CenterHorizontally)
        )

        Button(
            { gameViewModel.restartGame() },
            modifier = Modifier
                .fillMaxWidth(),
        ) { Text(stringResource(R.string.restart_game)) }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ScoreBox(killedMonstersCount: Int, isMonsterDead: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp)
    ) {
        Text(
            stringResource(R.string.killed_monsters_count),
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(end = 5.dp)
        )

        Text(
            killedMonstersCount.toString(),
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.titleLarge
        )

        if (isMonsterDead) Text(
            text = stringResource(R.string.monster_killed_message),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
fun LivesBox(livesCount: Int, isHealUpAvailable: Boolean, gameViewModel: GameViewModel) {
    Row(modifier = Modifier.fillMaxWidth()) {
        repeat(livesCount) {
            Icon(
                painter = painterResource(R.drawable.baseline_favorite_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .padding(end = 3.dp)
                    .size(40.dp)
            )
        }

        repeat(Constants.LIVES_COUNT - livesCount) {
            Icon(
                painter = painterResource(R.drawable.baseline_favorite_border_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .padding(end = 3.dp)
                    .size(40.dp)
            )
        }
        if (isHealUpAvailable) Button(
            onClick = { gameViewModel.healUpPlayer() },
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                stringResource(R.string.heal_up_button),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
fun Game(viewState: ViewState, gameViewModel: GameViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.weight(1f))

        GameMassages(
            isMonsterNew = viewState.isMonsterNew,
            isMonsterAttacking = viewState.isMonsterAttacking,
            isHitFailure = viewState.isHitFailure
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Creature(
                creature = viewState.player,
                hitValue = viewState.monsterHitValue,
                isCreatureAlive = !viewState.isPlayerDead,
                image = painterResource(R.drawable.player_removebg_preview),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
            )

            Creature(
                creature = viewState.monster,
                hitValue = viewState.playerHitValue,
                isCreatureAlive = !viewState.isMonsterDead,
                image = painterResource(R.drawable.angry_green_monster_removebg_preview),
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = { gameViewModel.attackMonster() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            enabled = !viewState.isAttackPressed,
        ) { Text(stringResource(R.string.attack_monster_button)) }
    }
}

@Composable
fun GameMassages(isMonsterNew: Boolean, isMonsterAttacking: Boolean, isHitFailure: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (isMonsterNew) Text(
            stringResource(R.string.new_monster_message),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if (isMonsterAttacking) Text(
            stringResource(R.string.monster_attacks_message),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if (isHitFailure) Text(
            stringResource(R.string.miss_message),
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun Creature(
    creature: Creature,
    hitValue: Int?,
    isCreatureAlive: Boolean,
    image: Painter,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            stringResource(R.string.creature_attack) + creature.attack,
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
        )

        Text(
            stringResource(R.string.creature_defense) + creature.defense,
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
        )

        HPLine(creature.health, hitValue)

        AnimatedVisibility(
            visible = isCreatureAlive,
            enter = expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(durationMillis = 500)
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 800)
            )
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.shake(hitValue != null)
            )
        }
    }
}

@Composable
fun HPLine(health: Int, hitValue: Int?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
    ) {
        Text(
            stringResource(R.string.creature_hp),
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.bodyLarge,
        )

        Text(
            "${health}/${Constants.HEALTH_MAX}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.surface
        )

        Spacer(modifier = Modifier.weight(1f))

        if (hitValue != null) Text(
            "-${hitValue}",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    LinearProgressIndicator(
        progress = { health.toFloat() / Constants.HEALTH_MAX },
        color = MaterialTheme.colorScheme.scrim,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp)
            .height(20.dp)
    )
}
