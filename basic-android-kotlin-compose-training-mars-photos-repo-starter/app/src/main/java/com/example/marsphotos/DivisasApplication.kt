package com.example.marsphotos

import android.app.Application
/*import com.example.marsphotos.data.AppContainer
import com.example.marsphotos.data.DefaultAppContainer

 */
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DivisasApplication : Application()
/*{

  lateinit var container: AppContainer
  override fun onCreate() {
      super.onCreate()
      container = DefaultAppContainer()
  }
}

   */
