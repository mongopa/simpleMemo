package xyz.mongop.simplememo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    lateinit var realm: Realm
    var strMemo:String = ""
    var intPosition:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //インテントの受け取り
        val bundle = intent.extras
        val strStatus = bundle.getString(getString(R.string.intent_key_status))

        //修正の場合、前に入っていたメモの表示
        if (strStatus == getString(R.string.status_change)){
            strMemo = bundle.getString(getString(R.string.intent_key_memo))
            editTextMemo.setText(strMemo)

            intPosition = bundle.getInt(getString(R.string.intent_key_position))
        }

        //登録ボタンを押した場合
        buttonEdit.setOnClickListener {
            if (strStatus == getString(R.string.status_add)){
                addNewWord()
            }else{
                changeWord()
            }
        }

        //もどるボタンを押した場合
        buttonEditBack.setOnClickListener {
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        //Realmインスタンスの取得
        realm = Realm.getDefaultInstance()

    }

    override fun onPause() {
        super.onPause()
        //インスタンスの片付け
        realm.close()
    }

    //修正の場合
    private fun changeWord() {
        val results = realm.where(MemoDB::class.java).findAll().sort(getString(R.string.db_memo))
        val selectedDB = results.get(intPosition)

        //入力した問題・答えでレコードの更新
        realm.beginTransaction()
        selectedDB.strMemo = editTextMemo.text.toString()
        realm.commitTransaction()

        Toast.makeText(this@EditActivity,"修正が完了しました", Toast.LENGTH_SHORT).show()

        finish()
        editTextMemo.setText("")
    }

    //新規登録の場合
    private fun addNewWord() {
        realm.beginTransaction()
        val wordDB = realm.createObject(MemoDB::class.java)
        wordDB.strMemo = editTextMemo.text.toString()
        realm.commitTransaction()

        Toast.makeText(this@EditActivity,"登録が完了しました", Toast.LENGTH_SHORT).show()

        finish()

        editTextMemo.setText("")
    }
}
