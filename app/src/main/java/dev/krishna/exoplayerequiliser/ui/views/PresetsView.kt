package dev.krishna.exoplayerequiliser.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.krishna.exoplayerequiliser.R
import dev.krishna.exoplayerequiliser.ui.viewmodel.AudioEqualizerViewModel
import dev.krishna.exoplayerequiliser.utils.effect

@Composable
fun PresetsView(viewModel: AudioEqualizerViewModel = viewModel()) {
    
    val audioEffects by viewModel.audioEffects.collectAsState()

    val groupedList = effect.chunked(4)

    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Divider(
            modifier = Modifier
                .weight(1f)
                .height(4.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color.White,
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.presets_title_text),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier
                .wrapContentWidth()
                .weight(0.5f)
                .padding(4.dp)
                .zIndex(1f),
            textAlign = TextAlign.Center
        )

        Divider(
            modifier = Modifier
                .weight(1f)
                .height(4.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color.White,
            thickness = 1.dp
        )
    }

    Spacer(modifier = Modifier.height(20.dp))


    for (itemList in groupedList){
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val horizontalPadding = if (maxWidth < 320.dp)
                8.dp
            else if (maxWidth > 400.dp)
                40.dp
            else
                20.dp

            val horizontalSpacing = if (maxWidth > 400.dp)
                24.dp
            else
                16.dp

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    space = horizontalSpacing,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (item in itemList){
                    val index by remember {
                        mutableIntStateOf(
                            effect.indexOf(
                                item
                            )
                        )
                    }

                    BoxWithConstraints(
                        modifier = Modifier
                            .wrapContentSize()
                            .border(
                                1.dp,
                                if (index == audioEffects?.selectedEffectType) Color.White else Color.Black,
                                RoundedCornerShape(40.dp)
                            )
                            .clip(RoundedCornerShape(40.dp))
                            .clickable {
                                viewModel.onSelectPreset(index)
                            }
                            .background(if (index == audioEffects?.selectedEffectType) Color.Black else Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(
                                    horizontal = horizontalPadding,
                                    vertical = 12.dp
                                ),
                            fontSize = 14.sp,
                            color = if (index == audioEffects?.selectedEffectType) Color.White else Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun previewPresets(){
    PresetsView()
}