package com.example.myapplication


import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.SoundManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    LifeCounterApp(navController)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun LifeCounterApp(navController: androidx.navigation.NavController) {
    val berserkGold = Color(0xFFB87333)
    val berserkBloodRed = Color(0xFF8B0000)
    val berserkDarkGrey = Color(0xFF2F2F2F)
    val berserkShadow = Color(0xFF000000)
    val player1Life = remember { mutableStateOf(25) }
    val player2Life = remember { mutableStateOf(25) }
    val gradientColors = listOf(Color(0xFF333333), Color(0xFF666666))
    val backgroundImageIds = listOf(
        R.drawable.stepi_bacgraund,
        R.drawable.forest_bacgraund,
        R.drawable.natral_bacgraund,
        R.drawable.dark_bacgraund,
        R.drawable.boloto_backgraund,
        R.drawable.gori_bacgraund
    )

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
    var selectedElementPlayer1 by remember { mutableStateOf(0) }
    var selectedElementPlayer2 by remember { mutableStateOf(1) }
    var showImagePickerDialog by remember { mutableStateOf<Int?>(null) } // Индекс игрока для диалога


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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Отступ от краев экрана
                verticalArrangement = Arrangement.SpaceBetween, // Равномерно распределяем пространство между картами
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .weight(1f) // Занимаем половину доступного пространства
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

                        lifeTotal = player2Life,
                        berserkGold,
                        berserkBloodRed,
                        berserkDarkGrey,
                        berserkShadow,
                        imageResourceIds,
                        backgroundColors,
                        maxLife = 100,
                        context = LocalContext.current,
                        backgroundImageIds,
                        onLifeChange = { newValue, increased -> player2Life.value = newValue },
                        rotate = true,
                        backgroundImageIndex = selectedElementPlayer2,
                        gradientColors = gradientColors,
                        onImageIndexChange = { selectedElementPlayer2 = it },
                        onShowImagePickerDialog = { showImagePickerDialog = 1 }

                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .weight(1f) // Занимаем половину доступного пространства
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
                        lifeTotal = player1Life,
                        berserkGold,
                        berserkBloodRed,
                        berserkDarkGrey,
                        berserkShadow,
                        imageResourceIds,
                        backgroundColors,
                        maxLife = 100,
                        context = LocalContext.current,
                        backgroundImageIds,
                        onLifeChange = { newValue, increased -> player1Life.value = newValue },
                        gradientColors = gradientColors,
                        backgroundImageIndex = selectedElementPlayer1,
                        onImageIndexChange = { selectedElementPlayer1 = it },
                        onShowImagePickerDialog = { showImagePickerDialog = 0 }

                    )

                }
            }
        }
        if (showImagePickerDialog != null) {
            ImagePickerDialog(
                imageResourceIds = imageResourceIds,
                onImageSelected = { index ->
                    //  Обновляем состояние в зависимости от того, для какого игрока был открыт диалог
                    when (showImagePickerDialog) {
                        0 -> selectedElementPlayer1 = index
                        1 -> selectedElementPlayer2 = index
                        else -> {} // Обработка некорректных значений
                    }
                    showImagePickerDialog = null
                },
                onDismiss = { showImagePickerDialog = null }
            )
        }
}}


@Composable
fun ElementSelectionButton(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit, // Изменено: теперь функция принимает индекс
    onImageIndexChange: (Int) -> Unit,
    selectedImageIndex: Int
) {
val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonColor = if (isPressed) Color.Red.copy(alpha = 0.8f) else Color.Red
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.1f else 1f,
        animationSpec = tween(100)
    )
    IconButton(
        onClick = { onClick(selectedImageIndex) },
        modifier = modifier
            .clip(CircleShape)
            .scale(scale)
            .size(50.dp * scale), // Размер теперь зависит от масштаба
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
fun ResetButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonColor = if (isPressed) Color.Red.copy(alpha = 0.8f) else Color.Red
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.1f else 1f,
        animationSpec = tween(100)
    )

    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(CircleShape)
            .scale(scale)
            .size(50.dp * scale), // Размер теперь зависит от масштаба
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerLifeCounterCard(
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
    onImageIndexChange: (Int) -> Unit,
    onLifeChange: (Int, Boolean) -> Unit,
    rotate: Boolean = false,
    backgroundImageIndex: Int,
    onShowImagePickerDialog: (Int) -> Unit
) {
    val soundManager = remember { SoundManager(context) }
    DisposableEffect(Unit) {
        onDispose { soundManager.release() }
    }
    val backgroundColor = remember(backgroundImageIndex) { backgroundColors[backgroundImageIndex % backgroundColors.size] }
    val imageResourceId = remember(backgroundImageIndex) { imageResourceIds[backgroundImageIndex % imageResourceIds.size] }


    val backgroundImageId = backgroundImageIds[backgroundImageIndex % backgroundImageIds.size]
    var showDialog by remember { mutableStateOf(false) }


    // Function to display or dismiss the ImagePickerDialog



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
                    scaleX = 1f // Убрал анимацию масштаба, т.к. она здесь избыточна
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
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(19.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.padding(13.dp)) {
                        ResetButton(onClick = {
                            lifeTotal.value = 25
                            soundManager.playSound(SoundManager.SoundType.RESET)
                        })
                    }
                    Column(modifier = Modifier.padding(5.dp)) {
                        ElementSelectionButton(
                            modifier = Modifier.padding(7.dp),
                            onClick = onShowImagePickerDialog, // Вызываем функцию из LifeCounterApp
                            onImageIndexChange = onImageIndexChange,// Передаем функцию сюда
                            selectedImageIndex = backgroundImageIndex
                        )
                    }
                }

                PlayerLifeCounter(
                    lifeTotal = lifeTotal,
                    maxLife = maxLife,
                    imageResourceId = imageResourceId,
                    backgroundColor = backgroundColor,
                    backgroundImageIndex = backgroundImageIndex, // Передаем backgroundImageIndex
                    onLifeChange = { newValue, increased ->
                        val oldLife = lifeTotal.value
                        onLifeChange(newValue, increased)
                        val soundType = when {
                            newValue > oldLife -> SoundManager.SoundType.INCREASE
                            increased -> SoundManager.SoundType.DECREASE
                            else -> SoundManager.SoundType.RESET
                        }
                        soundManager.playSound(soundType)
                    },
                )
            }
        }
    }
}


fun createMediaPlayer(context: Context, resourceId: Int): MediaPlayer? {
    return try {
        MediaPlayer.create(context, resourceId).apply {
            setOnErrorListener { mp, what, extra ->
                Log.e("MediaPlayer", "Error: $what, extra: $extra")
                false //возвращаем false, чтобы не прерывать воспроизведение других звуков
            }
        }
    } catch (e: Exception) {
        Log.e("MediaPlayer", "Error creating MediaPlayer: ${e.message}")
        null
    }
}

fun playSound(
    increased: Boolean,
    isPositiveChange: Boolean,
    context: Context,
    vararg mediaPlayers: MediaPlayer?
) {
    val player = when {
        isPositiveChange -> mediaPlayers[1]
        increased -> mediaPlayers[0]
        else -> mediaPlayers[2]
    }

    player?.let {
        if (it.isPlaying) {
            it.release() // Освобождаем, если уже играет
            return // Прерываем выполнение, новый MediaPlayer не создаём
        }
        try {
            it.reset()
            it.setDataSource(
                context,
                Uri.parse(
                    "android.resource://" + context.packageName + "/" + getResourceId(
                        it,
                        mediaPlayers
                    )
                )
            )
            it.prepare()
            it.start()
            it.setOnCompletionListener { mp ->
                mp.release()
            }
        } catch (e: Exception) {
            Log.e("MediaPlayer", "Error playing sound: ${e.message}")
        }
    }
}


private fun getResourceId(player: MediaPlayer, mediaPlayers: Array<out MediaPlayer?>): Int {
    return when (player) {
        mediaPlayers[0] -> R.raw.hs
        mediaPlayers[1] -> R.raw.hil
        else -> R.raw.sword
    }
}


fun releaseMediaPlayer(mediaPlayer: MediaPlayer?) {
    mediaPlayer?.release()
}

@Composable
fun PlayerLifeCounter(
    lifeTotal: MutableState<Int>,
    maxLife: Int,
    imageResourceId: Int,
    backgroundColor: Color,
    onLifeChange: (Int, Boolean) -> Unit,
    backgroundImageIndex: Int // Используем backgroundImageIndex
) {
    val maxBrightness = 1f
    val minBrightness = 0.2f
    val imageBrightness = if (lifeTotal.value <= 25) {
        (minBrightness + (lifeTotal.value.toFloat() / 25f) * (maxBrightness - minBrightness)).coerceIn(
            minBrightness,
            maxBrightness
        )
    } else {
        maxBrightness
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = imageResourceId),
            contentDescription = stringResource(R.string.player_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .alpha(imageBrightness)
                .padding(bottom = 8.dp)
                .animateContentSize(animationSpec = tween(300))
        )
        Spacer(modifier = Modifier.height(8.dp)) // Отступ между картинкой и именем

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (backgroundColor.luminance() > 0.5f) Color.Black.copy(alpha = 0.2f) else Color.White.copy(
                            alpha = 0.2f
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {

            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // Отступ между именем и счетчиком ХП

        AnimatedNumberText(lifeTotal.value, backgroundColor)

        Spacer(modifier = Modifier.height(16.dp)) // Отступ между счетчиком ХП и кнопками

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround // Изменено на SpaceAround
        ) {
            AnimatedActionButton(
                onClick = { onLifeChange(maxOf(0, lifeTotal.value + 1), false) },
                icon = painterResource(id = android.R.drawable.arrow_up_float),
                contentDescription = "Your button description",
                neutralColor = Color(0x808B0000),
                activeColor = Color(0xFF8B0000),
                borderColor = Color.Black
            )
            AnimatedActionButton(
                onClick = { onLifeChange(maxOf(0, lifeTotal.value - 1), false) },
                icon = painterResource(id = android.R.drawable.arrow_down_float),
                contentDescription = "Your button description",
                neutralColor = Color(0x808B0000),
                activeColor = Color(0xFF8B0000),
                borderColor = Color.Black
            )
        }
    }
}


@Composable
fun ImagePickerDialog(
    imageResourceIds: List<Int>,
    onImageSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите стихию", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)) }, // Увеличенный размер текста
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // Два столбца
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(imageResourceIds.size) { index ->
                    ImageItem(imageResourceIds[index], index, onImageSelected, onDismiss)
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
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
    icon: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    neutralColor: Color = Color(0x808B0000), // Dark Crimson (with alpha)
    activeColor: Color = Color(0xFF8B0000), // Bright Crimson
    animationDuration: Int = 100,
    borderColor: Color = Color.White // Added border color parameter
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonColor by animateColorAsState(
        targetValue = if (isPressed) activeColor else neutralColor,
        animationSpec = tween(animationDuration)
    )
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(animationDuration)
    )

    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(70.dp)
            .scale(scale)
            .clip(CircleShape)
            .border(width = 2.dp, color = borderColor, shape = CircleShape), // Added border
        interactionSource = interactionSource,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = buttonColor,
            contentColor = Color.White
        )
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        )
    }
}


@Composable
fun AnimatedNumberText(number: Int, backgroundColor: Color) {
    val transition = updateTransition(targetState = number, label = "life")
    val animatedNumber by transition.animateFloat(label = "number") { it.toFloat() }

    // Determine text color based on background luminance
    val textColor = Color.White

    // Add a contrasting background for better visibility
    Box(modifier = Modifier.padding(4.dp)) { // Added padding for better spacing
        Box(
            modifier = Modifier
                .background(
                    color = textColor.copy(alpha = 0.2f), // Semi-transparent background color
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = animatedNumber.toInt().toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 100.sp,
                color = textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
