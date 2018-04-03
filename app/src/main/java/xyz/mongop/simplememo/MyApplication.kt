package xyz.mongop.simplememo

import android.app.Application
import io.realm.Realm

/**
 * Created by Owner on 2017/11/01.
 */
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        //Realmの初期化
        Realm.init(this)
    }
}