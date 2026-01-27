package com.example.travelapp.ui.theme.screen

import android.net.Uri
import android.net.http.SslCertificate.restoreState
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.travelapp.ui.theme.component.EmptyState
import com.example.travelapp.ui.theme.navigation.Screen
import java.text.Normalizer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelapp.ui.theme.viewmodel.FavoriteViewModel
import com.example.travelapp.ui.theme.viewmodel.HomeViewModel
import com.example.travelapp.ui.theme.viewmodel.HotelDetailViewModel
import com.google.gson.Gson
import com.example.travelapp.ui.theme.data.AppData

@Composable
fun Home(
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(),
) {
    val selectedLocation = homeViewModel.selectedLocation
    val apiService = RetrofitClient.instance
    var hotels by remember { mutableStateOf<List<HotelProperty>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    val favoriteViewModel: FavoriteViewModel = viewModel()

    // Context để hiện Toast thông báo
    val context = LocalContext.current

    // Danh sách các Tag
    val categories = listOf("Location", "Hotels", "Plane", "Food", "Adventure")
    var selectedCategory by remember { mutableStateOf("Location") }

    val locations = listOf(
        "Hồ Chí Minh, Việt Nam", "Hà Nội, Việt Nam", "Đà Nẵng, Việt Nam", "Nha Trang, Việt Nam"
    )
    val searchKeyword = AppData.searchDestination
    val allPopularDestinations = remember { getSampleDestinations() }
    val displayList = remember(searchKeyword) {
        if (searchKeyword.isNotEmpty()) {
            allPopularDestinations.filter { it.location.contains(searchKeyword, ignoreCase = true) }
        } else {
            allPopularDestinations
        }
    }
    // ================= API CALL =================
    LaunchedEffect(selectedLocation) {
        isLoading = true
        try {
            val response = apiService.searchHotels(
                query = buildHotelQuery(selectedLocation),
                checkInDate = "2026-03-10",
                checkOutDate = "2026-03-11",
                apiKey = "e5ca58b702415625737f21d18ee1b3c715d4023b43f23d606ec1949d2f715ee8"
            )

            // Gộp danh sách quảng cáo và danh sách thường
            val resultList = listOfNotNull(response.ads, response.properties).flatten()

            hotels = resultList

            // --- QUAN TRỌNG: Lưu dữ liệu thật vào AppData để màn hình khác dùng ---
            AppData.hotelList = resultList

        } catch (e: Exception) {
            hotels = emptyList()
            AppData.hotelList = emptyList()
        } finally {
            isLoading = false
        }
    }

    // ================= SEARCH FILTER =================
    val filteredHotels = if (searchQuery.isBlank()) hotels else {
        val query = searchQuery.lowercase().removeVietnameseAccents()
        hotels.filter { it.name?.lowercase()?.removeVietnameseAccents()?.contains(query) == true }
    }
    val isSearching = searchQuery.isNotBlank()

    // ================= POPULAR / RECOMMEND =================
    val popularHotels = hotels.filter { it.reviews != null }.sortedByDescending { it.reviews ?: 0 }.take(5)
    val recommendHotels = hotels.filter { it.extracted_price != null }.sortedBy { it.extracted_price!! }.take(5)

    // ================= UI =================
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // 1. Header chọn địa điểm
        item {
            HomeHeader(
                locations = locations,
                selectedLocation = selectedLocation,
                onLocationChange = { homeViewModel.updateLocation(it) }
            )
        }

        // 2. Thanh tìm kiếm
        item {
            SearchBar(query = searchQuery, onQueryChange = { searchQuery = it })
        }

        // 3. TAGS CATEGORY (Đã sửa logic click)
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                categories.forEach { category ->
                    val isSelected = category == selectedCategory
                    val backgroundColor = if (isSelected) Color(0xFFE0F7FA) else Color(0xFFF5F5F5)
                    val textColor = if (isSelected) Color(0xFF00B6F0) else Color.Gray

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(backgroundColor)
                            .clickable {
                                // XỬ LÝ CHUYỂN TRANG
                                when (category) {
                                    "Location" -> selectedCategory = "Location"

                                    "Hotels" -> {
                                        // Chuyển sang màn hình AllHotels
                                        navController.navigate(Screen.AllHotels.route)
                                    }

                                    "Plane" -> {
                                        navController.navigate(Screen.FlightBooking.route)
                                    }

                                    "Food", "Adventure" -> {
                                        // Hiện thông báo
                                        Toast.makeText(context, "Tính năng đang chờ cập nhật!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            color = textColor,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // 4. Kết quả tìm kiếm hoặc Popular/Recommend
        if (isSearching) {
            item {
                if (!isLoading && filteredHotels.isEmpty()) EmptyState("No hotels found for \"$searchQuery\"")
            }
            item {
                if (!isLoading && filteredHotels.isNotEmpty()) {
                    HotelHorizontalList(hotels = filteredHotels, navController = navController, favoriteViewModel = favoriteViewModel)
                }
            }
        } else {
            // Popular
            item { SectionTitle(title = "Popular", onSeeAll = {}) }
            item {
                if (isLoading) LoadingRow()
                else if (popularHotels.isEmpty()) EmptyState("No popular hotels")
                else HotelHorizontalList(hotels = popularHotels, navController = navController, favoriteViewModel = favoriteViewModel)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Recommend
            item { SectionTitle(title = "Recommend", onSeeAll = {}) }
            item {
                if (isLoading) LoadingRow()
                else if (recommendHotels.isEmpty()) EmptyState("No recommendations")
                else HotelHorizontalList(hotels = recommendHotels, navController = navController, favoriteViewModel = favoriteViewModel)
            }
        }
    }
}

@Composable
fun HomeHeader(
    locations: List<String>,
    selectedLocation: String,
    onLocationChange: (String) -> Unit
) {
    Column {
        Text("Explore", color = Color.Gray, fontSize = 14.sp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = selectedLocation.substringBefore(","),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            LocationSelector(
                locations = locations,
                selectedLocation = selectedLocation,
                onLocationSelected = onLocationChange
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search hotel name") },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(30.dp),
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun CategoryRow() {
    val categories = listOf("Location", "Hotels", "Plane", "Adventure")

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
    navController: NavHostController,
    favoriteViewModel: FavoriteViewModel
) { // Mở ngoặc hàm

    val favoriteList by favoriteViewModel.favoriteHotels.collectAsState()

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = hotels) { hotel ->

            // Dòng này bây giờ sẽ hết lỗi vì favoriteList đã được khai báo ở trên
            val isFav = favoriteList.any { it.name == hotel.name }

            // Trong HotelHorizontalList
            HotelCard(
                hotel = hotel,
                isFavorite = isFav,
                onFavoriteClick = { selectedHotel ->
                    favoriteViewModel.toggleFavorite(selectedHotel)
                },
                onClick = {
                    AppData.currentHotel = hotel // Lưu khách sạn hiện tại vào biến chung
                    // ---------------------

                    navController.navigate(Screen.HotelDetail.route)
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


@Composable
fun BottomNavigationBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavigationBar(containerColor = Color.White) {

        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Screen.Home.route,
            onClick = { navigateTo(Screen.Home.route) }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite") },
            label = { Text("Favorite") },
            selected = currentRoute == Screen.Favorite.route,
            onClick = { navigateTo(Screen.Favorite.route) }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.trip),
                    contentDescription = "Trip"
                )
            },
            label = { Text("Trip") },
            selected = currentRoute == Screen.Trip.route,
            onClick = { navigateTo(Screen.Trip.route) }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.voucher),
                    contentDescription = "Voucher"
                )
            },
            label = { Text("Voucher") },
            selected = currentRoute == Screen.Voucher.route,
            onClick = { navigateTo(Screen.Voucher.route) }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == Screen.Profile.route,
            onClick = { navigateTo(Screen.Profile.route) }
        )
    }
}

@Composable
fun LocationSelector(
    locations: List<String>,
    selectedLocation: String,
    onLocationSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    AssistChip(
        onClick = { expanded = true },
        label = {
            Text(selectedLocation)
        },
        leadingIcon = {
            Icon(Icons.Default.LocationOn, contentDescription = null)
        }
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        locations.forEach { location ->
            DropdownMenuItem(
                text = { Text(location) },
                onClick = {
                    onLocationSelected(location)
                    expanded = false
                }
            )
        }
    }
}
fun buildHotelQuery(location: String): String {
    return when {
        location.contains("Hồ Chí Minh") ->
            "Hotels in Ho Chi Minh City"

        location.contains("Hà Nội") ->
            "Hotels in Hanoi"

        location.contains("Đà Nẵng") ->
            "Hotels in Da Nang"

        location.contains("Nha Trang") ->
            "Hotels in Nha Trang"

        else ->
            "Hotels in Vietnam"
    }
}

fun String.removeVietnameseAccents(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
}
// Định nghĩa kiểu dữ liệu mẫu
data class PopularDestination(val location: String, val imageRes: Int)

// Hàm lấy dữ liệu mẫu
fun getSampleDestinations(): List<PopularDestination> {
    return listOf(
        PopularDestination("Hà Nội", 0),
        PopularDestination("Hồ Chí Minh", 0),
        PopularDestination("Đà Nẵng", 0)
    )
}
