package com.example.berserklifecounter


import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.rememberNavController
import com.example.berserklifecounter.ui.theme.SoundManager
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.palette.graphics.Palette

interface OrientationChangeListener {
    fun onPlayer2HpChange(showReverse: Boolean)
}

// MainActivity (изменено)
class MainActivity : ComponentActivity(), OrientationChangeListener {
    private var currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                LifeCounterApp(this)
            }
        }
    }

    override fun onPlayer2HpChange(showReverse: Boolean) {
        val orientation =
            if (showReverse) ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Запрещаем анимацию (не гарантируется на всех устройствах)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        requestedOrientation = orientation

        // Установка флага обратно (необходимо для правильной работы других элементов UI)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}
enum class GameMode {
    STANDARD,
    SINGLE,
}

data class Theme(val name: String, val backgroundIds: List<Int>)



@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun LifeCounterApp(orientationChangeListener: OrientationChangeListener?) {
    val berserkGold = Color(0xFFB87333)
    val berserkBloodRed = Color(0xFF8B0000)
    val berserkDarkGrey = Color(0xFF2F2F2F)
    val berserkShadow = Color(0xFF000000)
    val player1Life = remember { mutableStateOf(25) }
    val player2Life = remember { mutableStateOf(25) }
    val gradientColors = listOf(Color(0xFF333333), Color(0xFF666666))

    val themes = remember {
        listOf(
            Theme(
                name = "Местность Коллекция 1.",
                backgroundIds =  listOf(
                    R.drawable.mesnost_stepi_1,
                    R.drawable.mesnost_forest_1,
                    R.drawable.mesnost_netral_1,
                    R.drawable.mesnost_dark_1,
                    R.drawable.mesnost_bloloto_1,
                    R.drawable.mesnost_gori_1,
                )
            ),
            Theme(
                name = "Местность Коллекция 2.",
                backgroundIds = listOf(
                    R.drawable.mesnost_stepi_2,
                    R.drawable.mesnost_forest_2,
                    R.drawable.mesnost_netral_2,
                    R.drawable.mesnost_dark_2,
                    R.drawable.mesnost_bololoto_2,
                    R.drawable.mesnost_gori_2,
                )
            ),
            Theme(
                name = "Местность Коллекция 3.",
                backgroundIds =  listOf(
                    R.drawable.mesnost_stepi_3,
                    R.drawable.mesnost_forest_3,
                    R.drawable.mesnost_neteral_3,
                    R.drawable.mesnost_dark_3,
                    R.drawable.mesnost_bololoto_3,
                    R.drawable.mesnost_gori_3,
                )
            ),
            Theme(
                name = "Дамы Коллекция 1.",
                backgroundIds = listOf(
                    R.drawable.dama_stepi_1,
                    R.drawable.dama_forest_1,
                    R.drawable.dama_netral_1,
                    R.drawable.dama_dark_1,
                    R.drawable.dama_boloto_1,
                    R.drawable.dama_gori_1,

                    )
            ),
            Theme(
                name = "Дамы Коллекция 2.",
                backgroundIds = listOf(
                    R.drawable.dama_stepi_2,
                    R.drawable.dama_forest_2,
                    R.drawable.dama_netral_2,
                    R.drawable.dama_dark_2,
                    R.drawable.dama_boloto_2,
                    R.drawable.dama_gori_2,
                )
            ),

            Theme(
                name = "Дамы Коллекция 3.",
                backgroundIds =  listOf(
                    R.drawable.dama_stepi_3,
                    R.drawable.dama_forest_3,
                    R.drawable.dama_netral_3,
                    R.drawable.dama_dark_3,
                    R.drawable.dama_boloto_3,
                    R.drawable.dama_gori_3,
                )
            ),
        )
    }
    var selectedBackgroundThemeIndexPlayer1 by remember { mutableStateOf(0) }
    var selectedBackgroundThemeIndexPlayer2 by remember { mutableStateOf(0) }


    val imageResourceIds = listOf(
        R.drawable.stepi,
        R.drawable.forest,
        R.drawable.netral,
        R.drawable.dark,
        R.drawable.boloto,
        R.drawable.gori
    )

    val backgroundColors = listOf(
        Color(0xFFE6D690),
        Color(0xFFA8E4A0),
        Color(0xFF4717A),
        Color(0xFFD8BFD8),
        Color(0xFFACB78E),
        Color(0xFF9ACEEB)
    )

    var resetHpPlayer1 by remember { mutableStateOf(25) }
    var resetHpPlayer2 by remember { mutableStateOf(25) }
    var resetPlayer1 = { player1Life.value = resetHpPlayer1 }
    var resetPlayer2 = { player2Life.value = resetHpPlayer2 }
    var уголПоворота by remember { mutableStateOf(0f) }
    var selectedElementPlayer1 by remember { mutableStateOf(0) }
    var selectedElementPlayer2 by remember { mutableStateOf(1) }
    var showImagePickerDialog by remember { mutableStateOf<Int?>(null) } // Индекс игрока для диалога
    var showEditDialog by remember { mutableStateOf(false) }
    var editingPlayer by remember { mutableStateOf<Int?>(null) } // 0 - Player1, 1 - Player2
    var newHpValue by remember { mutableStateOf("") }
    val listener = remember { orientationChangeListener } // Сохраняем listener в remember
    var showBackgroundThemeDialog by remember { mutableStateOf<Int?>(null) }
    var gameMode by remember { mutableStateOf(GameMode.STANDARD) } // Состояние режима игры
    var showGameModeMenu by remember { mutableStateOf(false) }


    Scaffold(modifier = Modifier.fillMaxSize(), contentColor = Color.White) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF202020), Color(0xFF242424))
                    )
                )
                .rotate(уголПоворота) // Поворачиваем весь экран
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var showImageMenuDialog by remember { mutableStateOf<Int?>(null) }

                if (showImageMenuDialog != null) {
                    ImageOptionsMenu(
                        onDismiss = { showImageMenuDialog = null },
                        onGameModeClick = { showGameModeMenu = true }, // Открываем GameModeMenu
                        onAppearanceClick = {
                            showBackgroundThemeDialog = showImageMenuDialog
                            Log.d("Menu","Appearance Clicked")
                        },
                        rotate = (showImageMenuDialog == 1)
                    )
                }
                if (showGameModeMenu) {
                    GameModeMenu(
                        gameMode = gameMode,
                        onGameModeClick = { newMode ->
                            gameMode = newMode
                            showGameModeMenu = false
                        },
                        onDismiss = { showGameModeMenu = false }
                    )
                }
                if (showBackgroundThemeDialog != null) {
                    BackgroundThemeSelectionDialog(
                        themes = themes,
                        onThemeSelected = { index ->
                            if(showBackgroundThemeDialog == 0){
                                selectedBackgroundThemeIndexPlayer1 = index
                            }else if (showBackgroundThemeDialog == 1){
                                selectedBackgroundThemeIndexPlayer2 = index
                            }
                            showBackgroundThemeDialog = null
                        },
                        onDismiss = { showBackgroundThemeDialog = null },
                        rotate = (showBackgroundThemeDialog == 1)
                    )
                }
                when (gameMode) {
                    GameMode.STANDARD -> {
                        StandardLayout(
                            player1Life = player1Life,
                            player2Life = player2Life,
                            resetPlayer1 = resetPlayer1,
                            resetPlayer2 = resetPlayer2,
                            berserkGold = berserkGold,
                            berserkBloodRed = berserkBloodRed,
                            berserkDarkGrey = berserkDarkGrey,
                            berserkShadow = berserkShadow,
                            imageResourceIds = imageResourceIds,
                            backgroundColors = backgroundColors,
                            context = LocalContext.current,
                            themes = themes,
                            selectedBackgroundThemeIndexPlayer1 = selectedBackgroundThemeIndexPlayer1,
                            selectedBackgroundThemeIndexPlayer2 = selectedBackgroundThemeIndexPlayer2,
                            gradientColors = gradientColors,
                            selectedElementPlayer1 = selectedElementPlayer1,
                            selectedElementPlayer2 = selectedElementPlayer2,
                            onShowEditDialog = { editingPlayer = it; showEditDialog = true },
                            onImageMenuClick = {
                                if(it == null){
                                    showImagePickerDialog = it
                                } else{
                                    showImageMenuDialog = it
                                }
                            },
                            onElementClick = { showImagePickerDialog = it }
                        )
                    }
                    GameMode.SINGLE -> {
                        SingleLayout(
                            player1Life = player1Life,
                            resetPlayer1 = resetPlayer1,
                            berserkGold = berserkGold,
                            berserkBloodRed = berserkBloodRed,
                            berserkDarkGrey = berserkDarkGrey,
                            berserkShadow = berserkShadow,
                            imageResourceIds = imageResourceIds,
                            backgroundColors = backgroundColors,
                            context = LocalContext.current,
                            themes = themes,
                            selectedBackgroundThemeIndexPlayer1 = selectedBackgroundThemeIndexPlayer1,
                            gradientColors = gradientColors,
                            selectedElementPlayer1 = selectedElementPlayer1,
                            onShowEditDialog = { editingPlayer = it; showEditDialog = true },
                            onImageMenuClick = {
                                if (it == null) {
                                    showImagePickerDialog = it
                                } else {
                                    showImageMenuDialog = it
                                }
                            },
                            onElementClick = {
                                showImagePickerDialog = it
                            }
                        )
                    }

                }
            }
        }
    }
    LaunchedEffect(showEditDialog, editingPlayer) {
        if (orientationChangeListener != null) {
            orientationChangeListener.onPlayer2HpChange(showEditDialog && editingPlayer == 1)
        }
    }

    if (showEditDialog && listener != null) {
        EditHpDialog(
            onDismiss = { showEditDialog = false },
            onSave = { hp, playerNum ->
                if (playerNum == 0) {
                    player1Life.value = hp
                    resetHpPlayer1 = hp
                } else {
                    player2Life.value = hp
                    resetHpPlayer2 = hp
                }
                listener.onPlayer2HpChange(false)
                showEditDialog = false
                уголПоворота = 0f
            },

            initialHp = if (editingPlayer == 0) player1Life.value else player2Life.value,
            playerNumber = editingPlayer ?: 0,
            onRotationChange = { angle -> уголПоворота = angle }
        )
    }

    if (showImagePickerDialog != null) {
        RotatableImagePickerDialog(
            imageResourceIds = imageResourceIds,
            onImageSelected = { index ->
                when (showImagePickerDialog) {
                    0 -> selectedElementPlayer1 = index
                    1 -> selectedElementPlayer2 = index
                    else -> {}
                }
                showImagePickerDialog = null
            },
            onDismiss = { showImagePickerDialog = null },
            rotate = (showImagePickerDialog == 1)
        )
    }

}


@Composable
fun GameModeMenu(
    gameMode: GameMode,
    onGameModeClick: (GameMode) -> Unit,
    onDismiss: () -> Unit,
    rotate: Boolean = false
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Выберите Режим Игры",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

                GameMode.values().forEach { mode ->
                    GameModeItem(
                        mode = mode,
                        selected = mode == gameMode,
                        onClick = {
                            onGameModeClick(mode)
                            onDismiss()
                        }
                    )
                    if (mode != GameMode.values().last()) {
                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                    }
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                ) {
                    Text("Закрыть", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun GameModeItem(mode: GameMode, selected: Boolean, onClick: () -> Unit) {
    val context = LocalContext.current // Получаем контекст

    ElevatedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp),
        colors = if (selected) {
            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        } else {
            ButtonDefaults.buttonColors()
        }

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Получите Description для режима игры, используя stringResource
            val description = when (mode) {
                GameMode.STANDARD -> stringResource(R.string.standard_game_mode_description)
                GameMode.SINGLE -> stringResource(R.string.single_game_mode_description)
            }
            // Иконка для режима игры
            val icon = when (mode) {
                GameMode.STANDARD -> painterResource(id = R.drawable.number_2_3830) // Замените на свою иконку
                GameMode.SINGLE -> painterResource(id = R.drawable.number_1_3080) // Замените на свою иконку
            }
            Icon(painter = icon, contentDescription = null)
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = when (mode) {
                        GameMode.STANDARD -> "Стандартный Режим"
                        GameMode.SINGLE -> "Режим Одиночной Игры"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }


            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Выбрано",
                    tint = Color.White
                )
            }
        }
    }
}






@Composable
fun StandardLayout(
    player1Life: MutableState<Int>,
    player2Life: MutableState<Int>,
    resetPlayer1: () -> Unit,
    resetPlayer2: () -> Unit,
    berserkGold: Color,
    berserkBloodRed: Color,
    berserkDarkGrey: Color,
    berserkShadow: Color,
    imageResourceIds: List<Int>,
    backgroundColors: List<Color>,
    context: Context,
    themes: List<Theme>,
    selectedBackgroundThemeIndexPlayer1: Int,
    selectedBackgroundThemeIndexPlayer2: Int,
    gradientColors: List<Color>,
    selectedElementPlayer1: Int,
    selectedElementPlayer2: Int,
    onShowEditDialog: (Int) -> Unit,
    onImageMenuClick: (Int?) -> Unit,
    onElementClick: (Int?) -> Unit // Новый параметр
) {
    Column(modifier = Modifier.fillMaxSize()) {  // Добавление Column
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            PlayerLifeCounterCard(
                onReset = resetPlayer2,
                lifeTotal = player2Life,
                berserkGold,
                berserkBloodRed,
                berserkDarkGrey,
                berserkShadow,
                imageResourceIds,
                backgroundColors,
                maxLife = 100,
                context = context,
                backgroundImageIds = themes[selectedBackgroundThemeIndexPlayer2].backgroundIds,
                onLifeChange = { newValue, increased -> player2Life.value = newValue },
                rotate = true,
                backgroundImageIndex = selectedElementPlayer2,
                gradientColors = gradientColors,
                onShowEditDialog = { onShowEditDialog(1) },
                onImageMenuClick = onImageMenuClick,
                onElementClick = onElementClick
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            PlayerLifeCounterCard(
                onReset = resetPlayer1,
                lifeTotal = player1Life,
                berserkGold,
                berserkBloodRed,
                berserkDarkGrey,
                berserkShadow,
                imageResourceIds,
                backgroundColors,
                maxLife = 100,
                context = context,
                backgroundImageIds = themes[selectedBackgroundThemeIndexPlayer1].backgroundIds,
                onLifeChange = { newValue, increased -> player1Life.value = newValue },
                gradientColors = gradientColors,
                backgroundImageIndex = selectedElementPlayer1,
                onShowEditDialog = { onShowEditDialog(0) },
                onImageMenuClick = onImageMenuClick,
                onElementClick = onElementClick
            )
        }
    }
}

@Composable
fun SingleLayout(
    player1Life: MutableState<Int>,
    resetPlayer1: () -> Unit,
    berserkGold: Color,
    berserkBloodRed: Color,
    berserkDarkGrey: Color,
    berserkShadow: Color,
    imageResourceIds: List<Int>,
    backgroundColors: List<Color>,
    context: Context,
    themes: List<Theme>,
    selectedBackgroundThemeIndexPlayer1: Int,
    gradientColors: List<Color>,
    selectedElementPlayer1: Int,
    onShowEditDialog: (Int) -> Unit,
    onImageMenuClick: (Int?) -> Unit,
    onElementClick: (Int?) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) { // Обёртка в Column
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            PlayerLifeCounterCard(
                onReset = resetPlayer1,
                lifeTotal = player1Life,
                berserkGold,
                berserkBloodRed,
                berserkDarkGrey,
                berserkShadow,
                imageResourceIds,
                backgroundColors,
                maxLife = 100,
                context = context,
                backgroundImageIds = themes[selectedBackgroundThemeIndexPlayer1].backgroundIds,
                onLifeChange = { newValue, increased -> player1Life.value = newValue },
                gradientColors = gradientColors,
                backgroundImageIndex = selectedElementPlayer1,
                onShowEditDialog = { onShowEditDialog(0) },
                onImageMenuClick = onImageMenuClick,
                onElementClick = onElementClick
            )
        }
    }
}

@Composable
fun GeneralLayout(
    player1Life: MutableState<Int>,
    player2Life: MutableState<Int>,
    resetPlayer1: () -> Unit,
    resetPlayer2: () -> Unit,
    berserkGold: Color,
    berserkBloodRed: Color,
    berserkDarkGrey: Color,
    berserkShadow: Color,
    imageResourceIds: List<Int>,
    backgroundColors: List<Color>,
    context: Context,
    themes: List<Theme>,
    selectedBackgroundThemeIndexPlayer1: Int,
    selectedBackgroundThemeIndexPlayer2: Int,
    gradientColors: List<Color>,
    selectedElementPlayer1: Int,
    selectedElementPlayer2: Int,
    onShowEditDialog: (Int) -> Unit,
    onImageMenuClick: (Int?) -> Unit,
    onElementClick: (Int?) -> Unit
) {
    val playerLifeList = remember {
        mutableStateListOf<MutableState<Int>>(  // Явно указываем тип
            player1Life,
            player2Life,
            mutableStateOf(25), // Используем mutableStateOf здесь
            mutableStateOf(25),
            mutableStateOf(25),
            mutableStateOf(25)
        )
    }
    val resetFunctions = remember {
        listOf(
            resetPlayer1,
            resetPlayer2,
            { playerLifeList[2].value = 25 },
            { playerLifeList[3].value = 25 },
            { playerLifeList[4].value = 25 },
            { playerLifeList[5].value = 25 }
        )
    }
    val selectedBackgroundThemeIndexes = remember {
        mutableStateListOf(
            selectedBackgroundThemeIndexPlayer1,
            selectedBackgroundThemeIndexPlayer2,
            0,
            0,
            0,
            0,
        )
    }

    val selectedElementIndexes = remember {
        mutableStateListOf(
            selectedElementPlayer1,
            selectedElementPlayer2,
            0,
            0,
            0,
            0,
        )
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(playerLifeList) { index, playerLife ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                PlayerLifeCounterCard(
                    onReset = resetFunctions[index],
                    lifeTotal = playerLife,
                    berserkGold = berserkGold,
                    berserkBloodRed = berserkBloodRed,
                    berserkDarkGrey = berserkDarkGrey,
                    berserkShadow = berserkShadow,
                    imageResourceIds = imageResourceIds,
                    backgroundColors = backgroundColors,
                    maxLife = 100,
                    context = context,
                    backgroundImageIds = themes[selectedBackgroundThemeIndexes[index]].backgroundIds,
                    onLifeChange = { newValue, increased -> playerLife.value = newValue },
                    gradientColors = gradientColors,
                    backgroundImageIndex = selectedElementIndexes[index],
                    onShowEditDialog = { onShowEditDialog(index) },
                    onImageMenuClick = onImageMenuClick,
                    onElementClick = onElementClick
                )
            }
        }
    }
}






@Composable
fun ElementSelectionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    context: Context
) {
    val soundManager = remember { SoundManager(context) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonColor = if (isPressed) Color.Red.copy(alpha = 0.8f) else Color.Red
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.1f else 1f,
        animationSpec = tween(100)
    )
    IconButton(
        onClick = {
            onClick() // Передаем null чтобы открыть диалог
            soundManager.playSound(SoundManager.SoundType.Menu)
        },
        modifier = modifier
            .clip(CircleShape)
            .scale(scale)
            .size(58.dp * scale),
        interactionSource = interactionSource,
        colors = IconButtonDefaults.iconButtonColors(containerColor = buttonColor)
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_dialog_dialer),
            contentDescription = "Выберите стихию",
            tint = Color.White
        )
    }
}






@Composable
fun ResetButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    context: Context
) { // Добавили context
    val soundManager = remember { SoundManager(context) } // Создаем SoundManager здесь
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonColor = if (isPressed) Color.Red.copy(alpha = 0.8f) else Color.Red
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.1f else 1f,
        animationSpec = tween(100)
    )

    IconButton(
        onClick = {
            onClick()
            soundManager.playSound(SoundManager.SoundType.RESET)
        },
        modifier = modifier
            .clip(CircleShape)
            .scale(scale)
            .size(58.dp * scale),
        interactionSource = interactionSource,
        colors = IconButtonDefaults.iconButtonColors(containerColor = buttonColor)
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_rotate),
            contentDescription = "Сбросить",
            tint = Color.White
        )
    }
}

@Composable
fun RotatableImagePickerDialog(
    imageResourceIds: List<Int>,
    onImageSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
    rotate: Boolean = false // Флаг для вращения
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .rotate(if (rotate) 180f else 0f) // Вращение здесь

        ) {
            Column {
                Text(
                    "Выберите стихию",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp), // Уменьшенный отступ
                    verticalArrangement = Arrangement.spacedBy(4.dp), // Уменьшенный отступ
                    horizontalArrangement = Arrangement.spacedBy(4.dp) // Уменьшенный отступ
                ) {
                    items(imageResourceIds.size) { index ->
                        ImageItem(imageResourceIds[index], index, onImageSelected, onDismiss)
                    }
                }
                Button(onClick = onDismiss) {
                    Text("Отмена")
                }
            }
        }
    }
}

@Composable
fun EditHpDialog(
    onSave: (Int, Int) -> Unit,
    initialHp: Int,
    onDismiss: () -> Unit,
    playerNumber: Int,
    onRotationChange: (Float) -> Unit
    // removed showInitialValue: Boolean = false
) {
    var hpValue by remember(initialHp) { // Используем initialHp как ключ для remember
        mutableStateOf(initialHp.toString()) // Инициализируем с текущим HP
    }
    var showError by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Dialog(onDismissRequest = onDismiss) { // onDismissRequest directly calls onDismiss
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.8f),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Изменение HP", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = hpValue,
                        onValueChange = { newHpValue ->
                            hpValue = newHpValue.filter { it.isDigit() }
                            showError = false
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        isError = showError,
                        label = { Text("HP") },
                        supportingText = {
                            if (showError) {
                                Text(
                                    "Пожалуйста, введите число от 0 до 99",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = onDismiss) { Text("Отмена") } // Directly calls onDismiss
                        Button(onClick = {
                            val parsedHp = hpValue.toIntOrNull()
                            val hpToSave = parsedHp ?: 0 // Handle null case gracefully
                            if (hpToSave in 0..99) {
                                onSave(hpToSave, playerNumber)
                                onDismiss()
                            } else if (hpValue.isEmpty()) {
                                onSave(0, playerNumber)
                                onDismiss()
                            } else {
                                showError = true
                            }
                        }) {
                            Text("ОК")
                        }
                    }
                }
            }
        }
        //Removed LaunchedEffect -  This was likely interfering. Handle orientation changes elsewhere.
    }
}


@Composable
fun PlayerLifeCounterCard(
    onReset: () -> Unit,
    lifeTotal: MutableState<Int>,
    berserkGold: Color,
    berserkBloodRed: Color,
    berserkDarkGrey: Color,
    berserkShadow: Color,
    imageResourceIds: List<Int>,
    backgroundColors: List<Color>,
    maxLife: Int,
    context: Context,
    backgroundImageIds: List<Int>,
    gradientColors: List<Color>,
    onLifeChange: (Int, Boolean) -> Unit,
    rotate: Boolean = false,
    backgroundImageIndex: Int,
    onShowEditDialog: () -> Unit,
    onImageMenuClick: (Int?) -> Unit,
    onElementClick: (Int?) -> Unit // Новый параметр
) {
    val soundManager = remember { SoundManager(context) }
    DisposableEffect(Unit) {
        onDispose { soundManager.release() }
    }
    val backgroundImageIndexState = rememberUpdatedState(newValue = backgroundImageIndex)

    val backgroundColor =
        remember(backgroundImageIndexState.value) { backgroundColors[backgroundImageIndexState.value % backgroundColors.size] }
    val imageResourceId =
        remember(backgroundImageIndexState.value) { imageResourceIds[backgroundImageIndexState.value % imageResourceIds.size] }
    val backgroundImageId = backgroundImageIds[backgroundImageIndexState.value % backgroundImageIds.size]


    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(initialOffsetX = { if (rotate) -it else it }) + fadeIn(
            animationSpec = tween(500)
        ),
        exit = slideOutHorizontally() + fadeOut(animationSpec = tween(500))
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .then(if (rotate) Modifier.rotate(180f) else Modifier)
                .graphicsLayer {
                    scaleX = 1f
                    scaleY = 1f
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.verticalGradient(gradientColors))
            ) {
                Image(
                    painter = painterResource(id = backgroundImageId),
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onImageMenuClick(if (rotate) 1 else 0) }
                )

                Row(                    modifier = Modifier
                    .fillMaxSize()
                    .padding(19.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.padding(13.dp)) {
                        ResetButton(
                            context = context,
                            onClick = onReset
                        )
                    }

                    Column(modifier = Modifier.padding(5.dp)) {
                        ElementSelectionButton(
                            modifier = Modifier.padding(7.dp),
                            onClick = {
                                onElementClick(if(rotate) 1 else 0) // Передаем индекс игрока
                            },
                            context = context,
                        )
                    }
                }

                PlayerLifeCounter(
                    lifeTotal = lifeTotal,
                    maxLife = maxLife,
                    imageResourceId = imageResourceId,
                    backgroundColor = backgroundColor,
                    backgroundImageIndex = backgroundImageIndexState.value,
                    context = context,
                    onShowEditDialog = onShowEditDialog,
                    onLifeChange = { newValue, increased ->
                        lifeTotal.value = newValue
                        soundManager.playSound(
                            when {
                                increased -> SoundManager.SoundType.DECREASE
                                else -> SoundManager.SoundType.INCREASE
                            }
                        )
                    },
                    onImageClick = { onImageMenuClick(if(rotate) 1 else 0) }
                )
            }
        }
    }
}





@Composable
fun PlayerLifeCounter(
    lifeTotal: MutableState<Int>,
    maxLife: Int,
    imageResourceId: Int,
    backgroundColor: Color,
    backgroundImageIndex: Int,
    onLifeChange: (Int, Boolean) -> Unit,
    context: Context,
    onShowEditDialog: () -> Unit,
    onImageClick: () -> Unit // Изменено

) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val isWideScreen = screenWidth > 350.dp

    val buttonSize = min(screenWidth / 6, 70.dp).coerceAtLeast(50.dp)
    val iconSize = buttonSize / 1.5f
    val spacing = if (isWideScreen) 16.dp else 4.dp
    val hpTextSize = if (isWideScreen) 110.sp else 65.sp

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = imageResourceId),
                contentDescription = stringResource(R.string.player_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .alpha(
                        if (lifeTotal.value <= 25) {
                            (0.2f + (lifeTotal.value.toFloat() / 25f) * 0.8f).coerceIn(0.2f, 1f)
                        } else {
                            1f
                        }
                    )
                    .padding(bottom = 8.dp)
                    .animateContentSize(animationSpec = tween(300))
                    .clickable { onImageClick() }
            )
            AnimatedNumberText(
                number = lifeTotal.value,
                backgroundColor = backgroundColor,
                textSize = hpTextSize,
                onShowEditDialog = onShowEditDialog,
                context = context
            )

        }

        if (isWideScreen) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedActionButton(
                    onClick = { onLifeChange(maxOf(0, lifeTotal.value - 1), false) },
                    icon = Icons.Filled.FavoriteBorder,
                    contentDescription = "Decrease life",
                    buttonSize = buttonSize,
                    iconSize = iconSize,
                    cornerRadius = 12.dp,
                    modifier = Modifier.weight(1f)
                )
                AnimatedActionButton(
                    onClick = { onLifeChange(maxOf(0, lifeTotal.value + 1), true) },
                    icon = Icons.Filled.Favorite,
                    contentDescription = "Increase life",
                    buttonSize = buttonSize,
                    iconSize = iconSize,
                    cornerRadius = 12.dp,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AnimatedActionButton(
                    onClick = { onLifeChange(maxOf(0, lifeTotal.value - 1), false) },
                    icon = Icons.Filled.FavoriteBorder,
                    contentDescription = "Decrease life",
                    buttonSize = buttonSize,
                    iconSize = iconSize,
                    cornerRadius = 12.dp,
                )
                AnimatedActionButton(
                    onClick = { onLifeChange(maxOf(0, lifeTotal.value + 1), false) },
                    icon = Icons.Filled.Favorite,
                    contentDescription = "Increase life",
                    buttonSize = buttonSize,
                    iconSize = iconSize,
                    cornerRadius = 12.dp,
                )
            }
        }
    }
}

@Composable
fun ImageItem(resourceId: Int, index: Int, onImageSelected: (Int) -> Unit, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onImageSelected(index)
                onDismiss()
            },
        shape = RoundedCornerShape(8.dp), // Скругленные углы
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Тень
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), // Отступ от краев
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun AnimatedActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    neutralColor: Color = Color(0x808B0000),
    activeColor: Color = Color(0xFF8B0000),
    buttonSize: Dp = 40.dp,
    iconSize: Dp = 20.dp,
    cornerRadius: Dp = 12.dp, // Добавлен параметр cornerRadius
    buttonPadding: Dp = 2.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonColor by animateColorAsState(
        targetValue = if (isPressed) activeColor else neutralColor,
        animationSpec = tween(100)
    )

    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(buttonSize)
            .clip(RoundedCornerShape(cornerRadius))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(cornerRadius)),
        interactionSource = interactionSource,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = buttonColor,
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier
                .size(iconSize)
                .padding(buttonPadding)
        )
    }
}


@Composable
fun AnimatedNumberText(
    number: Int,
    backgroundColor: Color,
    textSize: TextUnit,
    onShowEditDialog: () -> Unit,
    context: Context
) {
    val soundManager = remember { SoundManager(context) }
    val soundLoadSuccess by remember(soundManager) {
        derivedStateOf { soundManager.soundLoadSuccess.value }
    }

    LaunchedEffect(soundLoadSuccess) {
        if (soundLoadSuccess) {
            Log.d("SoundManager", "Sounds loaded successfully!")
        } else {
            Log.e("SoundManager", "Sound loading failed.")
        }
    }

    val transition = updateTransition(targetState = number, label = "life")
    val animatedNumber by transition.animateFloat(label = "number") { it.toFloat() }

    val textColor = Color.White

    Box(modifier = Modifier.padding(4.dp)) {
        Box(
            modifier = Modifier
                .clickable {
                    onShowEditDialog()
                    if (soundLoadSuccess) { // only play sound if loading was successful.
                        soundManager.playSound(SoundManager.SoundType.Text)
                    }
                }
                .background(
                    color = Color.Black.copy(alpha = 0.7f), // Затемняем фон здесь
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = animatedNumber.toInt().toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontSize = textSize,
                color = textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
    DisposableEffect(Unit) {
        onDispose { soundManager.release() }
    }
}


@Composable
fun ImageOptionsMenu(
    onDismiss: () -> Unit,
    onGameModeClick: () -> Unit,
    onAppearanceClick: () -> Unit,
    rotate: Boolean = false
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Настройки Игры",
                    style = MaterialTheme.typography.headlineSmall
                )
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                MenuItem(
                    text = "Выбор Режима",
                    description = "Изменить режим игры",
                    icon = Icons.Filled.Settings, // Замените на более подходящую иконку
                    onClick = onGameModeClick
                )

                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                MenuItem(
                    text = "Настройка Внешнего Вида",
                    description = "Изменить внешний вид игры",
                    icon = Icons.Filled.Person, // Замените на более подходящую иконку
                    onClick = onAppearanceClick
                )

                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Закрыть")
                }
            }
        }
    }
}

@Composable
fun MenuItem(text: String, description: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}



@Composable
fun BackgroundThemeSelectionDialog(
    themes: List<Theme>,
    onThemeSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
    rotate: Boolean = false
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(modifier = Modifier.rotate(if (rotate) 180f else 0f)) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
                    .heightIn(max = 500.dp),  // Ограничение высоты
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Выберите тему оформления", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1), // Одна колонка для вертикального списка
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(themes) { index, theme ->
                            BackgroundThemeSetItem(
                                theme = theme,
                                onClick = {
                                    onThemeSelected(index)
                                    onDismiss()
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                        Text("Отмена")
                    }
                }
            }
        }
    }
}

@Composable
fun BackgroundThemeSetItem(theme: Theme, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp), // Скругленные углы
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Тень
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = theme.name,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                theme.backgroundIds.take(3).forEach { imageId -> // Отображаем до 3х превью
                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
