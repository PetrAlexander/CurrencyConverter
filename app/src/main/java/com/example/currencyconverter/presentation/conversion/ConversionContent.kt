package com.example.currencyconverter.presentation.conversion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.R

@Composable
fun ConversionContent(component: ConversionComponent) {
    val state by component.model.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var menuType by remember {
        mutableStateOf(FieldType.BASE)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.amount.toString(),
            onValueChange = { component.changeAmountField(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (expanded) {
            when (val listState = state.currencyListState) {
                ConversionStore.State.CurrencyListState.Error -> {
                    Text(text = stringResource(R.string.something_went_wrong))
                }

                ConversionStore.State.CurrencyListState.Initial -> {

                }

                is ConversionStore.State.CurrencyListState.Loaded -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.padding(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(listState.currencyList) {
                                CurrencyItem(
                                    code = it.code,
                                    onClick = {
                                        component.changeCurrency(
                                            menuType,
                                            it
                                        )
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                ConversionStore.State.CurrencyListState.Loading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {
                    component.clickCurrency()
                    menuType = FieldType.BASE
                    expanded = true
                }
            ) {
                Text(text = state.baseCurrency.code)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.convert_to)
            )
            TextButton(
                onClick = {
                    component.clickCurrency()
                    menuType = FieldType.TARGET
                    expanded = true
                }
            ) {
                Text(text = state.targetCurrency.code)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                component.clickConvert(
                    state.amount,
                    state.baseCurrency.code,
                    state.targetCurrency.code
                )
            }
        ) {
            Text(text = stringResource(R.string.convert))
        }
    }
}