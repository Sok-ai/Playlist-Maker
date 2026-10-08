package com.example.playlistmaker.core.data.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MIGRATION_1_2 : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `playlist_table` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `playlist_name` TEXT NOT NULL,
                `playlist_description` TEXT NOT NULL,
                `image_path` TEXT,
                `songs` TEXT NOT NULL,
                `amount_songs` INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `track_in_playlist_table` (
                `song_id` INTEGER NOT NULL,
                `track_name` TEXT NOT NULL,
                `artist_name` TEXT NOT NULL,
                `collection_name` TEXT NOT NULL,
                `release_date` TEXT NOT NULL,
                `primary_genre_name` TEXT NOT NULL,
                `country` TEXT NOT NULL,
                `track_time_millis` INTEGER NOT NULL,
                `artwork_url_100` TEXT NOT NULL,
                `preview_url` TEXT NOT NULL,
                PRIMARY KEY(`song_id`)
            )
            """.trimIndent()
        )
    }
}
