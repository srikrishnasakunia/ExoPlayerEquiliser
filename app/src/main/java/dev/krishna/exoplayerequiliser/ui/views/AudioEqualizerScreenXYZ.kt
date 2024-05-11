package dev.krishna.exoplayerequiliser.ui.views

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.krishna.exoplayerequiliser.R
import dev.krishna.exoplayerequiliser.ui.viewmodel.AudioEqualizerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioEqualizerScreenXYZ() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.top_app_bar_title))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(50, 145, 150, alpha = 150)
    ) { it->

        val viewModel = hiltViewModel<AudioEqualizerViewModel>()
        val enableEqualizer by viewModel.enableEqualizer.collectAsState()

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = it.calculateTopPadding())
        ) {

            item {
                LiveStreamingScreen(viewModel)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.equalizer_title_text),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Switch(
                        checked = enableEqualizer,
                        onCheckedChange = { viewModel.toggleEqualizer() },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Color.Black,
                            checkedIconColor = Color.Black,
                            uncheckedTrackColor = Color.White,
                            uncheckedBorderColor = Color.Black
                        )
                    )
                }
            }

            item {
                AnimatedVisibility(
                    visible = enableEqualizer,
                    enter = fadeIn() + slideInVertically { fullHeight -> -fullHeight / 2 },
                    exit = fadeOut() + slideOutVertically { fullHeight -> -fullHeight / 3 }
                ) {
                    EqualizerView(viewModel = viewModel)
                }
            }

            item {
                AnimatedVisibility(
                    visible = enableEqualizer,
                    enter = fadeIn() + slideInVertically { fullHeight -> -fullHeight / 2 },
                    exit = fadeOut() + slideOutVertically { fullHeight -> -fullHeight / 2 }
                ) {
                    PresetsView(viewModel = viewModel)
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
