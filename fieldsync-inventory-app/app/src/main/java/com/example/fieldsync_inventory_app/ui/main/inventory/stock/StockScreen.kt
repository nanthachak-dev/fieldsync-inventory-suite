package com.example.fieldsync_inventory_app.ui.main.inventory.stock

import com.example.fieldsync_inventory_app.ui.main.inventory.model.InventorySort
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fieldsync_inventory_app.ui.common.components.BottomNavigationBar
import com.example.fieldsync_inventory_app.ui.common.components.SortIconButton
import com.example.fieldsync_inventory_app.ui.common.components.SubTopBar
import com.example.fieldsync_inventory_app.ui.common.components.TextInputSimple
import com.example.fieldsync_inventory_app.ui.common.navigation.Screen
import com.example.fieldsync_inventory_app.ui.main.inventory.components.VarietyCard
import com.example.fieldsync_inventory_app.ui.theme.RCRCSeedManagerTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StockScreen(viewModel: IStockViewModel,
                navController: NavController? = null) {

    val uiState by viewModel.inventoryUiState.collectAsState()
    val resourceUiState by viewModel.resourceUiState.collectAsState()
    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        resourceUiState.isLoading,
        { viewModel.refreshScreen() }
    )

    var searchQuery by remember { mutableStateOf(uiState.searchQuery) }
    var expanded by remember { mutableStateOf(false) }

    // Observe first launch state
    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch){
            viewModel.firstLaunch()
        }
    }

    Scaffold(
        topBar = {
            SubTopBar(
                title = "Stock Varieties",
                navController = navController
            )
        },
        bottomBar = {
            BottomNavigationBar(navController,2)
        },
        containerColor = Color(0xFF3D3C31)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            Column(Modifier
                .fillMaxSize()
            ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // -- Search Variety --
                TextInputSimple(
                    label = "Search Variety",
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.onSearchQueryChange(it)
                    },
                    modifier = Modifier.width(220.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                    },
                    containerColor = Color.White.copy(alpha = 0.08f),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(28.dp)
                )
                Spacer(modifier = Modifier.weight(1f)) // Spacer to push the sort button to the right

                // -- Sort Variety --
                Box {
                    SortIconButton(
                        onClick = { expanded = true },
                        tint = Color(0xFFFBC02D).copy(alpha = 0.8f),
                        containerColor = Color.White.copy(alpha = 0.08f),
                        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp))
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = Color(0xFF4A493E),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.width(180.dp)
                    ) {
                        Text(
                            "Sort By",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
                        
                        SortMenuItem("None", InventorySort.NONE, uiState.currentSort) {
                            viewModel.onSortChange(InventorySort.NONE)
                            expanded = false
                        }
                        SortMenuItem("Total Stock", InventorySort.STOCK, uiState.currentSort) {
                            viewModel.onSortChange(InventorySort.STOCK)
                            expanded = false
                        }
                        SortMenuItem("Graded", InventorySort.GRADED, uiState.currentSort) {
                            viewModel.onSortChange(InventorySort.GRADED)
                            expanded = false
                        }
                        SortMenuItem("Ungraded", InventorySort.UNGRADED, uiState.currentSort) {
                            viewModel.onSortChange(InventorySort.UNGRADED)
                            expanded = false
                        }
                        SortMenuItem("Germinated", InventorySort.GERMINATED, uiState.currentSort) {
                            viewModel.onSortChange(InventorySort.GERMINATED)
                            expanded = false
                        }
                        SortMenuItem("Ungerminated", InventorySort.UNGERMINATED, uiState.currentSort) {
                            viewModel.onSortChange(InventorySort.UNGERMINATED)
                            expanded = false
                        }
                    }
                }
            }

            // -- List of varieties --
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                itemsIndexed(uiState.varieties) { index, variety ->
                    VarietyCard(
                        variety,
                        index % 2 != 0,
                        onClick = {
                            // Navigate to BatchScreen with varietyId as argument
                            navController?.navigate("${Screen.Batch.route}/${variety.varietyId}")
                        }
                    )
                }
            }
            }
            PullRefreshIndicator(
                refreshing = resourceUiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun SortMenuItem(
    label: String,
    sortValue: InventorySort,
    currentSort: InventorySort,
    onClick: () -> Unit
) {
    val isSelected = sortValue == currentSort
    val accentColor = Color(0xFFFBC02D)
    
    DropdownMenuItem(
        text = { 
            Text(
                label, 
                color = if (isSelected) accentColor else Color.White,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ) 
        },
        onClick = onClick,
        trailingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else null,
        colors = MenuDefaults.itemColors(
            textColor = Color.White,
        )
    )
}

// =============================== Preview ==============================

val previewViewModel = PreviewStockViewModel()

@Preview(showBackground = true)
@Composable
fun PreviewManageStockScreen() {
    RCRCSeedManagerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF4A443A)
        ) {
            StockScreen(previewViewModel)
        }
    }
}
