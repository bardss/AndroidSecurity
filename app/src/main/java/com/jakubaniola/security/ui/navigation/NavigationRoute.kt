package com.jakubaniola.security.ui.navigation

import kotlinx.serialization.Serializable

interface NavigationRoute

@Serializable
object Home : NavigationRoute

@Serializable
object Cryptography : NavigationRoute
