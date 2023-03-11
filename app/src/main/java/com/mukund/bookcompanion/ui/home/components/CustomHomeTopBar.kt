package com.mukund.bookcompanion.ui.home.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.settings.mToast

@ExperimentalMaterial3Api
@Composable
fun CustomHomeTopBar(
    settings: () -> Unit,
    haptic: HapticFeedback,
    context: Context,
): Unit {
    //var scopeState by remember { mutableStateOf(0) }
    //val titles = listOf("All", "Read", "Unread")
    Column {
        LargeTopAppBar(
                title = {
                    Text(
                        text = "Book Companion",
                        fontWeight = FontWeight.W400,
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                actions = {
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        mToast(context, "Coming soon!")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.sort),
                            contentDescription = "Sort",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        settings.invoke()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            )
    }
}