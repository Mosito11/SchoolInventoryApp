package com.example.schoolinventoryapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "items_not_correct",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_from_db"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_selected"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["room_from_db"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["room_selected"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("qr"),
        Index("user_from_db"),
        Index("user_selected"),
        Index("room_from_db"),
        Index("room_selected")
    ]
)

data class IncorrectItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val qr: String,
    @ColumnInfo(name = "date_created")
    val dateCreated: String = LocalDate.now().toString(),
    @ColumnInfo(name = "user_from_db")
    val userFromDatabase: Int,
    @ColumnInfo(name = "user_selected")
    val userSelected: Int,
    @ColumnInfo(name = "room_from_db")
    val roomFromDatabase: Int,
    @ColumnInfo(name = "room_selected")
    val roomSelected: Int,
)
