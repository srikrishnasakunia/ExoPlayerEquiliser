package dev.krishna.exoplayerequiliser.ui.views

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import dev.krishna.exoplayerequiliser.utils.ExoPlayerManager

@OptIn(UnstableApi::class) @Composable
fun LiveStreamingScreen() {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    //Using remember because we want to keep Exoplayer Instance persist across all recompositions.
    val exoPlayer = remember {
        ExoPlayerManager.getExoPlayer(context)
    }

    // Using LaunchedEffect to launch the Exoplayer and set up the media source.
    LaunchedEffect(key1 = Unit){
        // Creating a DataSourceFactory for handling the Media Requests.
        val dataSourceFactory = DefaultHttpDataSource.Factory()

        // Using a URI builder to specify the URI for the Media Source.
        val uri = Uri.Builder()
            .encodedPath("http://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8")
            .build()

        val mediaItem = MediaItem.Builder().setUri(uri).build()

        /**Creating a internetVideoSource for handling HttpLiveStream(HLS) to handle data from the URI.
           Basically we are initialising HLSMediaSource with a dataSourceFactory and then
           passing the media item containing the uri.**/
        val internetVideoSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)

        exoPlayer.setMediaSource(internetVideoSource)
        exoPlayer.prepare()

        //viewModel.onStart(exoPlayer.audioSessionId)
    }

    //Creating the UI for the Player.
    Box(modifier = Modifier.fillMaxSize()) {
        /**
         * AndroidView is a interop or interoperability library provided to use Custom Android Views
         * that are yet not availabe for Compose.
         * Same thing can be done with Compose views whileusing them in Normal View (XML).
         * */

        /**
         * AndroidView takes in a factory parameter which takes in Context and returns the rendered
         * View instance. This Factory function is only executed when the view is initially created
         * or when the parent composable recomposes.
         * State updates within factory wont trigger recomposition, we need to use the update callback
         * for dynamic updates.
         * */
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.4f)
                .padding(top = 16.dp)
                .background(Color.Black),
            factory = {
                // PlayerView is a default View in Compose used for Creating default video players.
                // Consider using Player for creating custom video players and it doesn't require AndroidView as parent.
                PlayerView(context).apply {
                    player = exoPlayer
                    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE // Setting Player to keep playing the same video infinite times.
                    exoPlayer.playWhenReady = false // Setting Player to start playing as soon as all resources are available for playing.
                    useController = true // Shows controller for User to control like play,pause etc.
                }
            }
        )
    }
    /**
     * DisposableEffect is used when we want to use a side effect that requires cleanup when the
     * composable leaves the composition (unmounts) or the keys change.
     * Generally for preventing memory leaks and for resource management.*/
    DisposableEffect(key1 = lifeCycleOwner){
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_RESUME) {
                exoPlayer.playWhenReady = true
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                exoPlayer.playWhenReady = false
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        // OnDispose block is called when the Key parameter of DisposableEffect is changed.
        // Basically OnDispose block is used to specify what we want to do when the Composable is
        // going out of the composition.
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Releasing the Exoplayer once the Composable goes out of scope for better Resource Management.
    DisposableEffect(key1 = Unit){
        onDispose {
            ExoPlayerManager.releaseExoPlayer()
        }
    }
}