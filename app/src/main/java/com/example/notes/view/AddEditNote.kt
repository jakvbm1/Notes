
package com.example.notes.view
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.notes.AlarmScheduler

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddNotesScreen() {
    val context = LocalContext.current
    val selectedInterval = "weekly"
    //selectedInterval będzie miało przypisywaną wartość w zależności od tego co tam użytkownik wybierze

    AlarmScheduler.scheduleAlarm(selectedInterval, context)

}


