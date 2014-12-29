package com.example.wataru.ambitionlist.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wataru.ambitionlist.R;
import com.example.wataru.ambitionlist.dao.AmbitionDao;
import com.example.wataru.ambitionlist.database.DatabaseOpenHelper;
import com.example.wataru.ambitionlist.error.ErrorLog;
import com.example.wataru.ambitionlist.model.AmbitionDto;

import java.util.List;


public class AmbitionList extends Activity {
    View tempView = null;
    DatabaseOpenHelper helper = null;
    EditText editAmb = null;
    ListView listAmb = null;
    ArrayAdapter<AmbitionDto> adapter = null;
    List<AmbitionDto> ambTitleList = null;
    InputMethodManager inputMethodManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambition_list);

        editAmb = (EditText)findViewById(R.id.editAmb);
        listAmb = (ListView)findViewById(R.id.listAmb);

        helper = new DatabaseOpenHelper(this);

        //リストにamb_titleを入れる
        SQLiteDatabase db = helper.getReadableDatabase();
        try{
            AmbitionDao dao = new AmbitionDao(db);
            ambTitleList = dao.findAll();
        }finally {
            db.close();
        }
        adapter = new ArrayAdapter<AmbitionDto>(this, android.R.layout.simple_list_item_multiple_choice, ambTitleList);
        listAmb.setAdapter(adapter);

        //リスト項目長押し時の処理を定義
        listAmb.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    public boolean onItemLongClick(AdapterView<?> av,
                        View view, int position, long id) {
                        tempView = view;
                        //ダイアログを生成
                        AlertDialog.Builder builder = new AlertDialog.Builder(AmbitionList.this);
                        builder.setTitle("目標を削除").setMessage("この項目を削除してよろしいですか？")
                        //[はい]ボタンの設定
                        .setPositiveButton("はい",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String[] ambTitles = {((TextView) tempView).getText().toString()};
                                        AmbitionDto dto = null;
                                        SQLiteDatabase db = helper.getReadableDatabase();
                                        try {
                                            AmbitionDao dao = new AmbitionDao(db);
                                            dto = dao.findByAmbTitles(ambTitles);
                                        } finally {
                                            db.close();
                                        }
                                        db = helper.getWritableDatabase();

                                        try {
                                            AmbitionDao dao = new AmbitionDao(db);
                                            dao.deleteToAmbTitles(ambTitles);
                                        } finally {
                                            db.close();
                                        }

                                    //TODO リストから削除が反映されていない
                                        adapter.remove(dto);
                                        //adapter = new ArrayAdapter<AmbitionDto>(this, android.R.layout.simple_list_item_multiple_choice, ambTitleList);
                                        //listAmb.setAdapter(adapter);
                                    }
                                }

                        )
                        //[いいえ]ボタンの設定
                        .setNegativeButton("いいえ",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) { }
                            }
                        ).show();   //表示
                        return false;
                    }
                }
        );

        initEditText();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ambition_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //目標を追加するボタン
    public void onClick(View v) {
        //対象となる目標の文字列
        String target = editAmb.getText().toString();
        insertTitle(target);


    }

    /*値が有効か判別する
    @return boolean true 有効
    @return boolean false 無効
     */
    private boolean validate(String target) {
        //値に何も入力されていない場合
        if (target == null || target.equals("")) {
            ErrorLog.setToastText("値を入力してください");
            return false;
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        AmbitionDao dao = new AmbitionDao(db);

        //既に同じ名前の目標タイトルがあればfalse(汎用性の高いプログラムにする場合は削除する)
        String[] targets = {target};
        if (dao.findByAmbTitles(targets) != null) {
            ErrorLog.setToastText("既に同じ名前の目標が存在しています");
            return false;
        }


        return true;
    }

    private void insertTitle(String target){
        //テキストの内容が正当か確認する
        if (validate(target)) {
            //テキストの内容を追加する
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                AmbitionDao dao = new AmbitionDao(db);
                dao.insert(target);
            } finally {
                db.close();
            }
            //リストを更新する
            db = helper.getReadableDatabase();
            String[] targets = {target};
            AmbitionDto dto = null;
            try {
                AmbitionDao dao = new AmbitionDao(db);
                dto = dao.findByAmbTitles(targets);
            } finally {
                db.close();
            }

            //更新処理
            db = helper.getWritableDatabase();
            try {
                AmbitionDao dao = new AmbitionDao(db);
                ambTitleList = dao.findAll();
            } finally {
                db.close();
            }
            adapter = new ArrayAdapter<AmbitionDto>(this, android.R.layout.simple_list_item_multiple_choice, ambTitleList);
            listAmb.setAdapter(adapter);

            //テキストを初期化する
            editAmb.setText(null);
        } else {
            Toast.makeText(this, ErrorLog.getToastText(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initEditText(){
        //キーボードを閉じたいEditTextオブジェクト
        //editAmb           = (EditText) findViewById(R.id.insertText);
        //キーボード表示を制御するためのオブジェクト
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        //EditTextにリスナーをセット
        editAmb.setOnKeyListener(new View.OnKeyListener() {

            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){

                    insertTitle(editAmb.getText().toString());

                    //テキストを初期化する
                    editAmb.setText("");

                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(editAmb.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    return true;
                }
                return false;
            }
        });
    }

    //リターンキーが押下されたときの処理
//    private void editReturn(final EditText editText) {
//        //EditTextにリスナーをセット
//        editText.setOnKeyListener(new View.OnKeyListener() {
//
//            //コールバックとしてonKey()メソッドを定義
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                EditText edit = (EditText)v;
//                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
//                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
//                    data.add(edit.getText().toString());
//
//                    edit.setText("");
//
//
//                    //キーボードを閉じる
//                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
//
//                    return true;
//                }
//
//                return false;
//            }
//        });
    }



