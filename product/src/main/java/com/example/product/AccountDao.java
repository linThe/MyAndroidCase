package com.example.product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//根据需求写方法
public class AccountDao {
    private MyHelper helper ;
    public AccountDao (Context context){
        helper = new MyHelper(context) ;
    }
     public void insert (Account account){
         SQLiteDatabase db = helper.getWritableDatabase() ;
         ContentValues cv = new ContentValues() ;
         cv.put("name",account.getName());
         cv.put("money",account.getMoney());
         long id = db.insert("account",null,cv) ; //主键自增不需要id
         account.setId(id); //插入一次给一个id  按这样说：这个id就是从0开始的
         db.close();
    }
    public int delete(long id){
        SQLiteDatabase db = helper.getWritableDatabase() ;
//        String sql = "delete from account where _id = " + id ;
//        db.execSQL(sql);
        int count = db.delete("account","_id = "+id,null) ;
        db.close();
        return count;
    }

    public int updata(Account account){ //理解为什么要传实体类：可能要修改全部的值
        SQLiteDatabase db = helper.getWritableDatabase() ;
        ContentValues cv = new ContentValues() ;
        cv.put("name",account.getName());
        cv.put("money",account.getMoney());
        int count = db.update("account",cv,"_id = "+account.getId(),null) ;
        return count ;
    }
    public List<Account> queryAll(){
        SQLiteDatabase db = helper.getWritableDatabase() ;
        List<Account> list = new ArrayList<Account>() ;
        Cursor cursor = db.query("account",null,null,null,null,null,"money DESC") ;
        while(cursor.moveToNext()){
            Account account = new Account(cursor.getLong(0),cursor.getString(1),cursor.getInt(2)) ;
            list.add(account) ;
        }
        cursor.close();
        db.close();
        return list ;
    }

}
