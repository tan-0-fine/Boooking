package com.example.travelapp.ui.theme.navigation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.travelapp.ui.screens.Trip
import com.example.travelapp.ui.screens.TripItemCard
import com.example.travelapp.ui.theme.data.AppData
import com.example.travelapp.ui.theme.screen.AllHotels
import com.example.travelapp.ui.theme.screen.BookingSuccessScreen
import com.example.travelapp.ui.theme.screen.EditProfile
import com.example.travelapp.ui.theme.screen.Favorite
import com.example.travelapp.ui.theme.screen.FlightBookingScreen
import com.example.travelapp.ui.theme.screen.FlightResult
import com.example.travelapp.ui.theme.screen.Home
import com.example.travelapp.ui.theme.screen.HotelDetail
import com.example.travelapp.ui.theme.screen.Login
import com.example.travelapp.ui.theme.screen.PassengerInfo
import com.example.travelapp.ui.theme.screen.PaymentScreen
import com.example.travelapp.ui.theme.screen.Profile
import com.example.travelapp.ui.theme.screen.RoomSelection
import com.example.travelapp.ui.theme.screen.SeatSelection
import com.example.travelapp.ui.theme.screen.Signup
import com.example.travelapp.ui.theme.screen.Voucher
import com.example.travelapp.ui.theme.screen.Wellcome
import com.example.travelapp.ui.theme.viewmodel.FavoriteViewModel

// --- PHẦN 1: ĐỊNH NGHĨA SCREEN (Sửa lại cho gọn) ---
sealed class Screen(val route: String) {
    object Wellcome : Screen("wellcome")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")

    // Sửa dòng này: Bỏ tham số /{hotel} đi vì ta dùng AppData rồi
    object HotelDetail : Screen("hotel_detail_screen")

    object RoomSelection : Screen("room_selection_screen")
    object Payment : Screen("payment_screen")
    object Favorite : Screen("favorite")
    object Trip : Screen("trip")
    object Voucher : Screen("voucher")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object AllHotels : Screen("all_hotels")
    object FlightBooking : Screen("flight_booking")
}

// --- PHẦN 2: NAVGRAPH ---
@Composable
fun NavGraph(navController: NavHostController) {
    val favoriteViewModel: FavoriteViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Wellcome.route) {

        composable(Screen.Wellcome.route) {
            Wellcome(navController)
        }

        composable(Screen.Login.route) {
            Login(navController)
        }

        composable(Screen.Signup.route) {
            Signup(navController)
        }

        composable(Screen.Home.route) {
            Home(navController)
        }
        composable(Screen.AllHotels.route) {
            AllHotels(navController)
        }

        composable(Screen.Favorite.route) {
            Favorite(navController, favoriteViewModel)
        }

        composable(Screen.Trip.route) {
            Trip(navController)
        }

        composable(Screen.HotelDetail.route) {}

        composable(Screen.Voucher.route) {
            Voucher(navController)
        }

        composable(Screen.Profile.route) {
            Profile(navController)
        }
        composable(Screen.EditProfile.route) {
            EditProfile(navController)
        }

            // --- KHỐI HOTEL DETAIL (Đã đóng ngoặc đúng chỗ) ---
        composable(route = Screen.HotelDetail.route) {
            // 1. Lấy khách sạn từ AppData
            val hotel = AppData.currentHotel

            // 2. Chỉ hiện màn hình khi có dữ liệu
            if (hotel != null) {
                HotelDetail(
                    navController = navController,
                    viewModel = favoriteViewModel,
                    hotel = hotel
                )
            }
        }

        composable(Screen.RoomSelection.route) {
            RoomSelection(navController = navController)
        }

        composable(Screen.Payment.route) {
            PaymentScreen(navController = navController)
        }
        composable(Screen.FlightBooking.route) {
            FlightBookingScreen(navController)
        }
        composable("flight_result") {
            FlightResult(navController)
        }
        composable("passenger_info") { PassengerInfo(navController) }
        composable("seat_selection") { SeatSelection(navController) }
        composable("booking_success") {
            BookingSuccessScreen(navController)
        }
    }
}