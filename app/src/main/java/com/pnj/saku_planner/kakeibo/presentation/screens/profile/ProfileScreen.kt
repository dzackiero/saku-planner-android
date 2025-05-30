package com.pnj.saku_planner.kakeibo.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.kakeibo.presentation.components.ui.DefaultForm
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton

@Composable
fun ProfileScreen(
    state: ProfileState = ProfileState(),
    callback: ProfileCallback = ProfileCallback(),
    onNavigateBack: () -> Unit = { },
) {
    DefaultForm(
        title = stringResource(R.string.edit_profile),
        onNavigateBack = onNavigateBack,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.full_name),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = state.name,
                    onValueChange = callback.onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.email_address),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = state.email,
                    onValueChange = callback.onEmailChange,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = callback.updateProfile,
            ) { Text(stringResource(R.string.update_profile)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    KakeiboTheme {
        ProfileScreen()
    }
}