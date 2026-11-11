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

    Box(
        Modifier
            .fillMaxSize()
            .background(Navy)
    ) {
        when {
            prefs == null -> LoadingScreen()

            savedName.isBlank() -> {
                GuestSetupScreen { name ->
                    scope.launch {
                        context.profileStore.edit {
                            it[NAME_KEY] = name
                        }
                        showTour = true
                    }
                }
            }

            showTour || !tourSeen -> {
                WelcomeTour(
                    onFinish = {
                        scope.launch {
                            context.profileStore.edit {
                                it[TOUR_KEY] = "yes"
                            }
                        }
                        showTour = false
                    },
                    onSkip = {
                        scope.launch {
                            context.profileStore.edit {
                                it[TOUR_KEY] = "yes"
                            }
                        }
                        showTour = false
                    }
                )
            }

            else -> {
                MainShell(
                    displayName = savedNick.ifBlank { savedName },
                    savedName = savedName,
                    savedNickname = savedNick,
                    page = page,
                    onPage = { page = it },
                    onReplayTour = { showTour = true },
                    onSaveNickname = { nickname, onResult ->
                        scope.launch {
                            try {
                                context.profileStore.edit {
                                    it[NICK_KEY] = nickname
                                }
                                onResult(true)
                            } catch (_: Exception) {
                                onResult(false)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Cyan)
    }
}

@Composable
private fun Orb(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 112.dp
) {
    Box(
        modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE3FCFF),
                        Color(0xFF58D8FF),
                        Color(0xFF2368C9),
                        Color(0x00112655)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .size(size * .68f)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(0xAAFFFFFF),
                            Color(0x6656DFFF),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            Modifier
                .size(size * .34f)
                .clip(CircleShape)
                .background(Color(0xAAE9FFFF))
        )
    }
}

@Composable
private fun GuestSetupScreen(
    onContinue: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Orb()
        Spacer(Modifier.height(20.dp))

        Text(
            "Welcome to Anime Great Sage",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Pale
        )

        Spacer(Modifier.height(8.dp))
        Text(
            "Meet Raphael, your Great Sage companion.",
            color = Pale.copy(alpha = .75f)
        )

        Spacer(Modifier.height(26.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your name (optional)") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))
        Text(
            "Guest access does not require an account. Avoid entering sensitive personal information.",
            color = Pale.copy(alpha = .65f),
            fontSize = 12.sp
        )

        Spacer(Modifier.height(22.dp))
        Button(
            onClick = {
                onContinue(name.trim().ifBlank { "Guest" })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue as guest")
        }
    }
}

@Composable
private fun WelcomeTour(
    onFinish: () -> Unit,
    onSkip: () -> Unit
) {
    val pages = listOf(
        "Meet Raphael" to
            "Raphael is your conversational AI companion. Ask questions naturally and explore at your own pace.",
        "Explore Great Sage" to
            "Discover anime, manga and manhwa information, watch orders, characters, races, magic, skills and story lore.",
        "Find your next story" to
            "Ask for recommendations by title, genre, theme or a story you describe. Ask follow-up questions for closer matches.",
        "Your privacy and controls" to
            "Guest access is available. Account connections should remain optional. Review permissions before sharing any information."
    )

    var index by remember { mutableIntStateOf(0) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Orb(size = 104.dp)
        Spacer(Modifier.height(24.dp))

        Text(
            "A quick beginner tour",
            color = Cyan,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(14.dp))
        Text(
            pages[index].first,
            color = Pale,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))
        Text(
            pages[index].second,
            color = Pale.copy(alpha = .78f),
            fontSize = 16.sp
        )

        Spacer(Modifier.height(26.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            pages.indices.forEach { i ->
                Box(
                    Modifier
                        .width(if (i == index) 26.dp else 8.dp)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(
                            if (i == index) Cyan
                            else Pale.copy(alpha = .25f)
                        )
                )
            }
        }

        Spacer(Modifier.height(26.dp))
        Button(
            onClick = {
                if (index == pages.lastIndex) onFinish() else index++
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (index == pages.lastIndex) "Finish tour" else "Next")
        }

        TextButton(onClick = onSkip) {
            Text("Skip tutorial")
        }
    }
}

@Composable
private fun MainShell(
    displayName: String,
    savedName: String,
    savedNickname: String,
    page: String,
    onPage: (String) -> Unit,
    onReplayTour: () -> Unit,
    onSaveNickname: (String, (Boolean) -> Unit) -> Unit
) {
    var nickname by remember(savedNickname) { mutableStateOf(savedNickname) }
    var nicknameSaving by remember { mutableStateOf(false) }
    var nicknameSaved by remember(savedNickname) { mutableStateOf(false) }
    var nicknameError by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Navy,
        bottomBar = {
            NavigationBar(containerColor = Panel) {
                listOf("Home", "Explore", "Profile").forEach { item ->
                    NavigationBarItem(
                        selected = page == item,
                        onClick = { onPage(item) },
                        icon = {
                            Icon(
                                when (item) {
                                    "Home" -> Icons.Default.Home
                                    "Explore" -> Icons.Default.Search
                                    else -> Icons.Default.Person
                                },
                                contentDescription = item
                            )
                        },
                        label = { Text(item) }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            when (page) {
                "Home" -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                "ANIME GREAT SAGE",
                                color = Cyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                "Hello, $displayName",
                                color = Pale,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Orb(size = 64.dp)
                    }

                    Spacer(Modifier.height(24.dp))
                    Text(
                        "Your Great Sage awaits.",
                        color = Pale,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "This v1.1 starter gives you the app foundation. Connect real AI and verified content services during development.",
                        color = Pale.copy(alpha = .7f)
                    )

                    Spacer(Modifier.height(18.dp))
                    FeatureCard(
                        "Talk with Raphael",
                        "Hybrid AI foundation added; inference provider integration pending",
                        Icons.Default.AutoAwesome
                    )
                    FeatureCard(
                        "Master Knowledge Repository",
                        "Anime, games, study, cultures, cooking, energy systems and general knowledge",
                        Icons.Default.Book
                    )
                    FeatureCard(
                        "Anime Timeline Navigator",
                        "Release order, story chronology and spoiler-aware recommended watch order",
                        Icons.Default.Book
                    )
                    FeatureCard(
                        "Game Journey Navigator",
                        "Game release order, story order, campaigns, DLC and completion guides",
                        Icons.Default.Book
                    )
                    FeatureCard(
                        "Progress Tracker",
                        "Local data model foundation for anime episodes and game missions",
                        Icons.Default.Download
                    )
                    FeatureCard(
                        "Voice & avatar",
                        "Voice conversion provider interface; live call integration pending",
                        Icons.Default.AutoAwesome
                    )
                }

                "Explore" -> {
                    Text(
                        "Explore Great Sage",
                        color = Pale,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Search and discovery screens are planned here. The live catalog and source integrations are not connected yet.",
                        color = Pale.copy(alpha = .7f)
                    )
                    Spacer(Modifier.height(16.dp))

                    listOf(
                        "Anime & Action Anime",
                        "Manga, Manhwa, Manhua & Light Novels",
                        "Anime Timeline Navigator",
                        "Game Journey Navigator",
                        "Anime & Game Progress Tracker",
                        "Races & Species",
                        "Magic, Skills & Powers",
                        "Chakra, Ki, Aura & Spiritual Energy",
                        "Cooking & Anime Cuisine",
                        "Study Knowledge",
                        "World Cultures",
                        "General Knowledge",
                        "Upcoming Releases"
                    ).forEach {
                        FeatureCard(
                            it,
                            "Knowledge schema and navigation foundation; verified content integration pending",
                            Icons.Default.Book
                        )
                    }
                }

                else -> {
                    Text(
                        "Profile & Settings",
                        color = Pale,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Your local guest profile and app preferences.",
                        color = Pale.copy(alpha = .75f)
                    )
                    Spacer(Modifier.height(22.dp))
                    Text(
                        "Settings",
                        color = Cyan,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Panel),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "General",
                                color = Pale,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Guest account name: $savedName",
                                color = Pale.copy(alpha = .75f),
                                fontSize = 13.sp
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "Nickname (optional)",
                                color = Pale,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(8.dp))

                            OutlinedTextField(
                                value = nickname,
                                onValueChange = {
                                    nickname = it
                                    nicknameSaved = false
                                    nicknameError = false
                                },
                                label = { Text("Your nickname") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    nicknameSaving = true
                                    nicknameSaved = false
                                    nicknameError = false
                                    onSaveNickname(nickname.trim()) { success ->
                                        nicknameSaving = false
                                        nicknameSaved = success
                                        nicknameError = !success
                                    }
                                },
                                enabled = !nicknameSaving,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (nicknameSaving) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text("Saving...")
                                } else {
                                    Text("Save nickname")
                                }
                            }

                            if (nicknameSaved) {
                                Text(
                                    "Nickname saved.",
                                    color = Cyan,
                                    fontSize = 13.sp
                                )
                            }

                            if (nicknameError) {
                                Text(
                                    "Could not save nickname. Please try again.",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = onReplayTour,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Replay welcome tour")
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Cyan,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    color = Pale,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    description,
                    color = Pale.copy(alpha = .7f),
                    fontSize = 13.sp
                )
            }
        }
    }
}
