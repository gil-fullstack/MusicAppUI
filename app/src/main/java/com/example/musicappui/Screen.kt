package com.example.musicappui

import androidx.annotation.DrawableRes

sealed class Screen(val title: String, val route: String) {
    sealed class DrawerScreens(val dTitle: String, val dRoute: String, @DrawableRes val icon: Int)
        : Screen(dTitle, dRoute){
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


    }

}

val screenInDrawer = listOf(
    Screen.DrawerScreens.Account,
    Screen.DrawerScreens.Subscription,
    Screen.DrawerScreens.AddAccount
)