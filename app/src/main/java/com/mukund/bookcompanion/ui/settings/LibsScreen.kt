package com.mukund.bookcompanion.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibsScreen(
    backPress: () -> Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Open Source Libraries"
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

    ) {paddingValues ->
//           LibrariesContainer(
//               modifier = Modifier
//                   .padding(paddingValues)
//                   .background(MaterialTheme.colorScheme.background)
//           )
        val context = LocalContext.current

        val libs = Libs.Builder().withContext(context)
        val listLibs = libs.build().libraries
        var alertFlag = remember { mutableStateOf(false) }
        var alertContent = remember { mutableStateOf("") }
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(items = listLibs) {
                OutlinedCard(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = {
                        alertContent.value = it.licenses.elementAt(0).licenseContent.toString()
                        alertFlag.value = !alertFlag.value
                    }
                ) {
                    Text(
                        text = it.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, top = 10.dp, bottom = 5.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = it.artifactVersion.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, top = 5.dp, bottom = 5.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = it.licenses.elementAt(0).name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, top = 5.dp, bottom = 10.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        if (alertFlag.value) {
            AlertDialog(
                onDismissRequest = { alertFlag.value = false },
                text = {
                    ScrollableLicenseText(alertContent.value)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            alertFlag.value = false
                        },
                    ) {
                        Text("Confirm".uppercase(), Modifier.padding(10.dp))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { alertFlag.value = false },
                    ) {
                        Text("Dismiss".uppercase(), Modifier.padding(10.dp))
                    }
                },
                modifier = Modifier.wrapContentWidth(),
            )
        }
    }
}
@Composable
fun ScrollableLicenseText(licenseText: String) {
    val scrollState = rememberScrollState()
    Text(
        text = licenseText,
        style = TextStyle(fontSize = 14.sp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(scrollState)
            .fillMaxHeight(0.4f) // Adjust the height as needed
    )
}