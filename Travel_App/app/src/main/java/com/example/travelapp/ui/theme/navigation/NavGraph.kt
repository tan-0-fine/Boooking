package com.example.travelapp.ui.theme.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.travelapp.ui.theme.screen.Favourite
import com.example.travelapp.ui.theme.screen.Home
import com.example.travelapp.ui.theme.screen.Login
import com.example.travelapp.ui.theme.screen.Profile
import com.example.travelapp.ui.theme.screen.Saved
import com.example.travelapp.ui.theme.screen.Signup
import com.example.travelapp.ui.theme.screen.Trip
import com.example.travelapp.ui.theme.screen.Voucher
import com.example.travelapp.ui.theme.screen.Wellcome
sealed class Screen(val route: String){
    object Wellcome: Screen("wellcome")
    object Login: Screen("login")
    object Signup: Screen("signup")
    object Home: Screen("home")
    object HotelDetail : Screen("hotel_detail")

    object Saved : Screen("saved")
    object Trip : Screen("trip")
    object Voucher : Screen("voucher")
    object Favourite : Screen("favorite")
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
        composable(Screen.Saved.route) {
            Saved(navController)
        }

        composable(Screen.Trip.route) {
            Trip(navController)
        }

        composable(Screen.Voucher.route) {
            Voucher(navController)
        }
        composable(Screen.Favourite.route) {
            Favourite(navController)
        }

        composable(Screen.Profile.route) {
            Profile(navController)
        }

    }
}