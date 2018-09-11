package mhashim6.android.putback.data

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.provider.Settings
import androidx.core.content.edit
import org.jetbrains.anko.defaultSharedPreferences

object PreferencesRepository {

    private lateinit var preferences: SharedPreferences

    var soundUri: Uri?
        get() {
            val current = sound
            return if (current == SILENT_SOUND_PREFERENCE) null else Uri.parse(current)
        }
        set(value) {
            sound = value?.toString() ?: SILENT_SOUND_PREFERENCE
        }

    private var sound: String
        get() = preferences.getString(KEY_SOUND_PREFERENCE, Settings.System.DEFAULT_NOTIFICATION_URI.toString())!!
        set(value) = preferences.edit {
            putString(KEY_SOUND_PREFERENCE, value)
        }

    fun init(context: Context) {
        preferences = context.defaultSharedPreferences
    }

    const val KEY_SOUND_PREFERENCE = "sound_preference"
    private const val SILENT_SOUND_PREFERENCE = "SILENT"

}