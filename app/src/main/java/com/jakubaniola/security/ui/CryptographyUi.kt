@file:OptIn(ExperimentalMaterial3Api::class)

package com.jakubaniola.security.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jakubaniola.security.utils.cryptography.legacy.CryptographyAes
import com.jakubaniola.security.utils.cryptography.legacy.CryptographyAesFile
import com.jakubaniola.security.utils.cryptography.jetpacksecurity.CryptographyJetpackSecurityFile
import com.jakubaniola.security.utils.cryptography.jetpacksecurity.EncryptedLocalDatabase
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
    var isCryptographyByFileJetpackSecuritySelected by remember {
        mutableStateOf(false)
    }
    var isEncryptedSharedPreferences by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Type:",
            fontWeight = FontWeight.Bold
        )

        RadioTextButton(
            text = "Legacy: Cryptography File with AES",
            selected = isCryptographyByFileSelected,
        ) {
            isCryptographyByFileSelected = true
            isCryptographySelected = false
            isCryptographyByFileJetpackSecuritySelected = false
            isEncryptedSharedPreferences = false
        }

        RadioTextButton(
            text = "Legacy: Cryptography Text with AES",
            selected = isCryptographySelected,
        ) {
            isCryptographyByFileSelected = false
            isCryptographySelected = true
            isCryptographyByFileJetpackSecuritySelected = false
            isEncryptedSharedPreferences = false
        }

        RadioTextButton(
            text = "Jetpack Security: Cryptography File with AES",
            selected = isCryptographyByFileJetpackSecuritySelected,
        ) {
            isCryptographyByFileSelected = false
            isCryptographySelected = false
            isCryptographyByFileJetpackSecuritySelected = true
            isEncryptedSharedPreferences = false
        }
        RadioTextButton(
            text = "Jetpack Security: EncryptedSharedPreferences with AES",
            selected = isEncryptedSharedPreferences,
        ) {
            isCryptographyByFileSelected = false
            isCryptographySelected = false
            isCryptographyByFileJetpackSecuritySelected = false
            isEncryptedSharedPreferences = true
        }

        when {
            isCryptographyByFileSelected -> CryptographyAesFileUi(fileDir)
            isCryptographySelected -> CryptographyAesUi()
            isCryptographyByFileJetpackSecuritySelected -> CryptographyJetpackSecurityFileUi(fileDir)
            isEncryptedSharedPreferences -> EncryptedSharedPreferencesUi()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun CryptographyAesFileUi(fileDir: File) {
    var input by remember {
        mutableStateOf("")
    }

    var encryptionOutput by remember {
        mutableStateOf("")
    }

    var decryptionOutput by remember {
        mutableStateOf("")
    }

    val cryptographyFile = CryptographyAesFile(fileDir)

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
fun CryptographyAesUi() {
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

    val cryptography = CryptographyAes()

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

@Composable
fun CryptographyJetpackSecurityFileUi(fileDir: File) {
    var input by remember {
        mutableStateOf("")
    }

    var encryptionOutput by remember {
        mutableStateOf("")
    }

    var decryptionOutput by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val cryptography = CryptographyJetpackSecurityFile(context, fileDir)

    Text("Algorithm:")
    Text(
        text = cryptography.transformation.toString(),
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
            encryptionOutput = cryptography.encrypt(input)
        }
    ) { Text("Encrypt to file") }

    Text("Encrypted output (byte array):")
    Text(
        text = encryptionOutput,
        fontWeight = FontWeight.Bold
    )

    Button(
        modifier = Modifier
            .padding(8.dp, 8.dp),
        onClick = {
            decryptionOutput = cryptography.decrypt()
        }
    ) { Text("Decrypt from file") }

    Text("Decrypted from file output:")
    Text(
        text = decryptionOutput,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun EncryptedSharedPreferencesUi() {
    var inputKey by remember {
        mutableStateOf("")
    }
    var inputValue by remember {
        mutableStateOf("")
    }

    var output by remember {
        mutableStateOf("")
    }
    var encryptedFile by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val localDatabase = EncryptedLocalDatabase(context)

    Text("Algorithm:")
    Text(
        text = localDatabase.transformation.toString(),
        fontWeight = FontWeight.Bold
    )

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp),
        value = inputKey,
        onValueChange = { inputKey = it },
        label = { Text("Input KEY") }
    )

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp),
        value = inputValue,
        onValueChange = { inputValue = it },
        label = { Text("Input VALUE") }
    )

    Button(
        modifier = Modifier
            .padding(8.dp, 8.dp),
        onClick = {
            localDatabase.write(inputKey, inputValue)
            output = localDatabase.read()
            encryptedFile = localDatabase.readEncryptedFile()
        }
    ) { Text("Save to encrypted database") }

    Button(
        modifier = Modifier
            .padding(8.dp, 8.dp),
        onClick = {
            localDatabase.clearDatabase()
        }
    ) { Text("Clear database") }

    Text("All values in database:")
    Text(
        text = output,
        fontWeight = FontWeight.Bold
    )

    Text("All encrypted values in database:")
    Text(
        text = encryptedFile,
        fontWeight = FontWeight.Bold
    )
}