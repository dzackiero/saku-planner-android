package com.pnj.saku_planner.kakeibo.presentation.screens.scan

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArtTrack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.pnj.saku_planner.R
import com.pnj.saku_planner.kakeibo.presentation.components.LoadingScreen
import com.pnj.saku_planner.kakeibo.presentation.components.Permission
import com.pnj.saku_planner.kakeibo.presentation.screens.scan.viewmodels.ScanViewModel
import com.pnj.saku_planner.kakeibo.presentation.components.ui.CameraPreview
import com.pnj.saku_planner.kakeibo.presentation.components.ui.InvalidImageAlertDialog
import com.pnj.saku_planner.kakeibo.presentation.components.ui.executor
import com.pnj.saku_planner.kakeibo.presentation.components.ui.getCameraProvider
import com.pnj.saku_planner.kakeibo.presentation.components.ui.deleteTempFile
import com.pnj.saku_planner.kakeibo.presentation.components.ui.createCustomTempFile
import com.pnj.saku_planner.kakeibo.presentation.components.ui.uriToFile
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    scanViewModel: ScanViewModel,
    navigateToSummary: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isCameraReady by remember { mutableStateOf(false) }
    val emptyImageUri = "/dev/null".toUri()
    var imageUri by remember { mutableStateOf(emptyImageUri) }

    var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
    val imageCaptureUseCase by remember {
        mutableStateOf(
            ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()
        )
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var isLoading = false

    var showInvalidImageDialog by remember { mutableStateOf(false) }

    val errorMsg by scanViewModel.errorMsg.collectAsState()
    val totalPrice by scanViewModel.totalPrice.collectAsState()

    LaunchedEffect(totalPrice, errorMsg) {
        if (totalPrice != null && totalPrice != "") {
            navigateToSummary()
        }
        if (totalPrice == null && errorMsg == null) {
            showInvalidImageDialog = true
        }
    }

    val launcherGallery = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    )
    { uri: Uri? ->
        if (uri != null) {
            Timber.tag("Uri: ").d(uri.toString())
            val file = uriToFile(context, uri)
            if (file != null) {
                scanViewModel.loadItems(file)
            }
        } else {
            isLoading = false
            Timber.tag("Photo Picker").d("No media selected")
        }
    }

    Permission(
        permission = Manifest.permission.CAMERA,
        text = stringResource(R.string.camera_permission),
        permissionNotAvailableContent = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Text(
                    text = context.getString(R.string.camera_permission_rejected),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        })
                    }) {
                    Text(
                        text = stringResource(R.string.open_settings),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    ) {

        Box(modifier = modifier) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onUseCase = {
                    previewUseCase = it
                },
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 80.dp)
                    .fillMaxWidth()
            ) {
                val flashAnimation by rememberInfiniteTransition(label = "").animateFloat(
                    initialValue = 0.6f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        tween(5000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )

                val beatAnimation by rememberInfiniteTransition(label = "").animateFloat(
                    initialValue = 0.8f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        tween(5000),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )

                Text(
                    text = context.getString(R.string.lighting_warning),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(flashAnimation),
                    modifier = Modifier.scale(beatAnimation)
                )
            }

            Button(
                onClick = {
                    launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    isLoading = true
                },
                enabled = !isLoading,
                shape = CircleShape,
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .padding(vertical = 90.dp)
                    .padding(start = 40.dp, end = 8.dp)
                    .align(Alignment.BottomStart),
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArtTrack,
                    contentDescription = "Open Gallery",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(35.dp)
                )
            }

            OutlinedButton(
                modifier = Modifier
                    .padding(vertical = 80.dp)
                    .align(Alignment.BottomCenter),
                shape = CircleShape,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(if (isPressed) 8.dp else 12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.background),
                onClick = { /* GNDN */ },
                enabled = false
            ) {
                Button(
                    modifier = Modifier
                        .size(80.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPressed) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary
                    ),
                    interactionSource = interactionSource,
                    enabled = !isLoading,
                    onClick = {
                        isLoading = true
                        if (isCameraReady) {
                            val photoFile = createCustomTempFile(context)
                            val outputOptions =
                                ImageCapture.OutputFileOptions.Builder(photoFile).build()
                            imageCaptureUseCase.takePicture(
                                outputOptions,
                                context.executor,
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                        imageUri = output.savedUri!!
                                        val file = uriToFile(context, imageUri)
                                        if (file != null) {
                                            scanViewModel.loadItems(file)
                                        }

                                    }


                                    override fun onError(ex: ImageCaptureException) {
                                        isLoading = false
                                        deleteTempFile(photoFile)
                                        Toast.makeText(
                                            context,
                                            "Failed to capture image.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Timber.tag(ContentValues.TAG).e("onError: ${ex.message}")
                                    }
                                }
                            )
                        } else {
                            isLoading = false
                            Toast.makeText(
                                context,
                                "Camera is not ready. Please wait for initialization.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                }
            }

            LaunchedEffect(previewUseCase) {
                val cameraProvider = context.getCameraProvider()
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        previewUseCase,
                        imageCaptureUseCase
                    )
                    isCameraReady = true

                } catch (ex: Exception) {
                    Timber.tag("CameraCapture").e(ex, "Failed to bind camera use cases")
                }
            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            ) {
                LoadingScreen()
            }

        }

        InvalidImageAlertDialog(
            showDialog = showInvalidImageDialog,
            onDismiss = {
                showInvalidImageDialog = false
            }
        )
    }
}