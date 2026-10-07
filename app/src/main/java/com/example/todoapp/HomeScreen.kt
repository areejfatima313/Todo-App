package com.example.todoapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todoapp.data.entity.Task
import com.example.todoapp.viewmodel.TaskViewModel

@Composable
fun MyTasksScreen(
    viewModel: TaskViewModel,
    onLogout: () -> Unit = {},
    onAddTaskClick: () -> Unit = {},
    onEditTaskClick: (Int) -> Unit = {}
) {


    val tasks by viewModel.tasks.collectAsStateWithLifecycle()

    //  State
    var newTaskText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var showLogoutDialog by remember { mutableStateOf(false) }

    //  Filter Logic
    val filteredTasks = when (selectedFilter) {
        "Pending" -> tasks.filter { !it.isDone }
        "Completed" -> tasks.filter { it.isDone }
        else -> tasks
    }

    Scaffold(
        containerColor = Color(0xFFFFFBFE),
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF06292))
                    .clickable { onAddTaskClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF06292))
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )

                Text(
                    text = "My Tasks",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { showLogoutDialog = true }
                )
            }

            // 📥 Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(Modifier.height(16.dp))

                // ➕ Quick Add Task Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newTaskText,
                        onValueChange = { newTaskText = it },
                        placeholder = {
                            Text("Add a new task...", color = Color(0xFFB0B0BC), fontSize = 14.sp)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFF06292),
                            unfocusedBorderColor = Color(0xFFE8E8ED),
                            focusedContainerColor = Color(0xFFF8F8FC),
                            unfocusedContainerColor = Color(0xFFF8F8FC)
                        )
                    )

                    Spacer(Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF06292))
                            .clickable {
                                if (newTaskText.isNotBlank()) {
                                    viewModel.addTask(newTaskText.trim(), "Today")
                                    newTaskText = ""
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 🏷️ Filter Chips
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChipItem("All", selectedFilter == "All") { selectedFilter = "All" }
                    FilterChipItem("Pending", selectedFilter == "Pending") { selectedFilter = "Pending" }
                    FilterChipItem("Completed", selectedFilter == "Completed") { selectedFilter = "Completed" }
                }

                Spacer(Modifier.height(16.dp))

                // 🎯 Empty State vs Task List
                if (filteredTasks.isEmpty()) {
                    EmptyStateView()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filteredTasks, key = { it.id }) { task ->
                            TaskItemCard(
                                task = task,
                                onToggle = { viewModel.toggleDone(task) },
                                onDelete = { viewModel.deleteTask(task) },
                                onEdit = { onEditTaskClick(task.id) }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(80.dp))
            }
        }
    }

    // 🎯 Logout Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Logout",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to logout?",
                    fontSize = 14.sp,
                    color = Color(0xFF6B6B7B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE53935))
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Logout", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE8E8ED), RoundedCornerShape(10.dp))
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("Cancel", color = Color(0xFF1A1A2E), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
}

// 🎯 Empty State
@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFFCE4EC)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.ListAlt,
                contentDescription = "No tasks",
                tint = Color(0xFFF06292),
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "No tasks yet!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A2E)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Add your first task to get started.",
            fontSize = 14.sp,
            color = Color(0xFF8E8E9A),
            textAlign = TextAlign.Center
        )
    }
}

// 🏷️ Filter Chip
@Composable
fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0xFFF06292) else Color.White)
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFFF06292) else Color(0xFFE8E8ED),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Color(0xFF6B6B7B),
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// 🗂️ Task Item Card
@Composable
fun TaskItemCard(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF0F0F5), RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .border(
                    width = 2.dp,
                    color = if (task.isDone) Color(0xFFF06292) else Color(0xFFD0D0DA),
                    shape = RoundedCornerShape(6.dp)
                )
                .background(if (task.isDone) Color(0xFFF06292) else Color.Transparent)
                .clickable { onToggle() },
            contentAlignment = Alignment.Center
        ) {
            if (task.isDone) {
                Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (task.isDone) Color(0xFFB0B0BC) else Color(0xFF1A1A2E)
            )
            Spacer(Modifier.height(2.dp))
            Text(task.date, fontSize = 12.sp, color = Color(0xFF8E8E9A))
        }

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit",
            tint = Color(0xFFF06292),
            modifier = Modifier
                .size(20.dp)
                .clickable { onEdit() }
        )

        Spacer(Modifier.width(12.dp))

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color(0xFFE53935),
            modifier = Modifier
                .size(20.dp)
                .clickable { onDelete() }
        )
    }
}