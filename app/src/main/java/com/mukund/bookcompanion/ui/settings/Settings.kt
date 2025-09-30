package com.mukund.bookcompanion.ui.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
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
                        text = stringResource(R.string.settingsscreen_toplabel),
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { backPress.invoke() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = stringResource(R.string.settings_back_button_description),
                            modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
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
                    text = stringResource(R.string.settings_section_general),
                    modifier = Modifier.padding(20.dp),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                CustomEntrySwitch(
                    leadText = stringResource(R.string.settings_general_system_theme),
                    boolVal = viewModel.followSystemTheme,
                    onChange = { enabled ->
                        viewModel.saveUserFollowSystemEnabled(enabled)
                    }
                )
                if (!followSystem) {
                    CustomEntrySwitch(
                        leadText = stringResource(R.string.settings_general_dark_theme),
                        boolVal = viewModel.hasUserDarkThemeEnabled,
                        onChange = { enabled ->
                            viewModel.saveUserDarkThemeEnabled(enabled)
                        }
                    )
                }
                CustomEntryButton(
                    onClick = { backup.invoke() },
                    leadText = stringResource(R.string.settings_general_backup_restore),
                    subText = null
                )
                HorizontalDivider()
                Text(
                    text = stringResource(R.string.settings_section_about),
                    modifier = Modifier.padding(20.dp),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                CustomEntryButton(
                    onClick = {
                    },
                    painter = painterResource(R.drawable.info),
                    contentDescription = stringResource(R.string.settings_about_app_version),
                    leadText = stringResource(R.string.settings_about_app_version),
                    subText = BuildConfig.VERSION_NAME
                )

                CustomEntryButton(
                    onClick = { openLicenseDialog.value = true },
                    painter = painterResource(id = R.drawable.attribution),
                    contentDescription = stringResource(R.string.settings_about_license),
                    leadText = stringResource(R.string.settings_about_license),
                    subText = stringResource(R.string.settings_about_license_subtext),
                )
                CustomEntryButton(
                    onClick = { openSourceDialog.value = true },
                    painter = painterResource(id = R.drawable.code),
                    contentDescription = stringResource(R.string.settings_about_source_code),
                    leadText = stringResource(R.string.settings_about_source_code),
                )
                CustomEntryButton(
                    onClick = {
                        libraries()
                    },
                    painter = painterResource(id = R.drawable.description),
                    contentDescription = stringResource(R.string.settings_about_libraries),
                    leadText = stringResource(R.string.settings_about_libraries),
                    subText = stringResource(R.string.settings_about_libraries_subtext),
                )
                CustomEntryButton(
                    onClick = { openPrivacyDialog.value = true },
                    painter = painterResource(id = R.drawable.policy),
                    contentDescription = stringResource(R.string.settings_about_privacy_policy),
                    leadText = stringResource(R.string.settings_about_privacy_policy)
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