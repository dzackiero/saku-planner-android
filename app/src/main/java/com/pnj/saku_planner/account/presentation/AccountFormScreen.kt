package com.pnj.saku_planner.account.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnj.saku_planner.core.ui.components.BottomSheetField
import com.pnj.saku_planner.core.ui.components.DefaultForm
import com.pnj.saku_planner.core.ui.theme.SakuPlannerTheme

@Composable
fun AccountFormScreen(
    modifier: Modifier = Modifier
) {
    DefaultForm(title = "Account") {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    value = "Main Checking",
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        //TODO("Handle text change")
                    },
                )

                OutlinedTextField(
                    value = "Current Balance",
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        //TODO("Handle text change")
                    },
                )

                BottomSheetField(
                    options = listOf<String>(),
                    label = { Text("Account Type") },
                    itemContent = {
                        Text(it)
                    },
                    itemLabel = { it },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountFormScreenPreview() {
    SakuPlannerTheme {
        AccountFormScreen()
    }
}
