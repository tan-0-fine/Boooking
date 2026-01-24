package com.example.travelapp.ui.theme.navigation
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.travelapp.ui.theme.screen.Home
import com.example.travelapp.ui.theme.screen.Login
import com.example.travelapp.ui.theme.screen.Profile
import com.example.travelapp.ui.theme.screen.Favorite
import com.example.travelapp.ui.theme.screen.HotelDetail
import com.example.travelapp.ui.theme.screen.Signup
import com.example.travelapp.ui.theme.screen.Trip
import com.example.travelapp.ui.theme.screen.Voucher
import com.example.travelapp.ui.theme.screen.Wellcome
import com.example.travelapp.ui.theme.viewmodel.HotelDetailViewModel

sealed class Screen(val route: String){
    object Wellcome: Screen("wellcome")
    object Login: Screen("login")
    object Signup: Screen("signup")
    object Home: Screen("home")
    object HotelDetail : Screen("hotel_detail")

    object Favorite : Screen("favorite")
    object Trip : Screen("trip")
    object Voucher : Screen("voucher")
    object Profile : Screen("profile")
}

@Composable
fun NavGraph(navController: NavHostController){
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
            Favorite(navController)
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
        composable(Screen.HotelDetail.route) {
            val hotelDetailViewModel: HotelDetailViewModel = viewModel()
            HotelDetail(
                navController = navController,
                viewModel = hotelDetailViewModel
            )
        }

    }
}