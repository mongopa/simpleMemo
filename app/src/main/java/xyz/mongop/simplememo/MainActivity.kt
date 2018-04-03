package xyz.mongop.simplememo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    lateinit var realm: Realm
    lateinit var results: RealmResults<MemoDB>
    lateinit var word_list:ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ボタンのクリック処理
        buttonMain.setOnClickListener {
            val intent = Intent(this@MainActivity, EditActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_status),getString(R.string.status_add))
            startActivity(intent)
        }

        listViewMemo.onItemClickListener = this
        listViewMemo.onItemLongClickListener = this


    }

    //長押しした場合
    override fun onItemLongClick(p0: AdapterView<*>?, p1: View?, Intposition: Int, p3: Long): Boolean {
        val selectedDB = results[Intposition]
        realm.beginTransaction()
        selectedDB.deleteFromRealm()
        realm.commitTransaction()

        word_list.removeAt(Intposition)

        listViewMemo.adapter = adapter
        return true
    }

    //タップした場合
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedDB = results[p2]
        val strSelectedMemo = selectedDB.strMemo

        val intent = Intent(this@MainActivity, EditActivity::class.java)
        intent.putExtra(getString(R.string.intent_key_memo),strSelectedMemo)
        intent.putExtra(getString(R.string.intent_key_status),getString(R.string.status_change))
        intent.putExtra(getString(R.string.intent_key_position), p2)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        //realmインスタンス
        realm = Realm.getDefaultInstance()

        //DBに登録している単語一覧を表示(onCreateの方がonResumeより先に来るから、取得できない。)
        results = realm.where(MemoDB::class.java).findAll().sort(getString(R.string.db_memo))

        //for文を使ってリストの表示形式を修正する
        word_list = ArrayList<String>()
        results.forEach {
            word_list.add(it.strMemo)
        }

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, word_list)
        listViewMemo.adapter = adapter

    }

    override fun onPause() {
        super.onPause()

        realm.close()
    }
}