package com.jakubaniola.security

sealed class Screen {
    data object Home : Screen()
    data object Cryptography : Screen()
}