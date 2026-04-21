package com.example.tamtamduku

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tamtamduku.ui.theme.TAMTAMDUKUTheme
import model.*

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TAMTAMDUKUTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navCon = rememberNavController()
                    AppNavigation(navCon)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navCon: NavHostController) {
    NavHost(navCon, startDestination = "search") {
        composable("home") {
            HomeScreen(navCon)
        }
        composable("search") {
            SearchScreen(navCon)
        }
        composable("search/filterSearch") {
            FilterSearchScreen(navCon)
        }
    }
}

@Composable
fun FilterSearchScreen(nav: NavHostController) {
    var minGaji by remember { mutableStateOf("") }
    var maxGaji by remember { mutableStateOf("") }
    var namaQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Masukkan Nama: ")
            TextField(
                modifier = Modifier.weight(1f),
                value = namaQuery,
                onValueChange = { namaQuery = it },
                singleLine = true
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Min Gaji: ")
            TextField(
                modifier = Modifier.weight(1f),
                value = minGaji,
                onValueChange = { input ->
                    if (input.isEmpty() || input.toDoubleOrNull() != null || input == ".") {
                        minGaji = input
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Max Gaji: ")
            TextField(
                modifier = Modifier.weight(1f),
                value = maxGaji,
                onValueChange = { input ->
                    if (input.isEmpty() || input.toDoubleOrNull() != null || input == ".") {
                        maxGaji = input
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
        }
        
        Button(
            modifier = Modifier.padding(top = 16.dp).align(Alignment.End),
            onClick = { nav.popBackStack() }
        ) {
            Text("Terapkan Filter")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(navCon: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    val datas = NWGroup.NWG

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Write down....") },
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.fillMaxHeight(),
                onClick = { navCon.navigate("search/filterSearch") },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Filter")
            }
        }
        LazyColumn(modifier = Modifier.weight(1f).padding(16.dp).border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))) {
            val searched = datas.filter { it.nama.contains(searchText, ignoreCase = true) }
            items(searched) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
                ) {
                    Text(text = it.nama)
                    Text(text = it.deskripsi)
                    Text(text = it.lokasi)
                    Text(text = it.rating.toString())
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Row(modifier = Modifier
        .fillMaxHeight()
        .height(100.dp)) {
        Button(modifier = Modifier
            .width(100.dp)
            .aspectRatio(1f)
            .padding(10.dp), onClick = { navController.navigate("home") }) { Text("home") }
        Button(modifier = Modifier
            .width(100.dp)
            .aspectRatio(1f)
            .padding(10.dp), onClick = { navController.navigate("search") }) { Text("search") }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TAMTAMDUKUTheme {
        Greeting("Android")
    }
}
