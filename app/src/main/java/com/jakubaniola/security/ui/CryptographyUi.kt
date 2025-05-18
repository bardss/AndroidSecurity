package com.jakubaniola.security.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jakubaniola.security.utils.cryptography.jetpacksecurity.CryptographyJetpackSecurityFile
import com.jakubaniola.security.utils.cryptography.jetpacksecurity.EncryptedLocalDatabase
import com.jakubaniola.security.utils.cryptography.legacy.CryptographyAes
import com.jakubaniola.security.utils.cryptography.legacy.CryptographyAesFile
import com.jakubaniola.security.utils.cryptography.legacy.CryptographyRsa
import java.io.File

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun CryptographyScreen(fileDir: File) {
    var isCryptographyAesByFileSelected by remember { mutableStateOf(true) }
    var isCryptographyAesSelected by remember { mutableStateOf(false) }
    var isCryptographyRsaSelected by remember { mutableStateOf(false) }
    var isCryptographyByFileJetpackSecuritySelected by remember { mutableStateOf(false) }
    var isEncryptedSharedPreferences by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Cryptography Demo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider()
            Text(
                text = "Type:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    RadioTextButton(
                        text = "Legacy: Cryptography File with AES",
                        selected = isCryptographyAesByFileSelected,
                    ) {
                        isCryptographyAesByFileSelected = true
                        isCryptographyAesSelected = false
                        isCryptographyRsaSelected = false
                        isCryptographyByFileJetpackSecuritySelected = false
                        isEncryptedSharedPreferences = false
                    }
                    RadioTextButton(
                        text = "Legacy: Cryptography Text with AES",
                        selected = isCryptographyAesSelected,
                    ) {
                        isCryptographyAesByFileSelected = false
                        isCryptographyAesSelected = true
                        isCryptographyRsaSelected = false
                        isCryptographyByFileJetpackSecuritySelected = false
                        isEncryptedSharedPreferences = false
                    }
                    RadioTextButton(
                        text = "Legacy: Cryptography Text with RSA",
                        selected = isCryptographyRsaSelected,
                    ) {
                        isCryptographyAesByFileSelected = false
                        isCryptographyAesSelected = false
                        isCryptographyRsaSelected = true
                        isCryptographyByFileJetpackSecuritySelected = false
                        isEncryptedSharedPreferences = false
                    }
                    RadioTextButton(
                        text = "Jetpack Security: Cryptography File with AES",
                        selected = isCryptographyByFileJetpackSecuritySelected,
                    ) {
                        isCryptographyAesByFileSelected = false
                        isCryptographyAesSelected = false
                        isCryptographyRsaSelected = false
                        isCryptographyByFileJetpackSecuritySelected = true
                        isEncryptedSharedPreferences = false
                    }
                    RadioTextButton(
                        text = "Jetpack Security: EncryptedSharedPreferences with AES",
                        selected = isEncryptedSharedPreferences,
                    ) {
                        isCryptographyAesByFileSelected = false
                        isCryptographyAesSelected = false
                        isCryptographyRsaSelected = false
                        isCryptographyByFileJetpackSecuritySelected = false
                        isEncryptedSharedPreferences = true
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    when {
                        isCryptographyAesByFileSelected -> CryptographyAesFileUi(fileDir)
                        isCryptographyAesSelected -> CryptographyAesUi()
                        isCryptographyRsaSelected -> CryptographyRsaUi()
                        isCryptographyByFileJetpackSecuritySelected -> CryptographyJetpackSecurityFileUi(fileDir)
                        isEncryptedSharedPreferences -> EncryptedSharedPreferencesUi()
                    }
                }
            }
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
fun CryptographyRsaUi() {
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
    val cryptography = CryptographyRsa(context)

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
            encryptionOutput = cryptography.encrypt(input)
        }
    ) { Text("Encrypt") }

    Text("Encrypted output:")
    Text(
        text = encryptionOutput,
        fontWeight = FontWeight.Bold
    )

    Button(
        modifier = Modifier
            .padding(8.dp, 8.dp),
        onClick = {
            decryptionOutput = cryptography.decrypt(encryptionOutput)
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

