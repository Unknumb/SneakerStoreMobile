package com.example.appsneakerstore.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appsneakerstore.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppWithSideDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    authViewModel: AuthViewModel,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    val authState = authViewModel.uiState.collectAsState()
    val currentUser = authState.value.currentUser

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // User info section
                if (currentUser != null) {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = {
                            Text(
                                text = if (currentUser.isGuest) "Invitado" else currentUser.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        selected = false,
                        onClick = { /* Ver perfil */ }
                    )
                    if (!currentUser.isGuest) {
                        Text(
                            text = currentUser.email,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Divider()
                
                Text(
                    text = "Catálogo",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                
                NavigationDrawerItem(
                    label = { Text("Zapatillas deportivas") },
                    selected = false,
                    onClick = { /* acción */ }
                )
                NavigationDrawerItem(
                    label = { Text("Zapatillas casuales") },
                    selected = false,
                    onClick = {  /* acción */ }
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Divider()
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Logout, contentDescription = null) },
                    label = { Text("Cerrar sesión") },
                    selected = false,
                    onClick = onLogout
                )
                
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        content = content
    )
}
