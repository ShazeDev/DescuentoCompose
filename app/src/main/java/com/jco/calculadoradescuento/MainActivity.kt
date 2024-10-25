package com.jco.calculadoradescuento

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jco.calculadoradescuento.ui.theme.CalculadoraDescuentoTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraDescuentoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CalculadoraLayout()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalculadoraLayout() {
    var amountInput by remember { mutableStateOf("") }
    var descuentoInput by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false) }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val descuento = descuentoInput.toDoubleOrNull() ?: 0.0
    val porcentajeDescontar = calcularDescuento(amount, descuento, roundUp)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleBar(name = "App descuentos") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Blue
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
                .padding(50.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.total, porcentajeDescontar),
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 24.sp),
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Gray, RoundedCornerShape(16.dp))
                        .padding(16.dp)

                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.descuento, porcentajeDescontar),
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 24.sp),
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Gray, RoundedCornerShape(16.dp))
                        .padding(16.dp)

                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            EditNumberField(
                label = R.string.precio,
                leadingIcon = R.drawable.money,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                value = amountInput,
                onValueChanged = { amountInput = it },
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(),
            )
            EditNumberField(
                label = R.string.calcularDescuento,
                leadingIcon = R.drawable.percent,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                value = descuentoInput,
                onValueChanged = { descuentoInput = it },
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(),
            )

            Button(
                onClick = { /*TODO*/ },
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Blue,

                    )
            ) {
                Text(text = "Generar Descuento", modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize())

            }

            Button(
                onClick = { /*TODO*/ },
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Red
                )
            )
            {
                Text(text = "Limpiar", modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize())
            }


        }
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions
    )
}


// No actualizado aún

private fun calcularDescuento(
    amount: Double,
    tipPercent: Double = 15.0,
    roundUp: Boolean
): String {
    var tip = tipPercent / 100 * amount
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)

}

@Composable
fun TitleBar(name: String) {
    Text(text = name, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.White)
}




