package com.koreancoach.app.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.koreancoach.app.R

@Composable
fun SpeechIconButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.VolumeUp,
            contentDescription = if (isPlaying) {
                stringResource(R.string.cd_stop_audio)
            } else {
                stringResource(R.string.cd_play_audio)
            }
        )
    }
}
