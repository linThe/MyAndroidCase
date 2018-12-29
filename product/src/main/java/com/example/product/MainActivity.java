package com.example.product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/*
添加商品、价格，展示到listview，可以调整价格，删除商品
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name,et_money;
    private ImageView img_add,item_img_up,item_img_down,item_img_del ;
    private TextView tv_id,tv_name,tv_money;
    private ListView listView ;

    private List<Account> list ;
    private AccountDao dao ;
    private MyAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initita() ;
        dao = new AccountDao(this) ;
        list = dao.queryAll() ;
        adapter = new MyAdapter() ; //适配数据
        listView.setAdapter(adapter);
    }

    private void initita() {  //可以尝试将点击事件融和到一起
        et_name = (EditText)findViewById(R.id.et_name) ;
        et_money = (EditText)findViewById(R.id.et_money) ;
        img_add = (ImageView)findViewById(R.id.img_add) ;
        listView = (ListView)findViewById(R.id.product_lv) ;
        listView.setOnItemClickListener(new MyOnItemClickListener());
        img_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = et_name.getText().toString().trim() ;
        String money = et_money.getText().toString().trim() ;
        Integer imoney = money.equals("") ? 0 : Integer.parseInt(money) ;
        Account account = new Account(name,imoney) ;
        list.add(account) ;
        adapter.notifyDataSetChanged();
        dao.insert(account);
        //看不懂之选中最后一个
        listView.setSelection(listView.getCount()-1);
        et_name.setText("");
        et_money.setText("");
    }

    //适配器
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override  //接口的方法都是要自己实现的，接口的数据都是"别人"给的
        public View getView(int position, View convertView, ViewGroup parent) {
             //重用convertView,不用再重新获取一次视图，否则刷新一次获取一次视图可能很浪费资源
            View item = convertView != null ? convertView : View.inflate(getApplicationContext(),R.layout.item_layout,null);
            item_img_up = (ImageView)item.findViewById(R.id.img_up) ;  //这些Control在这能访问到，但是在其他方法就访问不到了？
            item_img_down = (ImageView)item.findViewById(R.id.img_down) ;
            item_img_del = (ImageView)item.findViewById(R.id.img_del) ;
            tv_id = (TextView)item.findViewById(R.id.tv_id) ;
            tv_name = (TextView)item.findViewById(R.id.tv_name) ;
            tv_money = (TextView)item.findViewById(R.id.tv_money) ;

            //初始化item的数据
            final Account account = list.get(position) ;
            tv_id.setText(account.getId().toString());
            tv_name.setText(account.getName());
            tv_money.setText(account.getMoney().toString());
            item_img_up.setImageResource(R.drawable.x);
            item_img_down.setImageResource(R.drawable.s);
            item_img_del.setImageResource(R.drawable.garbeage);
            //点击事件                           *if 你要使用主类实现点击事件你需要解决swich的id问题
            item_img_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //方法：1 获取界面的text值转换int加一set回去，当焦点不在这个item的时候更新数据库
                    //方法：2 获取实体类的值加一，刷新界面,更新数据库
                    int m = account.getMoney() + 1 ;
                    account.setMoney(m);
                    notifyDataSetChanged();
                    dao.updata(account) ;
                }
            });
            item_img_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int m = account.getMoney() - 1 ;
                    account.setMoney(m);
                    notifyDataSetChanged();
                    dao.updata(account) ;
                }
            });

            item_img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener alert = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list.remove(account) ; //匹配了某个值就可以删了？testtest
                            notifyDataSetChanged();
                            dao.delete(account.getId()) ;
                        }
                    } ;
                    //创建对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this) ;
                    builder.setTitle("确定要删除吗?");
                    builder.setPositiveButton("确定",alert) ;
                    builder.setNegativeButton("取消",null) ;
                    builder.show() ;
                }
            });

            return item;
        }
    }

    public class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //点击条目获取商品信息
            Account a = (Account)parent.getItemAtPosition(position) ;
            Toast.makeText(getApplicationContext(), a.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
