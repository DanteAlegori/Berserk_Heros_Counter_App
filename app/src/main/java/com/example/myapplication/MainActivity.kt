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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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


    Scaffold(modifier = Modifier.fillMaxSize(), contentColor = Color.White) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF202020), Color(0xFF242424))
                ))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(2.dp), // Уменьшите значение, например, 2.dp или 4.dp
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(350.dp)
                    .padding(8.dp)
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)), // Добавлена обводка
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Убрано elevation
                ) {
                    PlayerLifeCounterCard(
                        playerName = "Игрок 2",
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
                        backgroundImageIndex = 0,
                        gradientColors = gradientColors
                    )
                }



                IconButton(
                    onClick = {
                        player1Life.value = 25
                        player2Life.value = 25
                    },
                    modifier = Modifier.fillMaxWidth(0.5f),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = berserkBloodRed) // Сохраняем цвет
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_rotate), // Замените на ID вашего значка
                        contentDescription = "Сбросить жизни", // Не забудьте contentDescription
                        tint = Color.White // Цвет значка
                    )
                }

               

                Card(modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(350.dp)
                    .padding(8.dp)
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)), // Добавлена обводка
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Убрано elevation
                ) {
                    PlayerLifeCounterCard(
                        playerName = "Игрок 1",
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
                        backgroundImageIndex = 1,
                        gradientColors = gradientColors
                    )
                }
            }
        }
    }
}




@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerLifeCounterCard(
    playerName: String,
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
    cardHeight: Dp = 300.dp,
    backgroundImageIndex: Int = 0,

) {

    var showDialog by remember { mutableStateOf(false) }
    var selectedImageIndex by remember { mutableStateOf(backgroundImageIndex) }


    val imageResourceId by remember(selectedImageIndex) {
        derivedStateOf { imageResourceIds[selectedImageIndex % imageResourceIds.size] }
    }
    val backgroundColor by remember(selectedImageIndex) {
        derivedStateOf { backgroundColors[selectedImageIndex % backgroundColors.size] }
    }
    val scale by animateFloatAsState(targetValue = if (showDialog) 0.95f else 1f, animationSpec = tween(300))

    val backgroundImageId = backgroundImageIds[selectedImageIndex % backgroundImageIds.size]
    val soundManager = remember { SoundManager(context) }
    DisposableEffect(Unit) {
        onDispose {
            soundManager.release()
        }
    }

    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(initialOffsetX = { if (rotate) -it else it }) + fadeIn(animationSpec = tween(500)),
        exit = slideOutHorizontally() + fadeOut(animationSpec = tween(500))
    ) {
        Card(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .then(if (rotate) Modifier.rotate(180f) else Modifier)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(gradientColors)) // Use passed gradientColors
            ) {
                Image(
                    painter = painterResource(id = backgroundImageId),
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            PlayerLifeCounter(
                playerName = playerName,
                lifeTotal = lifeTotal,
                maxLife = maxLife,
                imageResourceId = imageResourceId,
                onImagePick = { index -> selectedImageIndex = index },
                onShowDialogChange = { showDialog = it },
                showDialog = showDialog,
                berserkGold = berserkGold,
                berserkBloodRed = berserkBloodRed,
                berserkDarkGrey = berserkDarkGrey,
                imageResourceIds = imageResourceIds,
                backgroundColor = backgroundColor,

                onLifeChange = { newValue, increased ->
                    val oldLife = lifeTotal.value
                    onLifeChange(newValue, increased)
                    val soundType = when {
                        newValue > oldLife -> SoundManager.SoundType.INCREASE
                        increased -> SoundManager.SoundType.DECREASE
                        else -> SoundManager.SoundType.RESET
                    }
                    soundManager.playSound(soundType)
                }
            )


        }
    }
}}
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

fun playSound(increased: Boolean, isPositiveChange: Boolean, context: Context, vararg mediaPlayers: MediaPlayer?) {
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
            it.setDataSource(context, Uri.parse("android.resource://" + context.packageName + "/" + getResourceId(it, mediaPlayers)))
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
    playerName: String,
    lifeTotal: MutableState<Int>,
    maxLife: Int,
    imageResourceId: Int,
    berserkGold: Color,
    berserkBloodRed: Color,
    berserkDarkGrey: Color,
    onLifeChange: (Int, Boolean) -> Unit,
    imageResourceIds: List<Int>,
    onImagePick: (Int) -> Unit,
    onShowDialogChange: (Boolean) -> Unit,
    showDialog: Boolean,
    backgroundColor: Color
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
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            Image(
                painter = painterResource(id = imageResourceId),
                contentDescription = stringResource(R.string.player_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp) // Установите желаемый размер (например, 100.dp x 100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .alpha(imageBrightness)
                    .padding(bottom = 8.dp)
                    .animateContentSize(animationSpec = tween(300))
            )

        }
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .background(
                            color = if (backgroundColor.luminance() > 0.5f) Color.Black.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = playerName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AnimatedActionButton(
                    onClick = { onLifeChange(maxOf(0, lifeTotal.value - 1), false) },
                    icon = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = stringResource(R.string.decrease),
                    berserkGold = berserkGold,
                )
                AnimatedNumberText(lifeTotal.value, backgroundColor)
                AnimatedActionButton(
                    onClick = { onLifeChange(minOf(maxLife, lifeTotal.value + 1), true) },
                    icon = painterResource(id = android.R.drawable.arrow_up_float),
                    contentDescription = stringResource(R.string.increase),
                    berserkGold = berserkGold,
                )
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                IconButton(
                    onClick = { onShowDialogChange(true) },
                    modifier = Modifier.size(50.dp), // Reduced button size
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = berserkBloodRed
                    )
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_dialog_dialer),
                        contentDescription = "Выберите стихию",
                        tint = Color.White
                    )
                }
            }
        }
        if (showDialog) {
            ImagePickerDialog(imageResourceIds, onImagePick) { onShowDialogChange(false) }
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
        title = { Text("Выберите стихию") },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    imageResourceIds.subList(0, 3).forEachIndexed { index, resourceId ->
                        ImageItem(resourceId, index, onImageSelected, onDismiss)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    imageResourceIds.subList(3, 6).forEachIndexed { index, resourceId ->
                        ImageItem(resourceId, index + 3, onImageSelected, onDismiss)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Выбор сделан")
            }
        }
    )
}

@Composable
fun ImageItem(resourceId: Int, index: Int, onImageSelected: (Int) -> Unit, onDismiss: () -> Unit) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = null,
        modifier = Modifier
            .size(50.dp) // Уменьшили размер картинки
            .clickable {
                onImageSelected(index)
                onDismiss()
            }
            .shadow(4.dp, CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun AnimatedActionButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.painter.Painter,
    contentDescription: String,
    berserkGold: Color
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonColor = if (isPressed) berserkGold.copy(alpha = 0.8f) else berserkGold
    val scale by animateFloatAsState(targetValue = if (isPressed) 1.1f else 1f, animationSpec = tween(100))

    IconButton(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .size(40.dp)
            .scale(scale),
        colors = IconButtonDefaults.iconButtonColors(containerColor = buttonColor)
    ) {
        Icon(icon, contentDescription = contentDescription, tint = Color.White)
    }
}


@Composable
fun AnimatedNumberText(number: Int, backgroundColor: Color) {
    val transition = updateTransition(targetState = number, label = "life")
    val animatedNumber by transition.animateFloat(label = "number") { it.toFloat() }

    // Determine text color based on background luminance
    val textColor =  Color.White

    // Add a contrasting background for better visibility
    Box(modifier = Modifier.padding(4.dp)) { // Added padding for better spacing
        Box(
            modifier = Modifier
                .background(
                    color = textColor.copy(alpha = 0.2f), // Semi-transparent background color
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ){
            Text(
                text = animatedNumber.toInt().toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 48.sp, // Increased font size
                color = textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center

            )
        }
    }
}

