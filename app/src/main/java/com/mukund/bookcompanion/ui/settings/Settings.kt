package com.mukund.bookcompanion.ui.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.BuildConfig
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.settings.components.CustomEntryButton
import com.mukund.bookcompanion.ui.settings.components.CustomEntrySwitch
import com.mukund.bookcompanion.ui.settings.components.CustomURLDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    backPress: () -> Boolean,
    libraries: () -> Unit,
    backup: () -> Unit,
){
    val uriHandler = LocalUriHandler.current
    val attribution = stringResource(id = R.string.Attribution)
    val source = stringResource(id = R.string.Source)
    val policy = stringResource(id = R.string.PrivacyPolicy)
    val context = LocalContext.current
    val openSourceDialog = remember { mutableStateOf(false) }
    val openLicenseDialog = remember { mutableStateOf(false) }
    val openPrivacyDialog = remember { mutableStateOf(value = false) }
    val followSystem = viewModel.followSystemTheme
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Return to home"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Text(
                    text = "General",
                    modifier = Modifier.padding(20.dp),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                CustomEntrySwitch(
                    leadText = "System Theme",
                    boolVal = viewModel.followSystemTheme,
                    onChange = { enabled ->
                        viewModel.saveUserFollowSystemEnabled(enabled)
                    }
                )
                if (!followSystem) {
                    CustomEntrySwitch(
                        leadText = "Dark Theme",
                        boolVal = viewModel.hasUserDarkThemeEnabled,
                        onChange = { enabled ->
                            viewModel.saveUserDarkThemeEnabled(enabled)
                        }
                    )
                }
                CustomEntryButton(
                    onClick = { backup.invoke() },
                    leadText = "Backup & Restore",
                    subText = null
                )
                HorizontalDivider()
                Text(
                    text = "About",
                    modifier = Modifier.padding(20.dp),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                CustomEntryButton(
                    onClick = {
                        // Get the latest version from your API or GitHub releases
//                        val latestVersion: String = getLatestVersion() // Implement this method to retrieve the latest version

                        // Compare the current version with the latest version
                        val currentVersion: String = BuildConfig.VERSION_NAME
//                        if (currentVersion == latestVersion) {
//                            mToast(context, "Running the latest version: $currentVersion")
//                        } else {
//                            mToast(context, "New version available! Latest version: $latestVersion")
//                        }
                    },
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "App version",
                    leadText = "App Version",
                    subText = BuildConfig.VERSION_NAME
                )

                CustomEntryButton(
                    onClick = { openLicenseDialog.value = true },
                    painter = painterResource(id = R.drawable.attribution),
                    contentDescription = "License",
                    leadText = "License",
                    subText = "GNU General Public License v3.0"
                )
                CustomEntryButton(
                    onClick = { openSourceDialog.value = true },
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
                    onClick = { openPrivacyDialog.value = true },
                    painter = painterResource(id = R.drawable.policy),
                    contentDescription = "Privacy Policy",
                    leadText = "Privacy Policy"
                )
            }
        }
    }
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