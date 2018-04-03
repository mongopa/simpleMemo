package xyz.mongop.simplememo

import io.realm.RealmObject

/**
 * Created by Owner on 2017/11/04.
 */
open class MemoDB: RealmObject() {
    //メモ内容
    var strMemo = ""
}