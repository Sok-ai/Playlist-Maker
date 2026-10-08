package com.example.playlistmaker.core.data.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MIGRATION_2_3 : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS `playlist_track_ref` (
    `ref_playlist_id` INTEGER NOT NULL,
    `ref_song_id` INTEGER NOT NULL,
    PRIMARY KEY(`ref_playlist_id`, `ref_song_id`),
    FOREIGN KEY(`ref_playlist_id`) REFERENCES `playlist_table`(`id`) 
        ON UPDATE NO ACTION ON DELETE CASCADE,
    FOREIGN KEY(`ref_song_id`) REFERENCES `track_in_playlist_table`(`song_id`) 
        ON UPDATE NO ACTION ON DELETE CASCADE
)"""
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_playlist_track_ref_ref_song_id` ON `playlist_track_ref` (`ref_song_id`)")
    }
}