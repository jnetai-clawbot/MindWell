package com.jnetai.mindwell

import android.app.Application
import com.jnetai.mindwell.data.MindWellDatabase

class MindWellApp : Application() {
    val database: MindWellDatabase by lazy { MindWellDatabase.getInstance(this) }
}