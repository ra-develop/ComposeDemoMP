import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.*
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


data class Country(val name: String, val zone: TimeZone, val flagImage: String)

fun countries() = listOf(
    Country("Japan", TimeZone.of("Asia/Tokyo"), "🇯🇵" ),
    Country("France", TimeZone.of("Europe/Paris"), "🇫🇷"),
    Country("Mexico", TimeZone.of("America/Mexico_City"), "🇲🇽"),
    Country("Indonesia", TimeZone.of("Asia/Jakarta"), "🇮🇩"),
    Country("Egypt", TimeZone.of("Africa/Cairo"), "🇪🇬"),
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(countries: List<Country> = countries()) {
    MaterialTheme {
        var greetingText by remember { mutableStateOf("Hello World!") }
        var showImage by remember { mutableStateOf(false) }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Today's date is ${toDaysDate()}",
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            var timeAtLocation by remember { mutableStateOf("No location selected") }

            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    timeAtLocation,
                    style = TextStyle(fontSize = 20.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )

                var showCountries by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier.padding(10.dp),
                ) {
                    DropdownMenu(
                        expanded = showCountries,
                        onDismissRequest = { showCountries = false }
                    ) {
                        countries.forEach { (name, zone, flagImage) ->
                            DropdownMenuItem(
                                onClick = {
                                    timeAtLocation = currentTimeAt(name, zone)
                                    showCountries = false
                                }
                            ) {
                                Text("$flagImage $name")
                            }
                        }
                    }

                }

                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = { showCountries = !showCountries }
                ) {
                    Text(
                        "Select Location",
                        textAlign = TextAlign.Center
                    )
                }


            }
            Spacer(Modifier.requiredHeight(20.dp))
            Button(onClick = {
                greetingText = "Compose: ${Greeting().greet()}"
                showImage = !showImage
            }) {
                Text(greetingText)
            }
            AnimatedVisibility(showImage) {
                Image(
                    painterResource("compose-multiplatform.xml"),
                    null
                )
            }
        }
    }
}

fun toDaysDate(): String {
    fun LocalDateTime.format() = toString().substringBefore('T')

    val now = Clock.System.now()
    val zone = TimeZone.currentSystemDefault()
    return now.toLocalDateTime(zone).format()
}

fun currentTimeAt(location: String, zone: TimeZone): String {
    fun LocalTime.formatted() = "$hour:$minute:$second"

    val time = Clock.System.now()
    val localTime = time.toLocalDateTime(zone).time

    return "The time in $location is ${localTime.formatted()}"
}
