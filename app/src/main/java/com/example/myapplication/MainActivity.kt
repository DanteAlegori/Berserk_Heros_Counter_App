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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
                name = "Стандартная",
                backgroundIds =  listOf(
                    R.drawable.stepi_bacgraund,
                    R.drawable.forest_bacgraund,
                    R.drawable.natral_bacgraund,
                    R.drawable.dark_bacgraund,
                    R.drawable.boloto_backgraund,
                    R.drawable.gori_bacgraund,
                )
            ),
            Theme(
                name = "Темная",
                backgroundIds = listOf(
                    R.drawable.dark_bacgraund,
                    R.drawable.boloto_backgraund,
                    R.drawable.gori_bacgraund,
                    R.drawable.stepi_bacgraund,
                    R.drawable.forest_bacgraund,
                    R.drawable.natral_bacgraund,
                )
            ),
            Theme(
                name = "Природа",
                backgroundIds =  listOf(
                    R.drawable.gori_bacgraund,
                    R.drawable.stepi_bacgraund,
                    R.drawable.forest_bacgraund,
                    R.drawable.natral_bacgraund,
                    R.drawable.dark_bacgraund,
                    R.drawable.boloto_backgraund
                )
            ),
            Theme(
                name = "Горы",
                backgroundIds = listOf(
                    R.drawable.gori_bacgraund,
                    R.drawable.dark_bacgraund,
                    R.drawable.stepi_bacgraund,
                    R.drawable.forest_bacgraund,
                    R.drawable.natral_bacgraund,
                    R.drawable.boloto_backgraund,

                    )
            ),
            Theme(
                name = "Пустыня",
                backgroundIds = listOf(
                    R.drawable.stepi_bacgraund,
                    R.drawable.gori_bacgraund,
                    R.drawable.forest_bacgraund,
                    R.drawable.natral_bacgraund,
                    R.drawable.dark_bacgraund,
                    R.drawable.boloto_backgraund
                )
            ),

            Theme(
                name = "Океан",
                backgroundIds =  listOf(
                    R.drawable.natral_bacgraund,
                    R.drawable.gori_bacgraund,
                    R.drawable.stepi_bacgraund,
                    R.drawable.forest_bacgraund,
                    R.drawable.dark_bacgraund,
                    R.drawable.boloto_backgraund
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
                        onGameModeClick = {
                            // TODO: Handle game mode click
                            Log.d("Menu","Game Mode Clicked")
                        },
                        onAppearanceClick = {
                            showBackgroundThemeDialog = showImageMenuDialog
                            Log.d("Menu","Appearance Clicked")
                        },
                        rotate = (showImageMenuDialog == 1)
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
                        context = LocalContext.current,
                        backgroundImageIds = themes[selectedBackgroundThemeIndexPlayer2].backgroundIds,
                        onLifeChange = { newValue, increased -> player2Life.value = newValue },
                        rotate = true,
                        backgroundImageIndex = selectedElementPlayer2,
                        gradientColors = gradientColors,
                        onShowEditDialog = { editingPlayer = 1; showEditDialog = true },
                        onImageMenuClick = {
                            if(it == null){
                                showImagePickerDialog = 1
                            } else{
                                showImageMenuDialog = it
                            }
                        }
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
                        context = LocalContext.current,
                        backgroundImageIds = themes[selectedBackgroundThemeIndexPlayer1].backgroundIds,
                        onLifeChange = { newValue, increased -> player1Life.value = newValue },
                        gradientColors = gradientColors,
                        backgroundImageIndex = selectedElementPlayer1,
                        onShowEditDialog = {
                            editingPlayer = 0; showEditDialog = true
                        },
                        onImageMenuClick = {
                            if(it == null){
                                showImagePickerDialog = 0
                            } else{
                                showImageMenuDialog = it
                            }
                        }
                    )
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





// ElementSelectionButton (изменено)
@Composable
fun ElementSelectionButton(
    modifier: Modifier = Modifier,
    onClick: (Int?) -> Unit,
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
            onClick(null) // Передаем null чтобы открыть диалог
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
    onRotationChange: (Float) -> Unit,
    showInitialValue: Boolean = false
) {
    var hpValue by remember(initialHp, showInitialValue) {
        mutableStateOf(if (showInitialValue) initialHp.toString() else "")
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
) {
    val soundManager = remember { SoundManager(context) }
    DisposableEffect(Unit) {
        onDispose { soundManager.release() }
    }
    val backgroundColor =
        remember(backgroundImageIndex) { backgroundColors[backgroundImageIndex % backgroundColors.size] }
    val imageResourceId =
        remember(backgroundImageIndex) { imageResourceIds[backgroundImageIndex % imageResourceIds.size] }
    val backgroundImageId = backgroundImageIds[backgroundImageIndex % backgroundImageIds.size]


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
                        .clickable { onImageMenuClick(if (rotate) 1 else 0) } // Открытие меню по клику на Image
                )

                Row(
                    modifier = Modifier
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
                            onClick = { onImageMenuClick(it) },
                            context = context
                        )
                    }
                }

                PlayerLifeCounter(
                    lifeTotal = lifeTotal,
                    maxLife = maxLife,
                    imageResourceId = imageResourceId,
                    backgroundColor = backgroundColor,
                    backgroundImageIndex = backgroundImageIndex,
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
                    color = textColor.copy(alpha = 0.2f),
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
    rotate:Boolean = false
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(modifier = Modifier.rotate(if (rotate) 180f else 0f)) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.8f),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Настройки Игрока", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        onGameModeClick()
                        onDismiss()
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Режим игры")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        onAppearanceClick()
                        // onDismiss()
                    },  modifier = Modifier.fillMaxWidth()) {
                        Text("Оформление")
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

// BackgroundThemeSetItem (изменено)
@Composable
fun BackgroundThemeSetItem(theme: Theme, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = theme.name,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
