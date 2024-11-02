package com.example.musicappui

import androidx.annotation.DrawableRes

sealed class Screen(val title: String, val route: String) {
    sealed class BottomScreen(
        val bTitle: String,
        val bRoute: String,
        @DrawableRes    val icon: Int
    ):Screen(bTitle, bRoute)
    {
            object Home : BottomScreen("Home", "home", R.drawable.baseline_home_24)
            object Library : BottomScreen("Library", "library", R.drawable.baseline_library_add_24)
            object Browse : BottomScreen("Browse", "browse", R.drawable.baseline_apps_24)
    }

    sealed class DrawerScreens(val dTitle: String, val dRoute: String, @DrawableRes val icon: Int) :
        Screen(dTitle, dRoute) {

        object Account : DrawerScreens(
            "Account",
            "account",
            R.drawable.ic_account
        )

        object Subscription : DrawerScreens(
            "Subscription",
            "Subscription",
            R.drawable.ic_subscribe
        )

        object AddAccount : DrawerScreens(
            "Add Account",
            "add_account",
            R.drawable.baseline_person_add_alt_1_24
        )
        object HomeAccount : DrawerScreens(
            "Home page",
            "home_account",
            R.drawable.baseline_home_24
        )


    }

}

val screensInBottom = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Library,
    Screen.BottomScreen.Browse
)

val screenInDrawer = listOf(
    Screen.DrawerScreens.Account,
    Screen.DrawerScreens.Subscription,
    Screen.DrawerScreens.AddAccount,
    Screen.DrawerScreens.HomeAccount
)