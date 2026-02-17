package com.example.fieldsync_inventory_app.ui.common.navigation

/**
 * A sealed class to define the navigation routes for better type safety.
 */
sealed class Screen(val route: String, val title: String? = null) {
    object Login : Screen("login_screen", "Login")
    object Dashboard : Screen("dashboard_screen", "Dashboard")
    object Management : Screen("management_screen", "Management")
    object Inventory : Screen("inventory_screen", "Inventory")
    object History : Screen("history_screen", "History")

    // Adjust Stock
    object AdjustStock : Screen("adjust_stock_screen", "Adjust Stock")
    object SelectSeedBatch : Screen("select_seed_batch_screen", "Select Seed Batch")

    // -- Inventory Routes --
    // Stock
    object Stock : Screen("stock_screen", "Stock")
    // Batch Screen (Sub-screen of Stock)
    object Batch : Screen("batch_screen", "Batch")

    // Stock In
    object StockIn : Screen("stock_in_screen", "Stock In")
    object AddSeedBatchStockIn: Screen("add_seed_batch_stock_in_screen", "Add Seed Batch")

    // Stock Out
    object StockOut : Screen("stock_out_screen", "Stock Out")
    object AddSeedBatchStockOut : Screen("add_seed_batch_stock_out_screen", "Add Seed Batch")


    // Report
    object SelectReport : Screen("select_report_screen", "Select Report")
    object StockReport : Screen("stock_report_screen", "Stock Report")
    object SaleReport : Screen("sale_report_screen", "Sale Report")


    // Sub-screens of Login Screen
    object Passcode : Screen("passcode_screen", "Passcode")

    // Top menu
    object SyncDatabase : Screen("sync_database_screen", "Sync Database")
    object UserAccount : Screen("user_account_screen", "User Account")


    // Entity Management
    object ManageEntity : Screen("manage_entity_screen", "Manage Entity")
    object RiceVariety : Screen("rice_variety_screen", "Rice Variety")
    object RiceVarietyForm : Screen("rice_variety_form_screen", "Rice Variety Form")
    object CustomerType : Screen("customer_type_screen", "Customer Type")
    object CustomerTypeForm : Screen("customer_type_form_screen", "Customer Type Form")
    object Customer : Screen("customer_screen", "Customer")
    object CustomerForm : Screen("customer_form_screen", "Customer Form")
    object SupplierType : Screen("supplier_type_screen", "Supplier Type")
    object SupplierTypeForm : Screen("supplier_type_form_screen", "Supplier Type Form")
    object Supplier : Screen("supplier_screen", "Supplier")
    object SupplierForm : Screen("supplier_form_screen", "Supplier Form")
    object User : Screen("user_screen", "User")
    object UserForm : Screen("user_form_screen", "User Form")
}
