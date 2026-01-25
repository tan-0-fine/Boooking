package com.example.travelapp.ui.theme.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.travelapp.ui.theme.api.HotelProperty
import com.example.travelapp.ui.theme.screen.Home
import com.example.travelapp.ui.theme.screen.Login
import com.example.travelapp.ui.theme.screen.Profile
import com.example.travelapp.ui.theme.screen.Favorite
import com.example.travelapp.ui.theme.screen.HotelDetail
import com.example.travelapp.ui.theme.screen.Signup
import com.example.travelapp.ui.theme.screen.Trip
import com.example.travelapp.ui.theme.screen.Voucher
import com.example.travelapp.ui.theme.screen.Wellcome
import com.example.travelapp.ui.theme.viewmodel.FavoriteViewModel
import com.example.travelapp.ui.theme.viewmodel.HotelDetailViewModel
import com.google.gson.Gson

sealed class Screen(val route: String){
    object Wellcome: Screen("wellcome")
    object Login: Screen("login")
    object Signup: Screen("signup")
    object Home: Screen("home")
    object HotelDetail : Screen("hotel_detail/{hotel}") {
        fun createRoute(hotel: String) = "hotel_detail/$hotel"
    }
    object Favorite : Screen("favorite")
    object Trip : Screen("trip")
    object Voucher : Screen("voucher")
    object Profile : Screen("profile")
}

@Composable
fun NavGraph(navController: NavHostController){
    val favoriteViewModel: FavoriteViewModel = viewModel()
    NavHost(navController = navController, startDestination = Screen.Wellcome.route){
        composable(Screen.Wellcome.route){
            Wellcome(navController)
        }
        composable(Screen.Login.route){
            Login(navController)
        }
        composable(Screen.Signup.route){
            Signup(navController)
        }
        composable(Screen.Home.route){
            Home(navController)
        }
        composable(Screen.Favorite.route) {
            Favorite(navController, favoriteViewModel)
        }

        composable(Screen.Trip.route) {
            Trip(navController)
        }

        composable(Screen.Voucher.route) {
            Voucher(navController)
        }

        composable(Screen.Profile.route) {
            Profile(navController)
        }
        composable(
            route = Screen.HotelDetail.route,
            // ... (phần arguments giữ nguyên)
        ) { backStackEntry ->
            // Lấy thông tin hotel từ tham số gửi sang (giữ nguyên code cũ của bạn)
            val hotelJson = backStackEntry.arguments?.getString("hotel")
            val hotel = Gson().fromJson(hotelJson, HotelProperty::class.java)

            // 4. XỬ LÝ CHO MÀN HÌNH CHI TIẾT (Sửa lỗi dòng 77)
            // Lấy danh sách yêu thích hiện tại để xem khách sạn này có được tim chưa
            val favoriteList by favoriteViewModel.favoriteHotels.collectAsState()
            val isFav = favoriteList.any { it.name == hotel.name } // Kiểm tra xem có trong list không

            HotelDetail(
                navController = navController,
                hotel = hotel,
                isFavorite = isFav, // Truyền trạng thái tim (Đỏ hay Trắng)
                onFavoriteClick = {
                    favoriteViewModel.toggleFavorite(it) // Gọi hàm xử lý khi bấm tim
                }
            )
        }
    }
}