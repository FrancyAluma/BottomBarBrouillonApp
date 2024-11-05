package com.example.bottombarbrouillon

import com.example.bottombarbrouillon.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bottombarbrouillon.Pagedessaye.EssayeScreen
import com.example.bottombarbrouillon.ui.theme.BottomBarBrouillonTheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BottomBarBrouillonTheme {
              EssayeScreen()
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen() {
    val pagerState = rememberPagerState()  // Accompanist Pager State
    var selectedIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isSnackbarVisible by remember { mutableStateOf(false) }

    // Synchroniser l'index sélectionné avec l'état du pager
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedIndex = page
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onItemClick = { index ->
                    scope.launch {
                        pagerState.animateScrollToPage(index)  // Synchronisation avec la navigation inférieure
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedIndex == 0) {  // Afficher le FAB uniquement sur la page d'accueil
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            val currentSnackbar = snackbarHostState.currentSnackbarData
                            if (currentSnackbar != null) {
                                currentSnackbar.dismiss()
                            } else {
                                snackbarHostState.showSnackbar(
                                    message = "Go Live",
                                    duration = SnackbarDuration.Indefinite
                                )
                            }
                        }
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.livebutton7),
                        contentDescription = "Go Live",
                        modifier = Modifier.size(24.dp)  // Taille du bouton
                    )
                }
            }
        },
        content = { paddingValues ->

            // Apply yellow background only when selectedIndex is 0 (HomeScreen)
            val backgroundColor = if (selectedIndex == 0) Color.White else Color.Transparent

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)  // Conditional background
                    .padding(paddingValues)
            ) {

                HorizontalPager(
                    count = bottomNavItems.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) { page ->
                    when (page) {
                        0 -> HomeScreen()  // Afficher "Go Live" uniquement sur la page d'accueil
                        1 -> ChatsScreen()
                        2 -> MemoriesScreen()
                        3 -> ContactsScreen()
                    }
                }

                // Snackbar Host pour afficher "Go Live"
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-32).dp, y = (-72).dp)  // Positionné au-dessus du FAB
                        .widthIn(min = 100.dp, max = 200.dp)
                        .padding(8.dp)
                ) {
                    Snackbar(
                        modifier = Modifier.wrapContentSize(),
                        action = {
                            // Action optionnelle dans le Snackbar
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Go Live",
                                fontSize = 20.sp,  // Taille du texte
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemClick: (Int) -> Unit) {
    NavigationBar (
        containerColor = Color.Cyan
    ) {
        bottomNavItems.forEachIndexed { index, bottomNavItem ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onItemClick(index) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (bottomNavItem.badges != 0) {
                                Badge(
                                    containerColor = when (bottomNavItem.title) {
                                        "Home" -> Color.Gray
                                        "Chats" -> Color(0xFF50C878)  // Vert émeraude
                                        "Memories" -> Color(0xFFFF7518)  // Citrouille
                                        "Contacts" -> Color(0xFF5D8AA8)  // Bleu Airforce
                                        else -> Color.Transparent
                                    }
                                ) {
                                    Text(text = bottomNavItem.badges.toString(), color = Color.White)
                                }
                            } else if (bottomNavItem.hasNews) {
                                Badge(
                                    containerColor = when (bottomNavItem.title) {
                                        "Home" -> Color.Gray
                                        "Chats" -> Color(0xFF50C878)
                                        "Memories" -> Color(0xFFFF7518)
                                        "Contacts" -> Color(0xFF5D8AA8)
                                        else -> Color.Transparent
                                    }
                                )
                            }
                        }
                    ) {
                        Image(
                            painter = painterResource(id = bottomNavItem.selectedIcon),
                            contentDescription = bottomNavItem.title,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = { Text(text = bottomNavItem.title) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = when (bottomNavItem.title) {
                        "Home" -> Color.Gray
                        "Chats" -> Color(0xFF50C878)
                        "Memories" -> Color(0xFFFF7518)
                        "Contacts" -> Color(0xFF5D8AA8)
                        else -> Color.Transparent
                    }
                )
            )
        }
    }
}

// Exemple de BottomNavItems
val bottomNavItems = listOf(
    BottomNavItem("Home", "home", R.drawable.home5, R.drawable.home5, false, 0),
    BottomNavItem("Chats", "chats", R.drawable.message3, R.drawable.message3, false, 5),
    BottomNavItem("Memories", "memories", R.drawable.cloud6, R.drawable.cloud6, true, 0),
    BottomNavItem("Contacts", "contacts", R.drawable.contact4, R.drawable.contact4, false, 3)
)

// Modèle de données pour BottomNavItem
data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val hasNews: Boolean,
    val badges: Int
)

// Exemple de pages (placeholders)
@Composable
fun HomeScreen() {

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){

        Row (modifier = Modifier.fillMaxWidth()
            .background(Color.Green)
            .height(150.dp)
        ) {

            Row (

                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Home",
                    fontWeight = FontWeight.Bold,
                    fontSize = 65.sp,
                    modifier = Modifier
                        .padding(10.dp)

                )

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription ="Plusinfos",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(35.dp)

                    )
                }
            }

        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )  {

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "CentChat",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 55.sp,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Image(
                    painter = painterResource(id = R.drawable.copyright5),
                    contentDescription = "right reserved",
                    modifier = Modifier
                        .size(20.dp)
                        .offset(y = 9.dp)

                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 50.dp),
                horizontalArrangement = Arrangement.Center
            ){

                Text(

                    text = "Connect",
                    fontSize = 20.sp,
                    color = Color(4280669030),
                    fontWeight = FontWeight.Bold,

                    )

                Text(

                    text = ".",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )

                Text(

                    text = "Organize",
                    fontSize = 20.sp,
                    color = Color(4294925312),
                    fontWeight = FontWeight.Bold,

                    )

                Text(

                    text = ".",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black


                )

                Text(

                    text = "Gift",
                    fontSize = 20.sp,
                    color = Color(4279793650),
                    fontWeight = FontWeight.Bold,

                    )

            }

        }
    }

}

@Composable
fun ChatsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Chats Screen")
    }
}

@Composable
fun MemoriesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Memories Screen")
    }
}

@Composable
fun ContactsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Contacts Screen")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BottomBarBrouillonTheme {
        MainScreen()
    }
}
