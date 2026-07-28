package com.example.zzzpicazzzas.presentation.screen.detail

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.zzzpicazzzas.R
import com.example.zzzpicazzzas.presentation.components.ImageCarousel
import com.example.zzzpicazzzas.presentation.components.StatusBarSpace
import com.example.zzzpicazzzas.presentation.entity.PizzaUi
import com.example.zzzpicazzzas.presentation.entity.VariantUi
import com.example.zzzpicazzzas.presentation.screen.detail.DetailEvent.OnMinusClicked
import com.example.zzzpicazzzas.presentation.screen.detail.DetailEvent.OnPizzaSelected
import com.example.zzzpicazzzas.presentation.screen.detail.DetailEvent.OnPlusClicked
import com.example.zzzpicazzzas.presentation.screen.detail.DetailEvent.OnSizeClicked
import com.example.zzzpicazzzas.presentation.theme.ActiveColor
import com.example.zzzpicazzzas.presentation.theme.DarkBgColor
import com.example.zzzpicazzzas.presentation.theme.HighlightColor
import com.example.zzzpicazzzas.presentation.theme.TextAndIconColor
import com.example.zzzpicazzzas.presentation.theme.White
import com.example.zzzpicazzzas.presentation.utils.createShadow
import com.example.zzzpicazzzas.presentation.utils.distributePoints
import com.example.zzzpicazzzas.presentation.utils.fullScreenHeightPx
import com.example.zzzpicazzzas.presentation.utils.fullScreenSizeDpSize
import com.example.zzzpicazzzas.presentation.utils.fullScreenWidthDp
import com.example.zzzpicazzzas.presentation.utils.fullScreenWidthPx
import com.example.zzzpicazzzas.presentation.utils.letterSpacingPercentToSp
import com.example.zzzpicazzzas.presentation.utils.toDp
import com.example.zzzpicazzzas.presentation.utils.toPx
import kotlin.math.sqrt

private const val RADIUS_RATIO_TO_SCREEN_HEIGHT = 0.37f
private const val CENTER_POINT_RATIO_TO_SCREEN_HEIGHT = 0.27f
private const val ANIM_SCREEN_RADIUS_SPRING_DUMP_RATIO = 0.7f
private const val ANIM_SIZE_SPRING_DUMP_RATIO = 0.7f
private const val ANIM_TITLE_ANIMATION_DURATION_MILLIS = 300

@Composable
fun DetailScreen() {
    val viewModel = hiltViewModel<DetailViewModel>()
    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current
    val internalLoadingExceptionMessage = stringResource(R.string.detail_internalExceptionMessage)

    LaunchedEffect(Unit) {
        viewModel.labelFlow.collect { label ->
            when (label) {
                is DetailLabel.InternalLoadingException -> Toast.makeText(
                    context,
                    internalLoadingExceptionMessage,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    DetailContent(
        state = state,
        onPlusClicked = { viewModel.onEvent(OnPlusClicked) },
        onMinusClicked = { viewModel.onEvent(OnMinusClicked) },
        onSizeClicked = { viewModel.onEvent(OnSizeClicked(it)) },
        onPizzaSelected = { viewModel.onEvent(OnPizzaSelected(it)) },
    )
}

@Composable
fun DetailContent(
    state: DetailUiState,
    onPlusClicked: () -> Unit,
    onMinusClicked: () -> Unit,
    onSizeClicked: (String) -> Unit,
    onPizzaSelected: (pizzaUrl: String) -> Unit,
) {
    if (state.pizzaList.isEmpty()) return

    var isScreenShown by remember { mutableStateOf(false) }
    val animScreenShowing by animateFloatAsState(
        targetValue = if (isScreenShown) 1f else 0f,
        animationSpec = spring(
            dampingRatio = ANIM_SCREEN_RADIUS_SPRING_DUMP_RATIO,
            stiffness = Spring.StiffnessLow,
        ),
        label = "animScreenShowing",
    )
    val animZoomPizza by animateFloatAsState(
        targetValue = if (state.isZoomMode) 1f else 0f,
        animationSpec = spring(
            dampingRatio = ANIM_SIZE_SPRING_DUMP_RATIO,
            stiffness = Spring.StiffnessLow,
        ),
        label = "animZoomPizza",
    )

    val selectedPizza = state.getSelectedPizza()
    val selectedPizzaSize = state.getSelectedPizza().variants.first { it.isSelected }.size
    val context = LocalContext.current
    val messageBack = stringResource(R.string.detail_messageBack)
    val messageFavorite = stringResource(R.string.detail_messageFavorite)

    LaunchedEffect(Unit) { isScreenShown = true }

    DetailBackground(animScreenShowing)
    ImageCarousel(
        imageUrlList = state.pizzaList.map { it.imageUrl },
        selectedUrlImage = state.getSelectedPizza().imageUrl,
        centerImageCenterPoint = getCenterImageCenterPoint(),
        onImageClick = onPizzaSelected,
        fullScreenWidthPx = fullScreenWidthPx(),
        animZoomImage = animZoomPizza,
        animScreenAlpha = animScreenShowing,
        selectedImageSize = selectedPizzaSize,
    )
    Header(
        title = state.title,
        onBackClick = { Toast.makeText(context, messageBack, Toast.LENGTH_LONG).show() },
        onFavoriteClick = { Toast.makeText(context, messageFavorite, Toast.LENGTH_LONG).show() },
        animShift = animScreenShowing,
        animZoomPizza = animZoomPizza,
    )
    SizePicker(
        variants = selectedPizza.variants,
        onSizeClicked = onSizeClicked,
        animScreenShift = animScreenShowing,
        animZoomPizza = animZoomPizza,
    )
    BottomContent(
        state = state,
        onPlusClicked = onPlusClicked,
        onMinusClicked = onMinusClicked,
        animScreenShift = animScreenShowing,
        animZoomPizza = animZoomPizza,
    )
}

@Composable
fun BottomContent(
    state: DetailUiState,
    onPlusClicked: () -> Unit,
    onMinusClicked: () -> Unit,
    animScreenShift: Float,
    animZoomPizza: Float,
) {
    val screenSize = fullScreenSizeDpSize()
    val (radiusPx, circleCenterOffsetPx) = getBackgroundRadiusAndCenterPx()
    val screenWidthPx = fullScreenWidthPx()
    val animScreenRadiusPx = radiusPx * (2f - animScreenShift)
    val animZoomRadiusPx = radiusPx * 2 * animZoomPizza
    val animSumRadiusPx = animScreenRadiusPx + animZoomRadiusPx
    val xCenterPx = screenWidthPx * 0.5f
    val animYTopPx = upperCircleYPx(xCenterPx, circleCenterOffsetPx.x, circleCenterOffsetPx.y, animSumRadiusPx)
    val yTopPx = upperCircleYPx(xCenterPx, circleCenterOffsetPx.x, circleCenterOffsetPx.y, radiusPx)
    val animYTopDp = animYTopPx.toDp()
    val yTopDp = yTopPx.toDp()
    val height = screenSize.height - yTopDp

    Column(
        modifier = Modifier
            .offset(0.dp, animYTopDp)
            .fillMaxWidth(1f)
            .height(height)
            .padding(top = 45.dp, bottom = 57.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = state.getSelectedPizza().description,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            color = ActiveColor,
            modifier = Modifier
                .padding(start = 40.dp, end = 21.dp),
        )
        AddPizza(
            pizzaCount = state.pizzaCount,
            price = state.fullPrice,
            onPlusClicked = onPlusClicked,
            onMinusClicked = onMinusClicked,
        )
    }
}

@Composable
fun AddPizza(
    pizzaCount: Int,
    price: Float,
    onPlusClicked: () -> Unit,
    onMinusClicked: () -> Unit,
) {
    val context = LocalContext.current
    val messageAdded = stringResource(R.string.detail_messageAdded)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(horizontal = 24.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(HighlightColor, CircleShape)
                .size(143.dp, 48.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_minus),
                contentDescription = stringResource(R.string.detail_descriptionDecrease),
                modifier = Modifier
                    .dropShadow(
                        shape = CircleShape,
                        shadow = createShadow(),
                    )
                    .clip(CircleShape)
                    .clickable(onClick = onPlusClicked),
            )
            Text(
                text = pizzaCount.toString(),
                color = ActiveColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Image(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = stringResource(R.string.detail_descriptionDecrease),
                modifier = Modifier
                    .dropShadow(
                        shape = CircleShape,
                        shadow = createShadow(),
                    )
                    .clip(CircleShape)
                    .clickable(onClick = onMinusClicked),
            )
        }
        Text(
            text = stringResource(R.string.detail_priceFormat, price),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ActiveColor,
        )
        Button(
            onClick = {
                Toast.makeText(context, messageAdded, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .sizeIn(83.dp, 48.dp),
        ) {
            Text(
                text = stringResource(R.string.detail_Add),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = White,
            )
        }
    }
}

@Composable
private fun SizePicker(
    variants: List<VariantUi>,
    onSizeClicked: (String) -> Unit,
    animScreenShift: Float,
    animZoomPizza: Float,
) {
    val distribution = distributePoints(variants.size)
    val screenWidthPx = fullScreenWidthPx()
    val (radiusPx, circleCenterOffsetPx) = getBackgroundRadiusAndCenterPx()
    val animScreenRadiusPx = radiusPx * (2f - animScreenShift)
    val animZoomRadiusPx = radiusPx * 2 * animZoomPizza
    val animSumRadiusPx = animScreenRadiusPx + animZoomRadiusPx

    Banana(screenWidthPx, animSumRadiusPx, circleCenterOffsetPx)

    variants.onEachIndexed { index, variant ->
        val xCenterPx = screenWidthPx * distribution[index]
        val yCenterPx = upperCircleYPx(xCenterPx, circleCenterOffsetPx.x, circleCenterOffsetPx.y, animSumRadiusPx)
        val xCenterDp = xCenterPx.toDp()
        val yCenterDp = yCenterPx.toDp()
        val isSelected = variant.isSelected
        val sizeSelected = 52.dp
        val sizeDeselected = 48.dp

        BoxXY(
            width = if (isSelected) sizeSelected else sizeDeselected,
            height = if (isSelected) sizeSelected else sizeDeselected,
            xCenterDp = xCenterDp,
            yCenterDp = yCenterDp,
            modifier = Modifier
                .run {
                    if (isSelected) {
                        clip(CircleShape)
                            .background(White)
                            .padding(2.dp)
                    } else {
                        dropShadow(
                            shape = CircleShape,
                            shadow = createShadow(),
                        )
                    }
                }
                .background(if (isSelected) ActiveColor else White, CircleShape)
                .clip(CircleShape)
                .clickable { onSizeClicked(variant.size) }
        ) {
            val fontSize = 18.sp
            Text(
                text = variant.size,
                fontSize = fontSize,
                color = if (isSelected) DarkBgColor else TextAndIconColor,
                letterSpacing = letterSpacingPercentToSp(0.02f, fontSize),
            )
        }
    }
}

@Composable
private fun Banana(
    screenWidthPx: Float,
    radiusPx: Float,
    circleCenterOffsetPx: Offset,
) {
    val xCenterPx = screenWidthPx * 0.5f
    val bananaRadiusPx = radiusPx * 0.857f
    val yCenterPx = upperCircleYPx(xCenterPx, circleCenterOffsetPx.x, circleCenterOffsetPx.y, bananaRadiusPx)
    val xCenterDp = xCenterPx.toDp()
    val yCenterDp = yCenterPx.toDp()

    BoxXY(
        width = 97.dp,
        height = 63.dp,
        xCenterDp = xCenterDp,
        yCenterDp = yCenterDp,
    ) {
        Image(
            painter = painterResource(R.drawable.img_banana),
            contentDescription = stringResource(R.string.detail_descriptionBanana)
        )
    }
}

@Composable
fun Header(
    title: String,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    animShift: Float,
    animZoomPizza: Float,
) {
    val size = 48.dp
    val animScreenOffsetXY = size * 2 * (1f - animShift)
    val animZoomOffsetXY = 260.dp * animZoomPizza
    val animSumOffsetXY = animScreenOffsetXY + animZoomOffsetXY

    Column {
        StatusBarSpace()
        Spacer(
            modifier = Modifier.height(18.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 24.dp, top = 36.dp, end = 24.dp),
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                contentDescription = stringResource(R.string.detail_descriptionBack),
                modifier = Modifier
                    .offset(x = -animSumOffsetXY)
                    .size(size)
                    .dropShadow(
                        shape = CircleShape,
                        shadow = createShadow(),
                    )
                    .clip(CircleShape)
                    .clickable(onClick = onBackClick),
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val titleFontSize = 24.sp
                Text(
                    text = stringResource(R.string.detail_titleSmall),
                    fontSize = 10.sp,
                    color = TextAndIconColor,
                    modifier = Modifier
                        .offset(y = -animSumOffsetXY),
                )
                AnimatedContent(
                    targetState = title,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(ANIM_TITLE_ANIMATION_DURATION_MILLIS)) togetherWith
                            fadeOut(animationSpec = tween(ANIM_TITLE_ANIMATION_DURATION_MILLIS))
                    },
                    label = stringResource(R.string.detail_labelTitleAnimation),
                    modifier = Modifier
                        .offset(y = -animSumOffsetXY),
                ) { targetText ->
                    Text(
                        text = targetText,
                        fontSize = titleFontSize,
                        lineHeight = titleFontSize,
                        letterSpacing = letterSpacingPercentToSp(0.02f, titleFontSize),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_favorite),
                contentDescription = stringResource(R.string.detail_descriptionFavorite),
                modifier = Modifier
                    .offset(x = animSumOffsetXY)
                    .size(size)
                    .dropShadow(
                        shape = CircleShape,
                        shadow = createShadow(),
                    )
                    .clip(CircleShape)
                    .clickable(onClick = onFavoriteClick),
            )
        }
    }
}

@Composable
private fun DetailBackground(animScreenRadiusScale: Float) {
    val (radiusPx, circleCenterOffsetPx) = getBackgroundRadiusAndCenterPx()
    Canvas(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(White),
    ) {
        drawCircle(
            color = HighlightColor,
            radius = radiusPx * (2f - animScreenRadiusScale),
            center = circleCenterOffsetPx,
        )
    }
}

@Composable
private fun BoxXY(
    width: Dp,
    height: Dp,
    xCenterDp: Dp,
    yCenterDp: Dp,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = { },
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width, height)
            .offset(
                x = xCenterDp - width / 2,
                y = yCenterDp - height / 2,
            )
            .then(modifier),
        content = content,
    )
}

@Composable
private fun getCenterImageCenterPoint(): DpOffset {
    val (_, circleCenterOffsetPx) = getBackgroundRadiusAndCenterPx()
    val radiusPx = 65.dp.toPx()
    val x = fullScreenWidthDp().toPx() / 2
    val y = upperCircleYPx(x, circleCenterOffsetPx.x, circleCenterOffsetPx.y, radiusPx)

    return DpOffset(x.toDp(), y.toDp())
}

@Composable
private fun getBackgroundRadiusAndCenterPx(): Pair<Float, Offset> {
    val widthPx = fullScreenWidthPx()
    val heightPx = fullScreenHeightPx()

    val radius = heightPx * RADIUS_RATIO_TO_SCREEN_HEIGHT
    val circleCenter = Offset(
        x = widthPx / 2,
        y = heightPx * CENTER_POINT_RATIO_TO_SCREEN_HEIGHT,
    )

    return Pair(radius, circleCenter)
}

private fun upperCircleYPx(x: Float, centerX: Float, centerY: Float, radius: Float): Float {
    val dx = x - centerX
    val underRoot = radius * radius - dx * dx

    return centerY + sqrt(underRoot)
}

@Preview
@Composable
fun Preview() {
    DetailContent(
        state = DetailUiState(
            title = "Midnight Harvest",
            selectedPizzaId = "1",
            pizzaList = listOf(
                PizzaUi(
                    id = "1",
                    name = "Midnight Harvest",
                    imageUrl = "https://oursongapp.com/images/pizzas/pizza_midnight_harvest.png",
                    description = "This pizza celebrates the rich and bold flavors of black olives paired with a medley of cheeses. The deep, earthy taste of black olives harmonizes beautifully with the creamy, melted cheeses.",
                    variants = listOf(
                        VariantUi(
                            size = VariantUi.SIZE_SMALL,
                            isSelected = true,
                        ),
                        VariantUi(
                            size = VariantUi.SIZE_MEDIUM,
                            isSelected = false,
                        ),
                        VariantUi(
                            size = VariantUi.SIZE_LARGE,
                            isSelected = false,
                        )
                    ),
                )
            ),
            pizzaCount = 2,
            fullPrice = 33.333f,
        ),
        onPlusClicked = { },
        onMinusClicked = { },
        onSizeClicked = { },
        onPizzaSelected = { },
    )
}
