package com.pnj.saku_planner.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.ui.components.PrimaryButton
import com.pnj.saku_planner.ui.theme.AppColor
import com.pnj.saku_planner.ui.theme.SakuPlannerTheme
import com.pnj.saku_planner.ui.theme.Typography

@Composable
fun RegisterScreen() {
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
                    text = "Create an Account",
                    style = Typography.displayLarge,
                    color = AppColor.SecondaryForeground,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Enter your information to create a new account",
                    style = Typography.bodyMedium,
                    color = AppColor.MutedForeground,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {},

                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account?", style = Typography.labelMedium)
                    TextButton(onClick = {}) {
                        Text("Forgot Password", style = Typography.labelMedium)
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = {}) {
                Text("Sign In", style = Typography.bodyLarge)
            }
            PrimaryButton(
                onClick = {},
            ) {
                Text(
                    text = "Create account",
                    style = Typography.titleMedium,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    SakuPlannerTheme {
        RegisterScreen()
    }
}