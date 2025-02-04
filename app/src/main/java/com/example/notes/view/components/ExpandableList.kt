package com.example.notes.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

@Composable
fun ExpandableList(
    title: String,
    stringItems: List<String>,
    selectedItem: String,
    onClick: (input: String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var currentSelection by rememberSaveable { mutableStateOf(selectedItem) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,  // Using onSurface for the title text color
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)) // Using outline for border color
                .background(MaterialTheme.colorScheme.surface)  // Using surface for background color
                .clickable { expanded = !expanded }
                .padding(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currentSelection,
                    color = MaterialTheme.colorScheme.onSurface  // Using onSurface for text color
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    tint = MaterialTheme.colorScheme.onSurface  // Using onSurface for the icon color
                )
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)) // Using outline for border color
                    .background(MaterialTheme.colorScheme.surface)  // Using surface for background color
                    .padding(8.dp)
            ) {
                stringItems.forEach { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                currentSelection = item
                                expanded = false
                                onClick(item)
                            }
                            .padding(12.dp),
                        color = MaterialTheme.colorScheme.onSurface  // Using onSurface for text color
                    )
                }
            }
        }
    }
}
