package com.example.schoolinventoryapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["qr"], unique = true),
        Index("user_id"),
        Index("room_id")
    ]
)
data class Item(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "inv_cis")
    val invCis: String,
    @ColumnInfo(name = "naz_inv")
    val nazInv: String?,
    @ColumnInfo(name = "vyr_cis")
    val vyrCis: String?,
    val zarazeno: String,
    val vyroba: Int?,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "room_id")
    val roomId: Int,
    @ColumnInfo(name = "naz_sku")
    val nazSku: String,
    val platnost: Int,
    val qr: String,
)
