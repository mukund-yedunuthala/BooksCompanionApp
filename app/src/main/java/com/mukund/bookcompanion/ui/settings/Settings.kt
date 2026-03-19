package com.mukund.bookcompanion.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mukund.bookcompanion.BuildConfig
import com.mukund.bookcompanion.R
import com.mukund.bookcompanion.ui.settings.components.CustomEntryButton
import com.mukund.bookcompanion.ui.settings.components.CustomEntrySwitch
import com.mukund.bookcompanion.ui.settings.components.CustomURLDialog

private sealed interface DialogState {
    data object None : DialogState
    data class Url(val url: String) : DialogState
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    backPress: () -> Boolean,
    libraries: () -> Unit,
    backup: () -> Unit,
) {
    val attribution = stringResource(R.string.Attribution)
    val source      = stringResource(R.string.Source)
    val policy      = stringResource(R.string.PrivacyPolicy)

    var dialogState by remember { mutableStateOf<DialogState>(DialogState.None) }
    val followSystem = viewModel.followSystemTheme

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text(text = stringResource(R.string.settingsscreen_toplabel))
                },
                navigationIcon = {
                    IconButton(onClick = { backPress() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = stringResource(R.string.settings_back_button_description),
                            modifier = Modifier.size(IconButtonDefaults.mediumIconSize)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SectionHeader(stringResource(R.string.settings_section_general))

                CustomEntrySwitch(
                    leadText = stringResource(R.string.settings_general_system_theme),
                    checked = followSystem,
                    onCheckedChange = { viewModel.saveUserFollowSystemEnabled(it) },
                )

                if (!followSystem) {
                    CustomEntrySwitch(
                        leadText = stringResource(R.string.settings_general_dark_theme),
                        checked = viewModel.hasUserDarkThemeEnabled,
                        onCheckedChange = { viewModel.saveUserDarkThemeEnabled(it) },
                    )
                }

                CustomEntryButton(
                    onClick = { backup() },
                    leadText = stringResource(R.string.settings_general_backup_restore),
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                SectionHeader(stringResource(R.string.settings_section_about))

                // Version entry — no onClick action, so onClick = {}
                CustomEntryButton(
                    onClick = {},
                    leadText = stringResource(R.string.settings_about_app_version),
                    subText = BuildConfig.VERSION_NAME,
                    painter = painterResource(R.drawable.info),
                )
                CustomEntryButton(
                    onClick = { dialogState = DialogState.Url(attribution) },
                    leadText = stringResource(R.string.settings_about_license),
                    subText = stringResource(R.string.settings_about_license_subtext),
                    painter = painterResource(R.drawable.attribution),
                )
                CustomEntryButton(
                    onClick = { dialogState = DialogState.Url(source) },
                    leadText = stringResource(R.string.settings_about_source_code),
                    painter = painterResource(R.drawable.code),
                )
                CustomEntryButton(
                    onClick = { libraries() },
                    leadText = stringResource(R.string.settings_about_libraries),
                    subText = stringResource(R.string.settings_about_libraries_subtext),
                    painter = painterResource(R.drawable.description),
                )
                CustomEntryButton(
                    onClick = { dialogState = DialogState.Url(policy) },
                    leadText = stringResource(R.string.settings_about_privacy_policy),
                    painter = painterResource(R.drawable.policy),
                )
            }
        }
    }

    if (dialogState is DialogState.Url) {
        CustomURLDialog(
            source = (dialogState as DialogState.Url).url,
            onDismiss = { dialogState = DialogState.None },
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}
