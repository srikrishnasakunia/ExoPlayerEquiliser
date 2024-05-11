package dev.krishna.exoplayerequiliser.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.krishna.exoplayerequiliser.data.storage.AUDIO_EFFECT_PREFERENCES
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuiler = GsonBuilder()
        return gsonBuiler.create()
    }

    @Named(AUDIO_EFFECT_PREFERENCES)
    @Provides
    fun provideAudioEffectPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(AUDIO_EFFECT_PREFERENCES,Context.MODE_PRIVATE)
    }
}