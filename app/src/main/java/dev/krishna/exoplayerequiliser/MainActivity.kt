package dev.krishna.exoplayerequiliser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.krishna.exoplayerequiliser.ui.theme.ExoPlayerEquiliserTheme
import dev.krishna.exoplayerequiliser.ui.views.AudioEqualizerScreenXYZ

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExoPlayerEquiliserTheme {
                AudioEqualizerScreenXYZ()
            }
        }
    }
}