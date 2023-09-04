@file:OptIn(ExperimentalMaterial3Api::class)

package com.jakubaniola.security

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jakubaniola.security.ui.theme.SecurityTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecurityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    CryptographyUi(filesDir)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun CryptographyUi(fileDir: File) {
    var message by remember {
        mutableStateOf("")
    }

    var result by remember {
        mutableStateOf("")
    }

    val cryptography = Cryptography()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 8.dp),
            value = message,
            onValueChange = { message = it }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 8.dp),
        ) {
            Button(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    val bytes = message.encodeToByteArray()
                    val file = File(fileDir, "secret.txt")
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                    val fos = FileOutputStream(file)

                    result = cryptography.encrypt(
                        byteArray = bytes,
                        outputStream = fos
                    ).decodeToString()
                }
            ) { Text("Encrypt") }
            Button(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    val file = File(fileDir, "secret.text")
                    message = cryptography.decrypt(
                        inputStream = FileInputStream(file)
                    ).decodeToString()
                }
            ) { Text("Decrypt") }
        }

        Text("Result:")
        Text(
            text = result,
            fontWeight = FontWeight.Bold
        )
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SecurityTheme {
        CryptographyUi(File(""))
    }
}