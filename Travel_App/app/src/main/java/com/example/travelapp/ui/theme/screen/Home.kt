package com.example.travelapp.ui.theme.screen

import android.net.http.SslCertificate.restoreState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelapp.R
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.travelapp.ui.theme.api.HotelProperty
import com.example.travelapp.ui.theme.api.RetrofitClient

@Composable
fun Home(navController: NavHostController) {

    val apiService = RetrofitClient.instance
    var hotels by remember { mutableStateOf<List<HotelProperty>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = apiService.getHotels(
                query = "Khách sạn tại Hồ Chí Minh",
                checkIn = "2026-03-10",
                checkOut = "2026-03-11",
                apiKey = "YOUR_API_KEY"
            )
            hotels = response.ads ?: emptyList()
        } catch (e: Exception) {
            hotels = emptyList()
        } finally {
            isLoading = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item { HomeHeader() }

        item { SearchBar() }

        item { CategoryRow() }

        item {
            SectionTitle(
                title = "Popular",
                onSeeAll = {}
            )
        }

        item {
            if (isLoading) {
                LoadingRow()
            } else {
                HotelHorizontalList(
                    hotels = hotels,
                    navController = navController
                )
            }
        }

        item {
            SectionTitle(
                title = "Recommend",
                onSeeAll = {}
            )
        }

        item {
            HotelHorizontalList(
                hotels = hotels,
                navController = navController
            )
        }
    }
}

@Composable
fun HomeHeader() {
    Column {
        Text("Explore", color = Color.Gray, fontSize = 14.sp)

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Hồ Chí Minh",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(8.dp))

            AssistChip(
                onClick = {},
                label = { Text("Hồ Chí Minh, Việt Nam") }
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}
@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Find thinking to do") },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(30.dp),
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        }
    )

    Spacer(modifier = Modifier.height(16.dp))
}
@Composable
fun CategoryRow() {
    val categories = listOf("Location", "Hotels", "Plane", "Food", "Adventure")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) {
            AssistChip(
                onClick = {},
                label = { Text(it) }
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
}
@Composable
fun SectionTitle(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(
            "See all",
            color = Color(0xFF2F80ED),
            modifier = Modifier.clickable { onSeeAll() }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
}
@Composable
fun HotelHorizontalList(
    hotels: List<HotelProperty>,
    navController: NavHostController
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(hotels) { hotel ->
            HotelCard(
                hotel = hotel,
                onClick = {
                    navController.navigate("hotel_detail")
                }
            )
        }
    }
}

@Composable
fun LoadingRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

// Component thanh điều hướng dưới cùng
@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = Color.White) {

        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Saved") },
            label = { Text("Saved") },
            selected = currentRoute == "saved",
            onClick = {
                navController.navigate("saved") {
                    launchSingleTop = true
                }
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.trip),
                    contentDescription = "trip"
                )
            },
            label = { Text("Trip") },
            selected = currentRoute == "trip",
            onClick = {
                navController.navigate("trip") {
                    launchSingleTop = true
                }
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.voucher),
                    contentDescription = "Voucher"
                )
            },
            label = { Text("Voucher") },
            selected = currentRoute == "voucher",
            onClick = {
                navController.navigate("voucher") {
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.favorite),
                    contentDescription = "favorite"
                )
            },
            label = { Text("Favorite") },
            selected = currentRoute == "favorite",
            onClick = {
                navController.navigate("favorite") {
                    launchSingleTop = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == "profile",
            onClick = {
                navController.navigate("profile") {
                    launchSingleTop = true
                }
            }
        )
    }
}
