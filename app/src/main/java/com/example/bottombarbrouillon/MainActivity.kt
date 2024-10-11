package com.example.bottombarbrouillon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bottombarbrouillon.ui.theme.BottomBarBrouillonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BottomBarBrouillonTheme {

                var francy by remember {
                    mutableIntStateOf(0)
                }
                Scaffold(

                    bottomBar = {

                        NavigationBar {
                            bottomNavItems.forEachIndexed { index, bottomNavItem ->
                                NavigationBarItem(
                                    selected = index == francy,
                                    onClick = {
                                        francy = index
                                    },
                                    icon = {

                                        BadgedBox(
                                            badge = {
                                                if (bottomNavItem.badges != 0) {
                                                    Badge (

                                                        containerColor = when (bottomNavItem.title) {
                                                            "Home" -> androidx.compose.ui.graphics.Color.Gray
                                                            "Contacts" -> androidx.compose.ui.graphics.Color(0xFF5D8AA8)  // Airforce Blue (RAF)
                                                            "Chats" -> androidx.compose.ui.graphics.Color(0xFF50C878)  // Emerald Green
                                                            "Memories" -> androidx.compose.ui.graphics.Color(0xFFFF7518)  // Pumpkin
                                                            else -> androidx.compose.ui.graphics.Color.Transparent
                                                        }

                                                    ) {
                                                        Text(
                                                            text = bottomNavItem.badges.toString(),
                                                            color = androidx.compose.ui.graphics.Color.White // Optional: make text white for better contrast
                                                        )
                                                    }
                                                } else if (bottomNavItem.hasNews) {
                                                    Badge(

                                                        containerColor = when (bottomNavItem.title) {
                                                            "Home" -> androidx.compose.ui.graphics.Color.Gray
                                                            "Contacts" -> androidx.compose.ui.graphics.Color(0xFF5D8AA8)  // Airforce Blue (RAF)
                                                            "Chats" -> androidx.compose.ui.graphics.Color(0xFF50C878)  // Emerald Green
                                                            "Memories" -> androidx.compose.ui.graphics.Color(0xFFFF7518)  // Pumpkin
                                                            else -> androidx.compose.ui.graphics.Color.Transparent
                                                        }
                                                    )
                                                }
                                            }
                                        ) {
                                            Image(
                                                painter = if (index == francy) painterResource(id = bottomNavItem.selectedIcon)
                                                else {
                                                    painterResource(id = bottomNavItem.unselectedIcon)
                                                },
                                                contentDescription = bottomNavItem.title,
                                                modifier = Modifier.size(24.dp)  // Taille de l'icône
                                            )
                                        }
                                    },
                                    label = {
                                        Text(text = bottomNavItem.title)
                                    },
                                    colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                        indicatorColor = when (bottomNavItem.title) {
                                            "Home" -> androidx.compose.ui.graphics.Color.Gray
                                            "Contacts" -> androidx.compose.ui.graphics.Color(0xFF5D8AA8)  // Airforce Blue (RAF)
                                            "Chats" -> androidx.compose.ui.graphics.Color(0xFF50C878)  // Emerald Green
                                            "Memories" -> androidx.compose.ui.graphics.Color(0xFFFF7518)  // Pumpkin
                                            else -> androidx.compose.ui.graphics.Color.Transparent
                                        }
                                    )
                                )

                            }
                        }
                    },
                    floatingActionButton = {

                        FloatingActionButton(
                            onClick = { /*TODO*/ },
                        ) {}
                    }
                ) {

                    val padding = it
                }
            }
        }
    }
}





val bottomNavItems = listOf(

    BottomNavItem(
        title = "Home",
        route = "home",
        selectedIcon =R.drawable.home5,
        unselectedIcon = R.drawable.home5,
        hasNews = false,
        badges = 0
    ),
    BottomNavItem(

        title = "Contacts",
        route = "contacts",
        selectedIcon = R.drawable.contact4 ,
        unselectedIcon = R.drawable.contact4,
        hasNews = false,
        badges = 3
    ),
    BottomNavItem(

        title = "Chats",
        route = "chats",
        selectedIcon = R.drawable.message3,
        unselectedIcon = R.drawable.message3,
        hasNews = false,
        badges = 5
    ),
    BottomNavItem(

        title = "Memories",
        route = "memories",
        selectedIcon = R.drawable.cloud6,
        unselectedIcon = R.drawable.cloud6  ,
        hasNews = true,
        badges = 0
    )
)
data class BottomNavItem(

    val title: String,
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val hasNews: Boolean,
    val badges: Int

)
