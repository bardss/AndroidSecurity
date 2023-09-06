@file:OptIn(ExperimentalMaterial3Api::class)

package com.jakubaniola.security.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jakubaniola.security.utils.cryptography.Cryptography
import com.jakubaniola.security.utils.cryptography.CryptographyFile
import java.io.File

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun CryptographyUi(fileDir: File) {
    var isCryptographyByFileSelected by remember {
        mutableStateOf(true)
    }
    var isCryptographySelected by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Type:",
            fontWeight = FontWeight.Bold
        )

        RadioTextButton(
            text = "Cryptography by file",
            selected = isCryptographyByFileSelected,
        ) {
            isCryptographyByFileSelected = true
            isCryptographySelected = false
        }

        RadioTextButton(
            text = "Cryptography NOT by file",
            selected = isCryptographySelected,
        ) {
            isCryptographyByFileSelected = false
            isCryptographySelected = true
        }

        when {
            isCryptographyByFileSelected -> CryptographyFileUi(fileDir)
            isCryptographySelected -> CryptographyUi()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun CryptographyFileUi(fileDir: File) {
    var input by remember {
        mutableStateOf("")
    }

    var encryptionOutput by remember {
        mutableStateOf("")
    }

    var decryptionOutput by remember {
        mutableStateOf("")
    }

    val cryptographyFile = CryptographyFile(fileDir)

    Text("Algorithm:")
    Text(
        text = cryptographyFile.cryptography.transformation,
        fontWeight = FontWeight.Bold
    )

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp),
        value = input,
        onValueChange = { input = it },
        label = { Text("Input") }
    )

    Button(
        modifier = Modifier
            .padding(8.dp, 8.dp),
        onClick = {
            encryptionOutput = cryptographyFile.encrypt(input)
        }
    ) { Text("Encrypt to file") }

    Text("Encrypted output (starts with IV):")
    Text(
        text = encryptionOutput,
        fontWeight = FontWeight.Bold
    )

    Button(
        modifier = Modifier
            .padding(8.dp, 8.dp),
        onClick = {
            decryptionOutput = cryptographyFile.decrypt()
        }
    ) { Text("Decrypt from file") }

    Text("Decrypted from file output:")
    Text(
        text = decryptionOutput,
        fontWeight = FontWeight.Bold
    )
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun CryptographyUi() {
    var input by remember {
        mutableStateOf("")
    }

    var encryptionIv by remember {
        mutableStateOf("")
    }

    var encryptionOutput by remember {
        mutableStateOf("")
    }

    var decryptionOutput by remember {
        mutableStateOf("")
    }

    val cryptography = Cryptography()

    Text("Algorithm:")
    Text(
        text = cryptography.transformation,
        fontWeight = FontWeight.Bold
    )

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp),
        value = input,
        onValueChange = { input = it },
        label = { Text("Input") }
    )

    Button(
        modifier = Modifier
            .padding(8.dp, 8.dp),
        onClick = {
            val encryptionOutputPair = cryptography.encrypt(input)
            encryptionIv = encryptionOutputPair.first
            encryptionOutput = encryptionOutputPair.second
        }
    ) { Text("Encrypt") }

    Text("IV:")
    Text(
        text = encryptionIv,
        fontWeight = FontWeight.Bold
    )

    Text("Encrypted output:")
    Text(
        text = encryptionOutput,
        fontWeight = FontWeight.Bold
    )

    Button(
        modifier = Modifier
            .padding(8.dp, 8.dp),
        onClick = {
            decryptionOutput = cryptography.decrypt(
                encryptionIv, encryptionOutput
            )
        }
    ) { Text("Decrypt") }

    Text("Decrypted:")
    Text(
        text = decryptionOutput,
        fontWeight = FontWeight.Bold
    )
}