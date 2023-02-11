package com.example.wordpractice.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Word (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val mean: String,
    val type: String
): Parcelable