package com.example.zzzpicazzzas.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.zzzpicazzzas.R
import com.example.zzzpicazzzas.presentation.entity.VariantUi
import com.example.zzzpicazzzas.presentation.utils.OffsetSaver
import com.example.zzzpicazzzas.presentation.utils.fullScreenWidthPx
import com.example.zzzpicazzzas.presentation.utils.toPx
import kotlin.math.abs


private const val CENTER_IMAGE_SIZE_RATIO_TO_SCREEN_WIDTH_S = 0.522f
private const val CENTER_IMAGE_SIZE_RATIO_TO_SCREEN_WIDTH_M = 0.65f
private const val CENTER_IMAGE_SIZE_RATIO_TO_SCREEN_WIDTH_L = 0.73f

private const val SIDE_IMAGE_SIZE_RATIO_TO_CENTER_IMAGE_S_SIZE = 0.408f
private const val SIDE_IMAGE_SIZE_RATIO_TO_CENTER_IMAGE_M_SIZE = 0.327f
private const val SIDE_IMAGE_SIZE_RATIO_TO_CENTER_IMAGE_L_SIZE = 0.291f

private const val ORIGINAL_IMAGE_SIZE_SCALE = 2f

private const val FAR_LEFT_POSITION = -2f
private const val LEFT_POSITION = -1f
private const val CENTER_POSITION = 0f
private const val RIGHT_POSITION = 1f
private const val FAR_RIGHT_POSITION = 2f

private const val ANIM_SIZE_SPRING_DUMP_RATIO = 0.7f

@Composable
fun ImageCarousel(
    imageUrlList: List<String>,
    selectedUrlImage: String,
    centerImageCenterPoint: DpOffset,
    fullScreenWidthPx: Float,
    selectedImageSize: String,
    animZoomImage: Float,
    animScreenAlpha: Float,
    onImageClick: (imageUrl: String) -> Unit,
) {
    val (centerImageSize, sideImageSize) = getImageSizes(selectedImageSize)

    val animCenterImageSizeSelection by animateFloatAsState(
        targetValue = centerImageSize,
        animationSpec = spring(
            dampingRatio = ANIM_SIZE_SPRING_DUMP_RATIO,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "animCenterImageSize",
    )

    InternalImageCarousel(
        imageUrlList = imageUrlList,
        selectedUrlImage = selectedUrlImage,
        centerImageCenterPoint = centerImageCenterPoint,
        fullScreenWidthPx = fullScreenWidthPx,
        sideImageSize = sideImageSize,
        centerImageSize = animCenterImageSizeSelection,
        animScreenAlpha = animScreenAlpha,
        animZoomImage = animZoomImage,
        onImageClick = onImageClick,
    )
}

@Composable
private fun InternalImageCarousel(
    imageUrlList: List<String>,
    selectedUrlImage: String,
    centerImageCenterPoint: DpOffset,
    fullScreenWidthPx: Float,
    sideImageSize: Float,
    centerImageSize: Float,
    animScreenAlpha: Float,
    animZoomImage: Float,
    onImageClick: (String) -> Unit,
) {
    if (imageUrlList.isEmpty() || selectedUrlImage.isEmpty()) return

    val animSwitch = remember { Animatable(0f) }
    var visibleImageUrlListState by remember { mutableStateOf(imageUrlList.getVisibleImageUrlList(selectedUrlImage)) }
    val painterList = rememberPainterList(visibleImageUrlListState)
    val imageHolderListState: MutableState<List<ImageHolder>> = remember(visibleImageUrlListState) {
        mutableStateOf(visibleImageUrlListState.toImageHolderList())
    }
    val isAnimateRightDirection = remember(selectedUrlImage) {
        selectedUrlImage == visibleImageUrlListState[imageHolderListState.getCenterImageHolder().position + 1]
    }
    val gesturePan = rememberSaveable(stateSaver = OffsetSaver()) { mutableStateOf(Offset.Zero) }
    val transformableState = rememberTransformableState { _, _, panChange, _ -> gesturePan.value += panChange }

    val leftImageCenterPointPx = Offset(0f, centerImageCenterPoint.y.toPx())
    val centerImageCenterPointPx = Offset(centerImageCenterPoint.x.toPx(), centerImageCenterPoint.y.toPx())
    val rightImageCenterPointPx = Offset(fullScreenWidthPx, centerImageCenterPoint.y.toPx())

    val sideShiftToAnchor = Offset(sideImageSize / 2, sideImageSize / 2)
    val centerShiftToAnchor = Offset(centerImageSize / 2, centerImageSize / 2)

    val leftImageAnchorPoint = leftImageCenterPointPx - sideShiftToAnchor
    val centerImageAnchorPoint = centerImageCenterPointPx - centerShiftToAnchor
    val rightImageAnchorPoint = rightImageCenterPointPx - sideShiftToAnchor

    val leftImageRect = Rect(leftImageAnchorPoint, Size(sideImageSize, sideImageSize))
    val centerImageRect = Rect(centerImageAnchorPoint, Size(centerImageSize, centerImageSize))
    val rightImageRect = Rect(rightImageAnchorPoint, Size(sideImageSize, sideImageSize))


    if (animZoomImage == 0f) {
        gesturePan.value = Offset.Zero
    }
    LaunchedEffect(selectedUrlImage) {
        if (selectedUrlImage.isEmpty()) return@LaunchedEffect

        if (selectedUrlImage != imageHolderListState.getCenterImageHolder().imageUrl) {
            animSwitch.snapTo(0f)
            val animationResult = animSwitch.animateTo(
                targetValue = if (isAnimateRightDirection) -1f else 1f,
                animationSpec = spring(
                    dampingRatio = ANIM_SIZE_SPRING_DUMP_RATIO,
                    stiffness = Spring.StiffnessMedium,
                )
            )

            if (animationResult.endReason == AnimationEndReason.Finished) {
                val newIndex = imageHolderListState.getCenterImageHolder().position +
                    if (isAnimateRightDirection) 1 else -1

                visibleImageUrlListState = imageUrlList.getVisibleImageUrlList(visibleImageUrlListState[newIndex])
                imageHolderListState.value = visibleImageUrlListState.toImageHolderList()
                animSwitch.snapTo(0f)
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .alpha(animScreenAlpha)
            .then(
                if (animZoomImage != 0f) {
                    Modifier
                        .clickable { onImageClick(selectedUrlImage) }
                        .transformable(transformableState)
                } else {
                    Modifier.pointerInput(selectedUrlImage) {
                        handleClick(centerImageRect, imageHolderListState, onImageClick, leftImageRect, rightImageRect)
                    }
                }
            )
    ) {
        imageHolderListState.value.forEach { imageHolder ->
            drawImage(
                imageHolder = imageHolder,
                imageHolderListState = imageHolderListState,
                animSwitch = animSwitch,
                fullScreenWidthPx = fullScreenWidthPx,
                isAnimateRightDirection = isAnimateRightDirection,
                centerImageSize = centerImageSize,
                sideImageSize = sideImageSize,
                centerImageCenterPointPx = centerImageCenterPointPx,
                painterList = painterList,
                gesturePan = gesturePan,
                animZoomImage = animZoomImage,
            )
        }
    }

    if (animZoomImage == 0f) {
        val zoomSize = 190.dp
        Image(
            painter = painterResource(R.drawable.img_zoom),
            contentDescription = stringResource(R.string.detail_descriptionZoom),
            modifier = Modifier
                .size(zoomSize)
                .offset(x = centerImageCenterPoint.x - zoomSize / 2, y = centerImageCenterPoint.y - zoomSize / 2),
        )
    }
}

@Composable
private fun rememberPainterList(visibleImageUrlList: List<String>): List<AsyncImagePainter> = visibleImageUrlList.map {
    rememberAsyncImagePainter(
        model = it,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun getImageSizes(size: String): Pair<Float, Float> {
    val ratioToScreenWidth = when(size) {
        VariantUi.SIZE_SMALL -> CENTER_IMAGE_SIZE_RATIO_TO_SCREEN_WIDTH_S
        VariantUi.SIZE_MEDIUM -> CENTER_IMAGE_SIZE_RATIO_TO_SCREEN_WIDTH_M
        // VariantUi.SIZE_LARGE
        else -> CENTER_IMAGE_SIZE_RATIO_TO_SCREEN_WIDTH_L
    }
    val ratioToCenterImage = when(size) {
        VariantUi.SIZE_SMALL -> SIDE_IMAGE_SIZE_RATIO_TO_CENTER_IMAGE_S_SIZE
        VariantUi.SIZE_MEDIUM -> SIDE_IMAGE_SIZE_RATIO_TO_CENTER_IMAGE_M_SIZE
        // VariantUi.SIZE_LARGE
        else -> SIDE_IMAGE_SIZE_RATIO_TO_CENTER_IMAGE_L_SIZE
    }
    val centerImageSize = fullScreenWidthPx() * ratioToScreenWidth
    val sideImageSize = centerImageSize * ratioToCenterImage

    return Pair(centerImageSize, sideImageSize)
}

private fun DrawScope.drawImage(
    imageHolder: ImageHolder,
    imageHolderListState: MutableState<List<ImageHolder>>,
    animSwitch: Animatable<Float, AnimationVector1D>,
    fullScreenWidthPx: Float,
    isAnimateRightDirection: Boolean,
    centerImageSize: Float,
    sideImageSize: Float,
    centerImageCenterPointPx: Offset,
    painterList: List<AsyncImagePainter>,
    animZoomImage: Float,
    gesturePan: MutableState<Offset>,
) {
    val isCenterHolder = imageHolder == imageHolderListState.getCenterImageHolder()
    val isLeftHolder = imageHolder == imageHolderListState.getLeftImageHolder()
    val isRightHolder = imageHolder == imageHolderListState.getRightImageHolder()

    val imagePainter = painterList[imageHolder.painterId]

    val animatableShift = animSwitch.value * fullScreenWidthPx / 2
    val ratioSideImageToCenterImage = sideImageSize / centerImageSize
    val animatableScale = if (isAnimateRightDirection) {
        when {
            isCenterHolder -> (1f - abs(animSwitch.value)) * (1f - ratioSideImageToCenterImage) +
                ratioSideImageToCenterImage
            isRightHolder -> abs(animSwitch.value) * (1f / ratioSideImageToCenterImage - 1f) + 1f
            // isLeftHolder, isFarLeftHolder, isFarRightHolder
            else -> 1f
        }
    } else {
        when {
            isCenterHolder -> (1f - abs(animSwitch.value)) * (1f - ratioSideImageToCenterImage) +
                ratioSideImageToCenterImage
            isLeftHolder -> abs(animSwitch.value) * (1f / ratioSideImageToCenterImage - 1f) + 1f
            // isRightHolder, isFarLeftHolder, isFarRightHolder
            else -> 1f
        }
    }

    val normalizePositionInViewer = when {
        imageHolder.normalizedOffset <= -1 -> -1f
        imageHolder.normalizedOffset >= 1f -> 1f
        else -> imageHolder.normalizedOffset
    }
    val startScale = (1f - abs(normalizePositionInViewer)) *
        (1f - ratioSideImageToCenterImage) + ratioSideImageToCenterImage
    val startPositionShift = when {
        isCenterHolder -> fullScreenWidthPx / 2 * imageHolder.normalizedOffset
        isLeftHolder -> fullScreenWidthPx / 2 * imageHolder.normalizedOffset - fullScreenWidthPx / 2 * animZoomImage
        // isRightHolder
        else -> fullScreenWidthPx / 2 * imageHolder.normalizedOffset + fullScreenWidthPx / 2 * animZoomImage
    }

    val imageSize = if (animZoomImage != 0f && isCenterHolder) {
        when (imagePainter.state.value) {
            is AsyncImagePainter.State.Success -> {
                val targetSize = imagePainter.intrinsicSize.width * ORIGINAL_IMAGE_SIZE_SCALE
                centerImageSize + (targetSize - centerImageSize) * animZoomImage
            }
            else -> Size.Unspecified.width
        }
    } else {
        centerImageSize * startScale * animatableScale
    }
    val imageCenter = Offset(
        x = centerImageCenterPointPx.x + startPositionShift + animatableShift,
        y = centerImageCenterPointPx.y,
    )
    val imageShiftToAnchor = Offset(imageSize / 2, imageSize / 2)
    val imageAnchorPoint = imageCenter - imageShiftToAnchor
    val imageAnchorPointWithPan = Offset(
        x = imageAnchorPoint.x + gesturePan.value.x * animZoomImage,
        y = imageAnchorPoint.y + gesturePan.value.y * animZoomImage,
    )

    if (animZoomImage != 0f && imageSize == Size.Unspecified.width) return

    drawImageIfNeeded(imageHolder.imageUrl, imageAnchorPointWithPan, imagePainter, imageSize)
}

private fun DrawScope.drawImageIfNeeded(
    imageUrl: String,
    imageAnchorPoint: Offset,
    painter: AsyncImagePainter,
    imageSize: Float,
) {
    if (!imageUrl.isEmpty()) {
        translate(imageAnchorPoint.x, imageAnchorPoint.y) {
            with(painter) {
                draw(Size(imageSize, imageSize))
            }
        }
    }
}

private fun List<String>.getVisibleImageUrlList(url: String): List<String> {
    val urlIndex = indexOf(url)

    val farLeftImageUrl = if (urlIndex - 2 >= 0) get(urlIndex - 2) else ""
    val leftImageUrl = if (urlIndex - 1 >= 0) get(urlIndex - 1) else ""
    val centerImageUrl = if (urlIndex != -1) url else ""
    val rightImageUrl = if (urlIndex + 1 < size) get(urlIndex + 1) else ""
    val farRightImageUrl = if (urlIndex + 2 < size) get(urlIndex + 2) else ""

    return listOf(farLeftImageUrl, leftImageUrl, centerImageUrl, rightImageUrl, farRightImageUrl)
}

private suspend fun PointerInputScope.handleClick(
    centerImageRect: Rect,
    imageHolderListState: MutableState<List<ImageHolder>>,
    onImageClick: (String) -> Unit,
    leftImageRect: Rect,
    rightImageRect: Rect,
) {
    fun performClick(
        imageHolderListState: MutableState<List<ImageHolder>>,
        positionInViewer: Float,
        onImageClick: (String) -> Unit,
    ) {
        imageHolderListState.value.forEach {
            if (it.normalizedOffset == positionInViewer) {
                onImageClick(it.imageUrl)
            }
        }
    }

    detectTapGestures { tapOffset ->
        when {
            centerImageRect.contains(tapOffset) -> performClick(imageHolderListState, CENTER_POSITION, onImageClick)
            leftImageRect.contains(tapOffset) -> performClick(imageHolderListState, LEFT_POSITION, onImageClick)
            rightImageRect.contains(tapOffset) -> performClick(imageHolderListState, RIGHT_POSITION, onImageClick)
        }
    }
}

private fun List<String>.toImageHolderList(): List<ImageHolder> = mapIndexed { index, imageUrl ->
    ImageHolder(
        painterId = index,
        position = index,
        imageUrl = imageUrl,
        normalizedOffset = when (index) {
            0 -> FAR_LEFT_POSITION
            1 -> LEFT_POSITION
            2 -> CENTER_POSITION
            3 -> RIGHT_POSITION
            // index == 4
            else -> FAR_RIGHT_POSITION
        },
    )
}

private fun MutableState<List<ImageHolder>>.getCenterImageHolder() =
    value.first { it.normalizedOffset == CENTER_POSITION }

private fun MutableState<List<ImageHolder>>.getLeftImageHolder() =
    value.first { it.normalizedOffset == LEFT_POSITION }

private fun MutableState<List<ImageHolder>>.getRightImageHolder() =
    value.first { it.normalizedOffset == RIGHT_POSITION }

private data class ImageHolder(
    val painterId: Int,
    val imageUrl: String,
    val position: Int,
    // visible in viewer value from = -1f to = 1f
    val normalizedOffset: Float,
)
