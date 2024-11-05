package com.example.bottombarbrouillon.Pagedessaye

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bottombarbrouillon.ui.theme.orange
import java.util.*

@Composable
fun EssayeScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Green)
                .height(150.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Memories",
                    fontWeight = FontWeight.Bold,
                    fontSize = 65.sp,
                    color = Color(0xFFFFD700),
                    modifier = Modifier.padding(10.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = Color.Black,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ToDo and Notes Section
        NotesToDoContent(context)
    }
}

@Composable
fun NotesToDoContent(context: Context, viewModel: NotesToDoViewModel = viewModel()) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("ToDo", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = orange)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(viewModel.todoList) { todo ->
                ToDoItem(context, todo)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Notes", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = orange)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(viewModel.notesList) { note ->
                NoteItem(note)
            }
        }
    }
}

@Composable
fun NoteItem(note: Note) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = note.title, fontWeight = FontWeight.Bold)
        Text(text = note.body, color = Color.Gray)
    }
}

@Composable
fun ToDoItem(context: Context, todo: ToDo, viewModel: NotesToDoViewModel = viewModel()) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = { isChecked -> todo.isDone = isChecked }
        )
        Text(text = todo.task, modifier = Modifier.weight(1f))

        IconButton(onClick = { openDateTimePicker(context, todo) }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Set Reminder")
        }
    }
}

// Date and Time Picker for setting alarm
fun openDateTimePicker(context: Context, todo: ToDo) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(context, { _, year, month, dayOfMonth ->
        calendar.set(year, month, dayOfMonth)

        TimePickerDialog(context, { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            scheduleAlarm(context, todo, calendar.timeInMillis)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
}

// Schedule Alarm
fun scheduleAlarm(context: Context, todo: ToDo, timeInMillis: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("TODO_TITLE", todo.task)
    }
    val pendingIntent = PendingIntent.getBroadcast(context, todo.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
}

// Broadcast Receiver for Alarm
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val todoTitle = intent.getStringExtra("TODO_TITLE")
        showNotification(context, todoTitle ?: "Reminder for your ToDo")
    }

    private fun showNotification(context: Context, todoTitle: String) {
        val notification = NotificationCompat.Builder(context, "todo_channel")
            .setContentTitle("ToDo Reminder")
            .setContentText(todoTitle)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}

// ViewModel for managing ToDo and Notes lists
class NotesToDoViewModel : ViewModel() {
    var notesList = listOf(
        Note("Meeting notes", "Discuss project scope"),
        Note("Shopping list", "Buy groceries")
    )
    var todoList = mutableStateListOf(
        ToDo(1, "Finish homework"),
        ToDo(2, "Prepare for presentation")
    )
}

// Data classes
data class Note(
    val title: String,
    val body: String
)

data class ToDo(
    val id: Int,
    val task: String,
    var isDone: Boolean = false
)

// Preview
@Preview (showBackground = true)
@Composable
fun EssayeScreenPreview() {
    EssayeScreen()
}