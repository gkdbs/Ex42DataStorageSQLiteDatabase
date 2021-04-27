package com.milkywaylhy.ex42datastoragesqlitedatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    EditText etAge;
    EditText etEmail;

    //SQLiteDatabase 참조변수
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName= findViewById(R.id.et_name);
        etAge= findViewById(R.id.et_age);
        etEmail= findViewById(R.id.et_email);

        // test.db 라는 이름으로 데이터베이스 파일 열기 또는 생성
        // 액티비티클래스에 이 기능이 메소드로 존재함
        // 이 메소드를 실행하면 test.db를 제어할 수 있는
        // 능력이 있는 객체(SQLiteDatabase)를 리턴해줌.
        db= openOrCreateDatabase("test.db", MODE_PRIVATE, null);

        // 만들어진 DB 파일에 테이블(표)을 생성 작업 수행
        // SQL언어를 이용해서 원하는 명령을 Database 에 실행!
        db.execSQL("CREATE TABLE IF NOT EXISTS member(num INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(20), age INTEGER, email TEXT)");
    }

    public void clickinsert(View view) {

        String name= etName.getText().toString();
        int age= Integer.parseInt( etAge.getText().toString() );
        String email= etEmail.getText().toString();

        db.execSQL("INSERT INTO member(name, age, email) VALUES('"+name+"','"+age+"','"+email+"')");

        etName.setText("");
        etAge.setText("");
        etEmail.setText("");
    }

    public void click_selectAll(View view) {
        Cursor cursor= db.rawQuery("SELECT * FROM member",null);
        //리턴객체 Cursor : select 요청의 결과표(ResultSet)를 가리키는 객체
        if(cursor==null) return;

        StringBuffer buffer= new StringBuffer();

        int cnt= cursor.getCount(); //총 레코드 수(데이터의 행(row)의 개수)
        cursor.moveToFirst();//첫 레코드로 이동
        for(int i=0; i<cnt; i++){
            int num= cursor.getInt(0);
            String name= cursor.getString(1);
            int age= cursor.getInt(2);
            String email= cursor.getString(cursor.getColumnIndex("email"));

            buffer.append(num+" "+name+" "+age+" "+email+"\n");

            cursor.moveToNext();//다음 레코드로 이동
        }

        Toast.makeText(this, buffer.toString(), Toast.LENGTH_SHORT).show();
    }

    public void click_select_byName(View view) {
        //이름으로 검색
        String name= etName.getText().toString();

        Cursor cursor= db.rawQuery("SELECT name, age FROM member WHERE name=?", new String[]{name});
        if(cursor==null) return;

        StringBuffer buffer= new StringBuffer();

        while(cursor.moveToNext()){
            String n= cursor.getString(0);
            int age= cursor.getInt(1);

            buffer.append(n+" : "+ age+"\n");
        }

        Toast.makeText(this, buffer.toString(), Toast.LENGTH_SHORT).show();

    }

    public void clickUpdatebyName(View view) {
        //해당 이름을 가진 데이터의 age를 30으로 변경
        String name= etName.getText().toString();
        db.execSQL("UPDATE member SET age=30, email='aa@aa' WHERE name=?", new String[]{name});
    }

    public void clickDeleteByName(View view) {
        String name= etName.getText().toString();
        db.execSQL("DELETE FROM member WHERE name=?", new String[]{name});
    }
}