package demo.lizl.com.psnine.config

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log

class ConfigHelper(context: Context)
{
    private val TAG = "ConfigHelper"

    private var mSettings: SharedPreferences? = null
    private var mSettingsEditor: SharedPreferences.Editor? = null

    init
    {
        mSettings = PreferenceManager.getDefaultSharedPreferences(context)
        mSettingsEditor = mSettings!!.edit()
    }

    fun putString(entry: String, value: String): Boolean
    {
        if (mSettingsEditor == null)
        {
            Log.e(TAG, "Settings are null")
            return false
        }
        mSettingsEditor!!.putString(entry, value)
        return mSettingsEditor!!.commit()
    }

    fun putInt(entry: String, value: Int): Boolean
    {
        if (mSettingsEditor == null)
        {
            Log.e(TAG, "Settings are null")
            return false
        }
        mSettingsEditor!!.putInt(entry, value)
        return mSettingsEditor!!.commit()
    }

    fun putFloat(entry: String, value: Float): Boolean
    {
        if (mSettingsEditor == null)
        {
            Log.e(TAG, "Settings are null")
            return false
        }
        mSettingsEditor!!.putFloat(entry, value)
        return mSettingsEditor!!.commit()
    }

    fun putBoolean(entry: String, value: Boolean): Boolean
    {
        if (mSettingsEditor == null)
        {
            Log.e(TAG, "Settings are null")
            return false
        }
        mSettingsEditor!!.putBoolean(entry, value)
        return mSettingsEditor!!.commit()
    }

    fun putLong(entry: String, value: Long): Boolean
    {
        if (mSettingsEditor == null)
        {
            Log.e(TAG, "Settings are null")
            return false
        }
        mSettingsEditor!!.putLong(entry, value)
        return mSettingsEditor!!.commit()
    }

    fun getString(entry: String, defaultValue: String): String?
    {
        if (mSettingsEditor == null)
        {
            Log.e(TAG, "Settings are null")
            return defaultValue
        }
        try
        {
            return mSettings!!.getString(entry, defaultValue)
        }
        catch (e: Exception)
        {
            Log.e(TAG, e.toString())
            return defaultValue
        }

    }

    fun getInt(entry: String, defaultValue: Int): Int
    {
        if (mSettingsEditor == null)
        {
            Log.e(TAG, "Settings are null")
            return defaultValue
        }
        return try
        {
            mSettings!!.getInt(entry, defaultValue)
        }
        catch (e: Exception)
        {
            Log.e(TAG, e.toString())
            defaultValue
        }
    }

    fun getLong(entry: String, defaultValue: Long): Long
    {
        if (mSettingsEditor == null)
        {
            Log.e(TAG, "Settings are null")
            return defaultValue
        }
        return try
        {
            mSettings!!.getLong(entry, defaultValue)
        }
        catch (e: Exception)
        {
            Log.e(TAG, e.toString())
            defaultValue
        }

    }

    fun getFloat(entry: String, defaultValue: Float): Float
    {
        if (mSettingsEditor == null)
        {
            Log.e(TAG, "Settings are null")
            return defaultValue
        }
        return try
        {
            mSettings!!.getFloat(entry, defaultValue)
        }
        catch (e: Exception)
        {
            Log.e(TAG, e.toString())
            defaultValue
        }

    }

    fun getBoolean(entry: String, defaultValue: Boolean): Boolean
    {
        if (mSettingsEditor == null)
        {
            Log.e(TAG, "Settings are null")
            return defaultValue
        }
        return try
        {
            mSettings!!.getBoolean(entry, defaultValue)
        }
        catch (e: Exception)
        {
            Log.e(TAG, e.toString())
            defaultValue
        }

    }

    companion object
    {
        private var configHelper: ConfigHelper? = null

        @Synchronized
        fun getDefaultConfigHelper(context: Context): ConfigHelper
        {
            if (configHelper == null)
            {
                configHelper = ConfigHelper(context)
            }
            return configHelper as ConfigHelper
        }
    }
}