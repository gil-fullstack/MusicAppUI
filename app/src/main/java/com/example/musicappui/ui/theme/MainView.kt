package com.example.musicappui.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicappui.MainViewModel
import com.example.musicappui.Screen
import kotlinx.coroutines.launch
import com.example.musicappui.screenInDrawer



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(modifier: Modifier = Modifier) {

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val viewModel:MainViewModel = viewModel()
    // Allow us to find out on which screen we are
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    val title = remember {
        mutableStateOf(currentScreen.title)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Home")},
                navigationIcon = { IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                })
                     {
                       Icon(Icons.Filled.Home, contentDescription = null)
                    }},
            )
        }, scaffoldState = scaffoldState,
        drawerContent = {
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                items(screenInDrawer) { item ->
                    val itemScope = this@items // Get LazyItemScope in the items function
                    DrawerItem(
                        selected = currentRoute == item.dRoute,
                        item = item
                    ){
                        scope.launch {
                            scaffoldState.drawerState.close()
//                            controller.navigate(item.dRoute)
                        }
                        if (currentRoute != "add_account") {
                            //
                        }else{
                            controller.navigate(item.dRoute)
                            title.value = item.dTitle
                        }
                    }
                }
            }
        }

    ) {
        Navigation(navController = controller, viewModel =  viewModel, pd =  it)
    }
}

@Composable
fun DrawerItem(
    selected: Boolean,
    item: Screen.DrawerScreens,
//    function: () -> Job,
    onDrawItemClicked: () -> Unit,
){
    val myBackground = if (selected) Color.DarkGray else Color.White
  Row(
      Modifier.fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 16.dp)
          .background(myBackground)
          .clickable {
              onDrawItemClicked()
          }){
      Icon(painter = painterResource(id = item.icon),
          contentDescription = item.dTitle,)
      Text(text = item.dTitle, style = MaterialTheme.typography.h5)
  }

}

@Composable
fun Navigation(navController: NavController, viewModel: MainViewModel, pd: PaddingValues) {

    NavHost(navController = navController as NavHostController, startDestination = Screen.DrawerScreens.AddAccount.route, modifier = Modifier.padding(pd)){
        composable(Screen.DrawerScreens.AddAccount.route){
            Text(text = "Add Account")
        }
        composable(Screen.DrawerScreens.Account.route){

        }
        composable(Screen.DrawerScreens.Subscription.route){

        }
    }
}