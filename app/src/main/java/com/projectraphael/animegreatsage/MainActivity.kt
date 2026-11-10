package com.projectraphael.animegreatsage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.launch

private val android.content.Context.profileStore by preferencesDataStore(name = "guest_profile")
private val NAME_KEY = stringPreferencesKey("display_name")
private val NICK_KEY = stringPreferencesKey("nickname")
private val TOUR_KEY = stringPreferencesKey("tour_seen")

private val Navy = Color(0xFF071426)
private val Panel = Color(0xFF10243D)
private val Cyan = Color(0xFF62D9FF)
private val Pale = Color(0xFFEAF7FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Cyan,
                    secondary = Color(0xFF9C8CFF),
                    background = Navy,
                    surface = Panel,
                    onPrimary = Navy,
                    onBackground = Pale,
                    onSurface = Pale
                )
            ) {
                GreatSageApp()
            }
        }
    }
}

@Composable
private fun GreatSageApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs by context.profileStore.data.collectAsState(initial = null)
    val savedName = prefs?.get(NAME_KEY).orEmpty()
    val savedNick = prefs?.get(NICK_KEY).orEmpty()
    val tourSeen = prefs?.get(TOUR_KEY) == "yes"
    var page by remember { mutableStateOf("Home") }
    var showTour by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize().background(Navy)) {
        when {
            prefs == null -> LoadingScreen()
            savedName.isBlank() -> GuestSetupScreen { name, nick ->
                scope.launch {
                    context.profileStore.edit {
                        it[NAME_KEY] = name
                        it[NICK_KEY] = nick
                    }
                    showTour = true
                }
            }
            showTour || !tourSeen -> WelcomeTour(
                onFinish = {
                    scope.launch { context.profileStore.edit { it[TOUR_KEY] = "yes" } }
                    showTour = false
                },
                onSkip = {
                    scope.launch { context.profileStore.edit { it[TOUR_KEY] = "yes" } }
                    showTour = false
                }
            )
            else -> MainShell(
                displayName = savedNick.ifBlank { savedName },
                page = page,
                onPage = { page = it },
                onReplayTour = { showTour = true }
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Cyan)
    }
}

@Composable
private fun Orb(modifier: Modifier = Modifier, size: androidx.compose.ui.unit.Dp = 112.dp) {
    Box(
        modifier.size(size).clip(CircleShape).background(
            Brush.radialGradient(
                colors = listOf(Color(0xFFE3FCFF), Color(0xFF58D8FF), Color(0xFF2368C9), Color(0x00112655))
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.size(size * .68f).clip(CircleShape).background(
            Brush.radialGradient(listOf(Color(0xAAFFFFFF), Color(0x6656DFFF), Color.Transparent))
        ))
        Box(Modifier.size(size * .34f).clip(CircleShape).background(Color(0xAAE9FFFF)))
    }
}

@Composable
private fun GuestSetupScreen(onContinue: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Orb()
        Spacer(Modifier.height(20.dp))
        Text("Welcome to Anime Great Sage", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Pale)
        Spacer(Modifier.height(8.dp))
        Text("Meet Raphael, your Great Sage companion.", color = Pale.copy(alpha = .75f))
        Spacer(Modifier.height(26.dp))
        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Your name (optional)") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = nickname, onValueChange = { nickname = it },
            label = { Text("Nickname (optional)") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Text("Guest access does not require an account. Avoid entering sensitive personal information.",
            color = Pale.copy(alpha = .65f), fontSize = 12.sp)
        Spacer(Modifier.height(22.dp))
        Button(
            onClick = { onContinue(name.trim().ifBlank { "Guest" }, nickname.trim()) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Continue as guest") }
    }
}

@Composable
private fun WelcomeTour(onFinish: () -> Unit, onSkip: () -> Unit) {
    val pages = listOf(
        "Meet Raphael" to "Raphael is your conversational AI companion. Ask questions naturally and explore at your own pace.",
        "Explore Great Sage" to "Discover anime, manga and manhwa information, watch orders, characters, races, magic, skills and story lore.",
        "Find your next story" to "Ask for recommendations by title, genre, theme or a story you describe. Ask follow-up questions for closer matches.",
        "Your privacy and controls" to "Guest access is available. Account connections should remain optional. Review permissions before sharing any information."
    )
    var index by remember { mutableIntStateOf(0) }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Orb(size = 104.dp)
        Spacer(Modifier.height(24.dp))
        Text("A quick beginner tour", color = Cyan, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(14.dp))
        Text(pages[index].first, color = Pale, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text(pages[index].second, color = Pale.copy(alpha = .78f), fontSize = 16.sp)
        Spacer(Modifier.height(26.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            pages.indices.forEach { i ->
                Box(Modifier.width(if (i == index) 26.dp else 8.dp).height(8.dp)
                    .clip(CircleShape).background(if (i == index) Cyan else Pale.copy(alpha = .25f)))
            }
        }
        Spacer(Modifier.height(26.dp))
        Button(onClick = { if (index == pages.lastIndex) onFinish() else index++ },
            modifier = Modifier.fillMaxWidth()) {
            Text(if (index == pages.lastIndex) "Finish tour" else "Next")
        }
        TextButton(onClick = onSkip) { Text("Skip tutorial") }
    }
}

@Composable
private fun MainShell(
    displayName: String,
    page: String,
    onPage: (String) -> Unit,
    onReplayTour: () -> Unit
) {
    Scaffold(
        containerColor = Navy,
        bottomBar = {
            NavigationBar(containerColor = Panel) {
                listOf("Home", "Explore", "Profile").forEach { item ->
                    NavigationBarItem(
                        selected = page == item,
                        onClick = { onPage(item) },
                        icon = {
                            Icon(when (item) {
                                "Home" -> Icons.Default.Home
                                "Explore" -> Icons.Default.Search
                                else -> Icons.Default.Person
                            }, contentDescription = item)
                        },
                        label = { Text(item) }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            when (page) {
                "Home" -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("ANIME GREAT SAGE", color = Cyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Hello, $displayName", color = Pale, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        }
                        Orb(size = 64.dp)
                    }
                    Spacer(Modifier.height(24.dp))
                    Text("Your Great Sage awaits.", color = Pale, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    Text("This v1.1 starter gives you the app foundation. Connect real AI and verified content services during development.",
                        color = Pale.copy(alpha = .7f))
                    Spacer(Modifier.height(18.dp))
                    FeatureCard("Talk with Raphael", "AI conversation interface — service connection pending", Icons.Default.AutoAwesome)
                    FeatureCard("Great Sage Archive", "Anime, manga, manhwa, races, magic, skills and lore — knowledge backend pending", Icons.Default.Book)
                    FeatureCard("Discover anime", "Search by title, category, theme or story — catalog integration pending", Icons.Default.Search)
                    FeatureCard("Voice & avatar", "Voice changer and avatar customization — implementation pending", Icons.Default.AutoAwesome)
                }
                "Explore" -> {
                    Text("Explore Great Sage", color = Pale, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Search and discovery screens are planned here. The live catalog and source integrations are not connected yet.",
                        color = Pale.copy(alpha = .7f))
                    Spacer(Modifier.height(16.dp))
                    listOf("Anime", "Manga & Manhwa", "Races & Species", "Magic, Skills & Powers", "Timelines & Watch Orders").forEach {
                        FeatureCard(it, "Knowledge module planned for a later implementation stage", Icons.Default.Book)
                    }
                }
                else -> {
                    Text("Your guest profile", color = Pale, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text("Signed in as a local guest: $displayName", color = Pale.copy(alpha = .75f))
                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(onClick = onReplayTour) { Text("Replay beginner tour") }
                    Text("This starter stores the guest display name and tutorial status on this device. It does not create a cloud account.",
                        color = Pale.copy(alpha = .65f), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Cyan, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, color = Pale, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(3.dp))
                Text(detail, color = Pale.copy(alpha = .65f), fontSize = 12.sp)
            }
        }
    }
}
