package com.example.musicappui.ui.theme

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicappui.MainViewModel
import com.example.musicappui.R
import com.example.musicappui.Screen
import kotlinx.coroutines.launch
import com.example.musicappui.screenInDrawer
import com.example.musicappui.screensInBottom


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(modifier: Modifier = Modifier) {

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val isSheetFullScreen by remember { mutableStateOf(false) }
    val modifier = if(isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()

    // Allow us to find out on which screen we are
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val dialogOpen = remember {
        mutableStateOf(false)
    }

    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    val title = remember {
        mutableStateOf(currentScreen.title)
    }

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = {it != ModalBottomSheetValue.HalfExpanded}
    )

    val roundedCornerRadius = if(isSheetFullScreen) 0.dp else 16.dp

    val bottomBar: @Composable () -> Unit = {
        if(currentScreen is Screen.DrawerScreens || currentScreen == Screen.BottomScreen.Home) {
            BottomNavigation(Modifier.wrapContentSize(), backgroundColor = Color.Magenta) {
                screensInBottom.forEach{
                        item ->
                    val isSelected = currentRoute == item.bRoute
                    Log.d("Navigation", "Item: ${item.bTitle}, Current Route: $currentRoute, Is Selected: $isSelected")
                    val tint = if(isSelected)Color.White else Color.Black
                    BottomNavigationItem(selected = currentRoute == item.bRoute,
                        onClick = { controller.navigate(item.bRoute)
                            title.value = item.bTitle
                        }, icon = {

                            Icon(tint= tint,
                                contentDescription = item.bTitle, painter= painterResource(id = item.icon))
                        },
                        label = { Text(text = item.bTitle, color = tint )}
                        , selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black

                    )
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
        sheetContent = {
        MoreBottomSheet(modifier = modifier)
    }) {
        Scaffold(
            bottomBar = bottomBar,
            topBar = {
                TopAppBar(
                    title = { Text(title.value) },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                if (modalSheetState.isVisible) {
                                    modalSheetState.hide()
                                } else {
                                    modalSheetState.show()
                                }
                            }
                          }
                        ){
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        })
                        {
                            Icon(Icons.Filled.AccountCircle, contentDescription = "Menu")
                        }
                    },
                )
            },
            scaffoldState = scaffoldState,
            drawerContent = {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 20.dp, start = 10.dp)
                        .background(Color.LightGray)
                ) {
                    items(screenInDrawer) { item ->
                        val itemScope = this@items // Get LazyItemScope in the items function
                        DrawerItem(
                            selected = currentRoute == item.dRoute,
                            item = item
                        ) {
                            scope.launch {
                                scaffoldState.drawerState.close()
//                            controller.navigate(item.dRoute)
                            }
                            if (item.dRoute == "add_account") {
                                dialogOpen.value = true
                            } else {
                                controller.navigate(item.dRoute)
                                title.value = item.dTitle
                            }
                        }
                    }
                }
            },
            modifier = Modifier.padding(top = 14.dp),

            ) {

            Navigation(navController = controller, viewModel = viewModel, pd = it)

            AccountDialog(dialogOpen = dialogOpen)
        }
    }

}

@Composable
fun DrawerItem(
    selected: Boolean,
    item: Screen.DrawerScreens,
    onDrawItemClicked: () -> Unit,
) {
    val myBackground = if (selected) Color.DarkGray else Color.White
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(myBackground)
            .clickable {
                onDrawItemClicked()
            }) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.dTitle,
            Modifier.padding(end = 8.dp)
        )
        Text(
            text = item.dTitle,
            style = MaterialTheme.typography.h5
        )
    }

}

@Composable
fun MoreBottomSheet(modifier: Modifier){
    Box(Modifier.fillMaxWidth().height(300.dp).background(Color.Magenta)){
        Column(modifier = modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = modifier.padding(8.dp),) {
                Icon(painter = painterResource(id = R.drawable.baseline_settings_24), contentDescription = "Settings")
                Text(text = "Settings", fontSize = 20.sp, color = Color.White)
            }
            Row(modifier = modifier.padding(8.dp),) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_share_24), contentDescription = "Settings")
                Text(text = "Share", fontSize = 20.sp, color = Color.White)
            }
            Row(modifier = modifier.padding(8.dp),) {
                Icon(painter = painterResource(id = R.drawable.ic_help_green), contentDescription = "Settings")
                Text(text = "Help", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, pd: PaddingValues) {

    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.DrawerScreens.Account.route,
        modifier = Modifier.padding(pd)
    ) {
//        composable(Screen.DrawerScreens.AddAccount.route){
//            Text(text = "Add Account")
//        }
        composable(Screen.DrawerScreens.Account.route) {
            AccountView()
        }
        composable(Screen.DrawerScreens.Subscription.route) {
            Subscription()
        }
        composable(Screen.BottomScreen.Home.bRoute){
            Home()
        }
        composable(Screen.BottomScreen.Browse.bRoute){
            Browse()
        }

        composable(Screen.BottomScreen.Library.bRoute){
            Library()
        }
    }
}