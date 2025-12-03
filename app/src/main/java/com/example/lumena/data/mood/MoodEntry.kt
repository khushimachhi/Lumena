package com.example.lumena.data.mood

import androidx.room.Entity
import androidx.room.PrimaryKey


 // dateIso should be "YYYY-MM-DD" (LocalDate ISO). Use DateUtils.todayIso() helper when saving.
@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateIso: String,
    val mood: Int,      // 1..5
    val energy: Int,    // 0..10
    val note: String? = null
)
