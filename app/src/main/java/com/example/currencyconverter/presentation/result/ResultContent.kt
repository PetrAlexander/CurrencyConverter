package com.example.currencyconverter.presentation.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.R

@Composable
fun ResultContent(component: ResultComponent) {
    val state by component.model.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val conversionState = state.conversionState) {
            ResultStore.State.ConversionState.Error -> {
                Text(text = stringResource(R.string.something_went_wrong))
            }

            ResultStore.State.ConversionState.Initial -> {

            }

            is ResultStore.State.ConversionState.Loaded -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CurrencyResult(
                        currencyValue = state.amount.toString(),
                        currencyCode = state.baseCode
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.convert_to)
                    )
                    CurrencyResult(
                        currencyValue = conversionState.conversion.toString(),
                        currencyCode = state.targetCode
                    )
                }
            }

            ResultStore.State.ConversionState.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
