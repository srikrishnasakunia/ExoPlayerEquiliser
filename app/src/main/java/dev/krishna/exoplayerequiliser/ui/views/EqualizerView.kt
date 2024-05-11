@file:JvmName("AudioEqualizerScreenKt")

package dev.krishna.exoplayerequiliser.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.krishna.exoplayerequiliser.ui.viewmodel.AudioEqualizerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerView(viewModel: AudioEqualizerViewModel) {

    val xAxis = listOf("60Hz","230Hz","910Hz","3kHz","14kHz")

    val audioEffects by viewModel.audioEffects.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .graphicsLayer {
                rotationZ = 270f
            },
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (index in xAxis.indices) {
            Row (
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(220.dp)
            ){
                Box {
                    Text(
                        text = xAxis[index],
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.CenterStart)
                            .rotate(90f),
                        color = Color.White,
                        fontSize = 8.sp,
                        textAlign = TextAlign.Start
                    )
                    
                    Slider(
                        modifier = Modifier.offset(x = 20.dp),
                        value = audioEffects!!.gainValues[index]
                            .times(1000f)
                            .toFloat()
                            .coerceIn(-300f,300f),
                        onValueChange = {
                            viewModel.onBandLevelChanged(index,it.toInt())
                        },
                        valueRange = -300f..300f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Black,
                            inactiveTrackColor = Color.White,
                            activeTrackColor = Color.Black
                        ),
                        thumb = {
                            Box (
                                modifier = Modifier
                                    .size(20.dp)
                                    .border(
                                        1.dp,
                                        Color.White,
                                        CircleShape
                                    )
                                    .clip(CircleShape)
                                    .background(Color.Black, CircleShape)
                            )
                        }
                    )
                }
            }
        }
    }

}