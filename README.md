# 🛡️ Android Security

Repository to learn and play with Android security.

- Min. SDK set to 21
- UI built with Compose

## Content

Currently:
1. Lock screen required
2. Legacy: Cryptography AES - encrypt and decrypt file - Android M+ (23+)
3. Legacy: Cryptography AES - encrypt and decrypt text - Android M+ (23+)
4. Legacy: Cryptography RSA - encrypt and decrypt text - Android Lollipop+ (21+)
5. Jetpack Security (alpha): EncryptedSharedPreferences
6. Jetpack Security (alpha): Cryptography AES - encrypt and decrypt file
7. Block screen recording and screenshots

TODO:
1. Hash functions
2. Proguard
3. Safe API keys
4. Certificate pinning

## Materials

### Android
#### Artictles
- [Guide to Encryption & Decryption in Android (Keystore, Ciphers, and more)](https://www.youtube.com/watch?v=aaSck7jBDbw)
- [Secure data in Android](https://proandroiddev.com/secure-data-in-android-encryption-7eda33e68f58)
- [Encryption Tutorial For Android: Getting Started](https://www.kodeco.com/778533-encryption-tutorial-for-android-getting-started?page=2)
- [Google Help: Remediation for Unsafe Cryptographic Encryption](https://support.google.com/faqs/answer/9450925?hl=en)

#### Tools
- [Frida - Dynamic instrumentation toolkit for developers, reverse-engineers, and security researchers.](https://frida.re/)

### General
#### Courses
- [Google Cybersecurity Professional Certificate](https://www.coursera.org/professional-certificates/google-cybersecurity?)
- [An Introduction to Cryptography](https://www.coursera.org/learn/an-introduction-to-cryptography)

## FAQ

(Last updated 07.09.2023)

1. **What encryption to use with the Android version before Marshmellow (23)?**

Or Jetpack Security library (but it is in alpha with issues) or cryptography with RSA.

2. **In AES - does the Initialization Vector need to be kept secret?**

No. Source: https://support.google.com/faqs/answer/9450925?hl=en and https://stackoverflow.com/questions/9049789/aes-encryption-key-versus-iv

3. **In legacy cryptography - does an alias need to be kept secret?**

No. Source: https://stackoverflow.com/questions/49420586/how-to-safely-save-alias-key-of-android-keystore
