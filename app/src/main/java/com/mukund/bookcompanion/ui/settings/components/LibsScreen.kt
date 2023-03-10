package com.mukund.bookcompanion.ui.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
                        .padding(10.dp)
                        .clickable(enabled = false) {
                            alertFlag.value = true
                            alertContent.value = it.licenses.elementAt(0).licenseContent.toString()
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
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
               Surface(
                   shape = MaterialTheme.shapes.extraLarge
               ) {
                   LazyColumn() {
                       items(0) {
                           Text(
                               text = alertContent.value,
                               modifier = Modifier.padding(10.dp)
                           )
                       }
                   }
               }
            }
        }
    }
}