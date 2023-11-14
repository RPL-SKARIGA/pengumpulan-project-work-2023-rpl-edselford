package com.edselmustapa.mywallet.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.edselmustapa.mywallet.config.Setting
import com.edselmustapa.mywallet.lib.PreferencesViewModel
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
fun ChangeCardBottomSheet(
    dismissRequest: () -> Unit,
    bottomSheetState: SheetState,
    cardImages: List<Pair<String, Int>>,
    preferences: PreferencesViewModel,
    setting: Setting
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        onDismissRequest = {
            dismissRequest()
        },
        sheetState = bottomSheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            var selectedCard by remember { mutableStateOf<String?>(null) }

            Button(
                onClick = {
                    preferences.save(setting.copy(cardColor = selectedCard ?: "card_1"))
                    dismissRequest()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedCard != null
            ) {
                Text("Change Card")
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                maxItemsInEachRow = 2
            ) {
                val context = LocalContext.current


                cardImages.forEach { (char, svg) ->
                    AsyncImage(
                        modifier = Modifier
                            .weight(.5f)
                            .padding(vertical = 5.dp)
                            .then(
                                if (selectedCard == char) Modifier.border(
                                    2.dp,
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(10.dp)
                                ) else Modifier
                            )
                            .clickable {
                                selectedCard = char
                            },
                        model = ImageRequest.Builder(context)
                            .data(svg)
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = "",
                    )
                }

                Spacer(modifier = Modifier.weight(.5f))
            }



            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

