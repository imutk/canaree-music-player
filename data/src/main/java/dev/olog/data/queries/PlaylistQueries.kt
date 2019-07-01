package dev.olog.data.queries

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore.Audio.Playlists.*
import dev.olog.contentresolversql.querySql
import dev.olog.core.MediaIdCategory
import dev.olog.core.gateway.Id
import dev.olog.core.prefs.BlacklistPreferences
import dev.olog.core.prefs.SortPreferences

internal class PlaylistQueries(
    private val contentResolver: ContentResolver,
    blacklistPrefs: BlacklistPreferences,
    sortPrefs: SortPreferences
) : BaseQueries(blacklistPrefs, sortPrefs, false) {

    fun getAll(): Cursor {

        val query = """
            SELECT $_ID, $NAME
            FROM $EXTERNAL_CONTENT_URI
            ORDER BY $DEFAULT_SORT_ORDER
        """

        return contentResolver.querySql(query)
    }

    fun getById(id: Id): Cursor {

        val query = """
            SELECT $_ID, $NAME
            FROM $EXTERNAL_CONTENT_URI
            WHERE $_ID = ?
            ORDER BY $DEFAULT_SORT_ORDER
        """

        return contentResolver.querySql(query)
    }

    fun countPlaylistSize(playlistId: Id): Cursor {
        val query = """
            SELECT ${Members._ID}, ${Members.AUDIO_ID}
            FROM ${Members.getContentUri("external", playlistId)}
            WHERE ${defaultSelection()}
        """
        return contentResolver.querySql(query)
    }

    fun getRelatedArtists(playlistId: Id): Cursor {
        val query = """
             SELECT
                ${Members.ARTIST_ID},
                ${Members.ARTIST},
                ${Columns.ALBUM_ARTIST},
                ${Members.IS_PODCAST}
            FROM ${Members.getContentUri("external", playlistId)}
            WHERE ${defaultSelection()}
            ORDER BY lower(${Members.ARTIST}) COLLATE UNICODE ASC
        """

        return contentResolver.querySql(query)
    }

    fun getSongList(playlistId: Id): Cursor {

        val query = """
            SELECT ${Members._ID}, ${Members.ARTIST_ID}, ${Members.ALBUM_ID},
                ${Members.TITLE}, ${Members.ARTIST}, ${Members.ALBUM}, ${Columns.ALBUM_ARTIST},
                ${Members.DURATION}, ${Members.DATA}, ${Members.YEAR},
                ${Members.TRACK}, ${Members.DATE_ADDED}, ${Members.IS_PODCAST}
            FROM ${Members.getContentUri("external", playlistId)}
            WHERE ${defaultSelection()}
            ORDER BY ${songListSortOrder(MediaIdCategory.PLAYLISTS, Members.DEFAULT_SORT_ORDER)}
        """
        return contentResolver.querySql(query)
    }

    private fun defaultSelection(): String {
        return "${isPodcast()} AND ${notBlacklisted()}"
    }

}