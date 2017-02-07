package com.novatek.welfarevendor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

enum LAYOUT_ID {
    MAIN_UI,
    VENDORINFO_UI
}

public class MainActivity extends AppCompatActivity {

    static VendorInformationUI VendorInformation;
    DatabaseManager dbManager;

    private Cursor mCursor;
    private SimpleCursorAdapter mAdapter;
    private Spinner QueryItemSpinner;
    private Spinner ConditionSpinner;
    private EditText QueryEditText;
    private Button QueryButton, ResetButton;
    private TextView QueryCount;
    private ListView ResultListView;

    String QueryItem, Condition, QueryText;
    int QueryItemIndex, ConditionIndex, ResultListIndex;
    LAYOUT_ID      LayoutID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LayoutID = LAYOUT_ID.MAIN_UI;

        dbManager = new DatabaseManager(this);
        VendorInformation = new VendorInformationUI(this);
        QueryItem = "特約商店名稱";
        QueryItemIndex = 1;
        QueryText = "";
        Condition = "LIKE";
        ConditionIndex = 0;
        ResultListIndex = 0;

        String sql = "select * from WelfareVendor where " + QueryItem + " " + Condition + " '%"+QueryText+"%'";
        ExecuteQuery(sql);

        setMainUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.app_information)
        {
            ShowAppInfoDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if(LayoutID != LAYOUT_ID.MAIN_UI){
                setContentView(R.layout.activity_main);
                LayoutID = LAYOUT_ID.MAIN_UI;
                setMainUI();
                String sql = "select * from WelfareVendor where " + QueryItem + " " + Condition + " '%"+QueryText+"%'";
                ExecuteQuery(sql);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if(dbManager != null)
            dbManager.closeDBhelper();
        if(mCursor != null)
            mCursor.close();
        super.onDestroy();
    }

    public void setMainUI()
    {
        QueryItemSpinner = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter QueryItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[]{"分類", "特約商店名稱", "地區", "電話", "地址", "合約起始日", "合約終止日"});
        QueryItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QueryItemSpinner.setAdapter(QueryItemAdapter);
        QueryItemSpinner.setSelection(QueryItemIndex, true);
        QueryItemSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                QueryItem = adapterView.getSelectedItem().toString();
                QueryItemIndex = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        ConditionSpinner = (Spinner) findViewById(R.id.spinner_condition);
        ArrayAdapter ConditionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[]{"LIKE", "=", "<>", ">", "<", ">=", "<="});
        ConditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ConditionSpinner.setAdapter(ConditionAdapter);
        ConditionSpinner.setSelection(ConditionIndex, true);
        ConditionSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                Condition = adapterView.getSelectedItem().toString();
                ConditionIndex = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        QueryEditText = (EditText) findViewById(R.id.text_query);
        QueryEditText.setText(QueryText);
        QueryButton = (Button) findViewById(R.id.button_query);
        QueryButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                QueryText = QueryEditText.getText().toString();
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(QueryEditText.getWindowToken(), 0);

                String sql = "select * from WelfareVendor where " + QueryItem + " " + Condition + " '%"+QueryText+"%'";
                ExecuteQuery(sql);
            }
        });

        ResetButton = (Button) findViewById(R.id.button_reset);
        ResetButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                QueryText = "";
                QueryEditText.setText(QueryText);
                QueryItem = "特約商店名稱";
                QueryItemIndex = 1;
                QueryItemSpinner.setSelection(QueryItemIndex, true);
                Condition = "LIKE";
                ConditionIndex = 0;
                ConditionSpinner.setSelection(ConditionIndex, true);
                String sql = "select * from WelfareVendor where " + QueryItem + " " + Condition + " '%"+QueryText+"%'";
                ExecuteQuery(sql);
            }
        });
    }

    public void ExecuteQuery(String sql)
    {
        mCursor = dbManager.executeSql(sql, null);

        QueryCount = (TextView)findViewById(R.id.text_count);
        String text = getString(R.string.query_result) + "<font color=\"#FF0000\">" + Integer.toString(mCursor.getCount()) + " </font>";
        QueryCount.setText(Html.fromHtml(text));

        mAdapter = new SimpleCursorAdapter(this,
                R.layout.view_vender_entry, mCursor, new String[] { "分類", "特約商店名稱" },
                new int[] { R.id.member_category, R.id.member_name }, 0);

        ResultListView = (ListView)findViewById(R.id.listView);
        ResultListView.setAdapter(mAdapter);
        ResultListView.setSelection(ResultListIndex);
        ResultListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                mCursor.moveToPosition(position);
                String name = mCursor.getString(mCursor.getColumnIndex("特約商店名稱"));
                String phone = mCursor.getString(mCursor.getColumnIndex("電話"));
                String address = mCursor.getString(mCursor.getColumnIndex("地址"));
                String detail = mCursor.getString(mCursor.getColumnIndex("優惠項目"));
                ResultListIndex = ResultListView.getFirstVisiblePosition();
                VendorInformation.SetVendorInformationUI(name, "", phone, address, detail);
                LayoutID = LAYOUT_ID.VENDORINFO_UI;
            }
        });
        ResultListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    ResultListIndex = ResultListView.getFirstVisiblePosition();
                }
            }

        });
    }

    public void ShowAppInfoDialog()
    {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View DialogView = inflater.inflate(R.layout.ui_aboutapp, null);

        AlertDialog.Builder Builder = new AlertDialog.Builder(this);
        Builder.setIcon(R.drawable.app_icon);
        Builder.setTitle(getString(R.string.show_infomation));
        Builder.setView(DialogView);

        Builder.setPositiveButton(getString(R.string.check_infomation),  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        final AlertDialog AppInfoDialog = Builder.create();
        AppInfoDialog.show();
    }
}