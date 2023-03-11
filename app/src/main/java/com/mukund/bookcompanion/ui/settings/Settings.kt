package com.mukund.bookcompanion.ui.settings

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mukund.bookcompanion.BuildConfig
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.settings.components.CustomEntryButton
import com.mukund.bookcompanion.ui.settings.components.CustomURLDialog
import com.mukund.bookcompanion.ui.theme.BooksCompanionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(backPress: () -> Boolean, libraries: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val attribution = stringResource(id = R.string.Attribution)
    val source = stringResource(id = R.string.Source)
    val policy = stringResource(id = R.string.PrivacyPolicy)
    val context = LocalContext.current
    var openSourceDialog = remember { mutableStateOf(false) }
    var openLicenseDialog = remember { mutableStateOf(false) }
    var openPrivacyDialog = remember { mutableStateOf(value = false) }
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { backPress.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Return to home"
                        )
                    }
                },
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                item {
                    CustomEntryButton(
                        onClick = {
                            mToast(context, "Coming Soon!")
                        },
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "App version",
                        leadText = "App Version",
                        subText = BuildConfig.VERSION_NAME + " (${BuildConfig.VERSION_CODE})"
                    )
                    CustomEntryButton(
                        onClick = {openLicenseDialog.value = true},
                        painter = painterResource(id = R.drawable.attribution),
                        contentDescription = "License",
                        leadText = "License",
                        subText = "GNU General Public License v3.0"
                    )
                    CustomEntryButton(
                        onClick = {openSourceDialog.value = true},
                        painter = painterResource(id = R.drawable.code),
                        contentDescription = "Source Code",
                        leadText = "Source Code",
                    )
                    CustomEntryButton(
                        onClick = {
                            libraries()
                        },
                        painter = painterResource(id = R.drawable.description),
                        contentDescription = "Libraries",
                        leadText = "Libraries",
                        subText = "Open source libraries"
                    )
                    CustomEntryButton(
                        onClick = {openPrivacyDialog.value = true},
                        painter = painterResource(id = R.drawable.policy),
                        contentDescription = "Privacy Policy",
                        leadText = "Privacy Policy"
                    )
                }
            }
        }
    )
    if (openSourceDialog.value) {
        CustomURLDialog(openSourceDialog, source, uriHandler)
    }
    if (openLicenseDialog.value) {
        CustomURLDialog(openLicenseDialog, attribution, uriHandler)
    }
    if (openPrivacyDialog.value) {
        CustomURLDialog(openPrivacyDialog, policy, uriHandler)
    }

}
fun mToast(context: Context, text : String){
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsPreview1() {
    BooksCompanionTheme() {
        SettingScreen (backPress = { false }) { false }
    }
}

@Preview
@Composable
fun SettingsPreview2() {
    BooksCompanionTheme() {
        SettingScreen (backPress = { false }) { false }
    }
}