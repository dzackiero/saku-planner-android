package com.pnj.saku_planner.kakeibo.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.R
import com.pnj.saku_planner.core.theme.AppColor
import com.pnj.saku_planner.core.theme.KakeiboTheme
import com.pnj.saku_planner.core.theme.Typography
import com.pnj.saku_planner.kakeibo.presentation.components.PasswordTextField
import com.pnj.saku_planner.kakeibo.presentation.components.ui.PrimaryButton
import com.pnj.saku_planner.kakeibo.presentation.screens.auth.viewmodels.RegisterCallback
import com.pnj.saku_planner.kakeibo.presentation.screens.auth.viewmodels.RegisterState

@Composable
fun RegisterScreen(
    state: RegisterState = RegisterState(),
    callback: RegisterCallback = RegisterCallback(),
    onRegister: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(vertical = 32.dp, horizontal = 16.dp)
            .fillMaxSize()
            .background(AppColor.PrimaryForeground),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(Modifier.padding(bottom = 16.dp)) {
                Text(
                    text = stringResource(R.string.create_an_account),
                    style = Typography.displayLarge,
                    color = AppColor.SecondaryForeground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = stringResource(R.string.enter_your_information_to_create_a_new_account),
                    style = Typography.bodyMedium,
                    color = AppColor.MutedForeground
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // show error at top
                state.errorMessage?.let { error ->
                    Text(
                        text = error,
                        style = Typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = state.name,
                    onValueChange = callback.onNameChange,
                    label = { Text(stringResource(R.string.full_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.email,
                    onValueChange = callback.onEmailChange,
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordTextField(
                    value = state.password,
                    onPasswordChange = callback.onPasswordChange,
                    label = { Text(stringResource(R.string.password)) },
                    modifier = Modifier.fillMaxWidth()
                )

                PasswordTextField(
                    value = state.passwordConfirm,
                    onPasswordChange = callback.onPasswordConfirmChange,
                    label = { Text(stringResource(R.string.confirm_password)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.already_have_an_account),
                        style = Typography.labelMedium
                    )
                    TextButton(onClick = onNavigateToLogin) {
                        Text(
                            text = stringResource(R.string.login),
                            style = Typography.labelMedium
                        )
                    }
                }
            }
        }

        PrimaryButton(
            onClick = onRegister,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.loading),
                        style = Typography.titleMedium
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.create_new_account),
                    style = Typography.titleMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    KakeiboTheme {
        RegisterScreen(
            state = RegisterState(
                errorMessage = "Registration failed",
                isLoading = true
            ),
            callback = RegisterCallback(),
            onRegister = {},
            onNavigateToLogin = {}
        )
    }
}
