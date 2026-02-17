package com.example.fieldsync_inventory_app.ui.common.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fieldsync_inventory_app.data.security.AuthEvent
import com.example.fieldsync_inventory_app.ui.auth.account_management.UserAccountScreen
import com.example.fieldsync_inventory_app.ui.auth.account_management.UserAccountViewModel
import com.example.fieldsync_inventory_app.ui.auth.login.LoginScreen
import com.example.fieldsync_inventory_app.ui.auth.login.LoginViewModel
import com.example.fieldsync_inventory_app.ui.auth.passcode.PasscodeScreen
import com.example.fieldsync_inventory_app.ui.auth.passcode.PasscodeViewModel
import com.example.fieldsync_inventory_app.ui.common.components.top_bar.TopBar
import com.example.fieldsync_inventory_app.ui.common.view_model.user_session.UserSessionViewModel
import com.example.fieldsync_inventory_app.ui.main.dashboard.DashboardScreen
import com.example.fieldsync_inventory_app.ui.main.dashboard.DashboardViewModel
import com.example.fieldsync_inventory_app.ui.main.history.HistoryScreen
import com.example.fieldsync_inventory_app.ui.main.history.HistoryViewModel
import com.example.fieldsync_inventory_app.ui.main.inventory.InventoryScreen
import com.example.fieldsync_inventory_app.ui.main.inventory.InventoryViewModel
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.VarietyBatchScreen
import com.example.fieldsync_inventory_app.ui.main.inventory.batch.VarietyBatchViewModel
import com.example.fieldsync_inventory_app.ui.main.inventory.stock.StockScreen
import com.example.fieldsync_inventory_app.ui.main.inventory.stock.StockViewModel
import com.example.fieldsync_inventory_app.ui.main.report.SelectReportScreen
import com.example.fieldsync_inventory_app.ui.main.report.sale_report.SaleReportScreen
import com.example.fieldsync_inventory_app.ui.main.report.sale_report.SaleReportViewModel
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.StockReportScreen
import com.example.fieldsync_inventory_app.ui.main.report.stock_report.StockReportViewModel
import com.example.fieldsync_inventory_app.ui.main.stock_management.ManageStockScreen
import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.AdjustStockScreen
import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.AdjustStockViewModel
import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch.AddSeedBatchAdjustStockViewModel
import com.example.fieldsync_inventory_app.ui.main.stock_management.adjust_stock.seed_batch.SelectSeedBatchScreen
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.StockInScreen
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.StockInViewModel
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_in.seed_batch.AddSeedBatchStockInScreen
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.StockOutScreen
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.StockOutViewModel
import com.example.fieldsync_inventory_app.ui.main.stock_management.stock_out.seed_batch.AddSeedBatchStockOutScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.ManageEntityScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer.CustomerFormScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer.CustomerScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer.CustomerViewModel
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type.CustomerTypeFormScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type.CustomerTypeScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.customer_type.CustomerTypeViewModel
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.RiceVarietyFormScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.RiceVarietyScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.rice_variety.RiceVarietyViewModel
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier.SupplierFormScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier.SupplierScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier.SupplierViewModel
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.SupplierTypeFormScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.SupplierTypeScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.supplier_type.SupplierTypeViewModel
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.UserFormScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.UserScreen
import com.example.fieldsync_inventory_app.ui.reference.entity_management.user.UserViewModel
import com.example.fieldsync_inventory_app.ui.reference.sync_database.SyncDatabaseScreen
import com.example.fieldsync_inventory_app.ui.reference.sync_database.SyncDatabaseViewModel

/**
 * How to register new screen in AppNavigation()
 * 1. Define view model parameter in Screen's constructor
 * 2. Register new route in Screen.kt
 * 3. Define view model object in AppNavigation() and instantiated by hiltViewModel() in AppNavigation()
 * 4. Call compose() in NavHost() and pass route
 * 5. In compose() block, call screen and pass view model parameter
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val syncDatabaseViewModel: SyncDatabaseViewModel = hiltViewModel()
    val selectSeedBatchViewModel: AddSeedBatchAdjustStockViewModel = hiltViewModel()
    val adjustStockViewModel: AdjustStockViewModel = hiltViewModel()
    val stockOutViewModel: StockOutViewModel = hiltViewModel()
    val stockInViewModel: StockInViewModel = hiltViewModel()
    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    // -- Inventory --
    val inventoryViewModel: InventoryViewModel = hiltViewModel()
    val stockViewModel: StockViewModel = hiltViewModel()
    val stockBatchViewModel: VarietyBatchViewModel = hiltViewModel()
    // -- History --
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val passcodeViewModel: PasscodeViewModel = hiltViewModel()
    val stockReportViewModel: StockReportViewModel = hiltViewModel()
    val saleReportViewModel: SaleReportViewModel = hiltViewModel()
    val riceVarietyViewModel: RiceVarietyViewModel = hiltViewModel()
    val customerTypeViewModel: CustomerTypeViewModel = hiltViewModel()
    val customerViewModel: CustomerViewModel = hiltViewModel()
    val supplierTypeViewModel: SupplierTypeViewModel = hiltViewModel()
    val supplierViewModel: SupplierViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    val userSessionViewModel: UserSessionViewModel = hiltViewModel()
    val userAccountViewModel: UserAccountViewModel = hiltViewModel()
    // Collect state OUTSIDE the composable scope
    val passcodeSettings by passcodeViewModel.passcodeSettings.collectAsState()
    
    // Handle global authentication events (e.g., token expired) 🔑
    LaunchedEffect(Unit) {
        userSessionViewModel.authEventBus.events.collect { event ->
            if (event is AuthEvent.Unauthorized) {
                Log.d("AppNavigation", "Unauthorized event received, jumping to Login")
                navController.navigate("${Screen.Login.route}?kickOut=true") {
                    // Clear backstack to prevent user from going back to protected screens
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(
            route = "${Screen.Login.route}?kickOut={kickOut}",
            arguments = listOf(navArgument("kickOut") { defaultValue = false; type = NavType.BoolType })
        ) { backStackEntry ->
            val kickOut = backStackEntry.arguments?.getBoolean("kickOut") ?: false
            val loginViewModel: LoginViewModel = hiltViewModel()

            LaunchedEffect(kickOut) {
                if (kickOut) {
                    loginViewModel.setErrorMessage("Session expired. Please login again.")
                }
            }

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    val hasPasscode = passcodeSettings.passcodeHash.isNotEmpty()
                    Log.d("AppNavigation", "hasPasscode: $hasPasscode")
                    if (hasPasscode) {
                        // User has passcode set, go to verification
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        // No passcode set, go to setup
                        navController.navigate(Screen.Passcode.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // Passcode screen
        composable(Screen.Passcode.route) {
            PasscodeScreen(passcodeViewModel, navController)
        }

        // Dashboard screen
        composable(Screen.Dashboard.route) {
            DashboardScreen(dashboardViewModel,
                { TopBar(screenTitle = "Dashboard", navController) }, navController)
        }

        // Manage stock screen
        composable(Screen.Management.route) {
            ManageStockScreen(
                { TopBar(screenTitle = "Management", navController) }, navController)
        }

        // -- Inventory --
        // Inventory screen
        composable(Screen.Inventory.route) {
            InventoryScreen(inventoryViewModel,
                { TopBar(screenTitle = "Inventory", navController) }, navController)
        }
        composable(Screen.Stock.route) {
            StockScreen(stockViewModel, navController)
        }
        // --------------------------

        // History screen
        composable(Screen.History.route) {
            HistoryScreen(historyViewModel,
                { TopBar(screenTitle = "History", navController) }, navController)
        }

        // -- Sub screens of Management screen --
        // Adjust Stock Screen: Manage Stock/
        composable(Screen.AdjustStock.route) {
            AdjustStockScreen(adjustStockViewModel, navController)
        }

        // Stock-In screen: Manage Stock/
        composable(Screen.StockIn.route) {
            StockInScreen(stockInViewModel, navController)
        }

        // Stock-Out screen: Manage Stock/
        composable(Screen.StockOut.route) {
            StockOutScreen(stockOutViewModel, navController)
        }

        // Select Seed Batch screen: Manage Stock/Adjust Stock
        composable(Screen.SelectSeedBatch.route) {
            SelectSeedBatchScreen(selectSeedBatchViewModel, navController)
        }

        // Add Seed Batch Stock Out screen: Manage Stock/Stock Out
        composable(Screen.AddSeedBatchStockOut.route) {
            AddSeedBatchStockOutScreen(stockOutViewModel, navController)
        }

        // Add Seed Batch Stock In screen: Manage Stock/Stock In
        composable(Screen.AddSeedBatchStockIn.route) {
            AddSeedBatchStockInScreen(stockInViewModel, navController)
        }

        // -- Report --
        // Select Report screen: Manage Stock/
        composable(Screen.SelectReport.route) {
            SelectReportScreen(navController)
        }
        // Stock Report screen: Manage Stock/Report/Select Report/Stock Report
        composable(Screen.StockReport.route) {
            StockReportScreen(stockReportViewModel, navController)
        }
        // Sale Report screen: Manage Stock/Report/Select Report/Sale Report
        composable(Screen.SaleReport.route) {
            SaleReportScreen(saleReportViewModel, navController)
        }

        // -- Sub screens of Inventory screen --
        // Batch screen: Inventory/Batch
        composable(
            route = "${Screen.Batch.route}/{varietyId}", // Receive varietyId from other screen
            arguments = listOf(navArgument("varietyId") { type = NavType.IntType })
        ) {
            val varietyId = it.arguments?.getInt("varietyId") ?: 0
            VarietyBatchScreen(stockBatchViewModel, navController, varietyId)
        }

        // -- Top menu --
        composable(Screen.SyncDatabase.route) {
            SyncDatabaseScreen(syncDatabaseViewModel, navController)
        }

        // User Account: TopNav/User Account
        composable(Screen.UserAccount.route) {
            UserAccountScreen(userSessionViewModel, passcodeViewModel,userAccountViewModel, navController)
        }

        // -- Entity Management --
        // Manage Entity: TopNav/Manage Entity
        composable(Screen.ManageEntity.route) {
            ManageEntityScreen(navController)
        }
        // Variety: TopNav/Manage Entity/Rice Variety
        composable(Screen.RiceVariety.route) {
            RiceVarietyScreen(riceVarietyViewModel,navController)
        }
        // Variety Form: TopNav/Manage Entity/Rice Variety (card item)/Form
        composable(Screen.RiceVarietyForm.route) {
            RiceVarietyFormScreen(riceVarietyViewModel,navController)
        }

        // Customer Type: TopNav/Manage Entity/Customer Type
        composable(Screen.CustomerType.route) {
            CustomerTypeScreen(customerTypeViewModel, navController)
        }
        // Customer Type Form: TopNav/Manage Entity/Customer Type (card item)/Form
        composable(Screen.CustomerTypeForm.route) {
            CustomerTypeFormScreen(customerTypeViewModel, navController)
        }

        // Customer: TopNav/Manage Entity/Customer
        composable(Screen.Customer.route) {
            CustomerScreen(customerViewModel, navController)
        }
        // Customer Form: TopNav/Manage Entity/Customer (card item)/Form
        composable(Screen.CustomerForm.route) {
            CustomerFormScreen(customerViewModel, navController)
        }

        // Supplier Type: TopNav/Manage Entity/Supplier Type
        composable(Screen.SupplierType.route) {
            SupplierTypeScreen(supplierTypeViewModel, navController)
        }
        // Supplier Type Form: TopNav/Manage Entity/Supplier Type (card item)/Form
        composable(Screen.SupplierTypeForm.route) {
            SupplierTypeFormScreen(supplierTypeViewModel, navController)
        }

        // Supplier: TopNav/Manage Entity/Supplier
        composable(Screen.Supplier.route) {
            SupplierScreen(supplierViewModel, navController)
        }
        // Supplier Form: TopNav/Manage Entity/Supplier (card item)/Form
        composable(Screen.SupplierForm.route) {
            SupplierFormScreen(supplierViewModel, navController)
        }

        // User: TopNav/Manage Entity/User
        composable(Screen.User.route) {
            UserScreen(userViewModel, navController)
        }
        // User Form: TopNav/Manage Entity/User (card item)/Form
        composable(Screen.UserForm.route) {
            UserFormScreen(userViewModel, navController)
        }
    }
}
