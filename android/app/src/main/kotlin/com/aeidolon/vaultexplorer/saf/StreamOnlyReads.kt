package com.aeidolon.vaultexplorer.saf

import android.content.Context

/**
 * "Stream instead of download" switch for SAF-mirrored vaults.
 *
 * Set from the Flutter settings toggle (MainActivity SET_STREAM_ONLY_READS)
 * and persisted in SharedPreferences, so processes that run without the
 * Flutter UI (documents provider, automation receiver) still honour it.
 * Defaults to true, matching AppSettings.streamOnlySafReads.
 */
object StreamOnlyReads {
    private const val PREFS = "ve_runtime_flags"
    private const val KEY = "stream_only_reads"

    @Volatile
    private var cached: Boolean? = null

    fun isEnabled(context: Context): Boolean {
        cached?.let { return it }
        return synchronized(this) {
            cached ?: context.applicationContext
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getBoolean(KEY, true)
                .also { cached = it }
        }
    }

    fun set(context: Context, enabled: Boolean) {
        cached = enabled
        context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY, enabled).apply()
    }
}
