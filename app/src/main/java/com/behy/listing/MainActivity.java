package com.behy.listing;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Constants
    String _settingFile="SETTING";
    String _initFlag ="IS_INITIALIZED";
    String _cat_no="CAT_NUMBER";

    ArrayList<Category> cat_list = new ArrayList<>();
    ArrayList<String[]> item_title = new ArrayList<>();

    Context context;
    int catId=0;

    private boolean isInitialized(){
        SharedPreferences setting = getSharedPreferences(_settingFile,MODE_PRIVATE);
        if(setting.contains(_initFlag))
            return setting.getBoolean(_initFlag, false);
        return false;
    }
    private boolean inizializeCats(){

        // ____________________ Init Data
        String[] cat_name={"cat_1","cat_2","catt_3"};
        ArrayList<String[]> titleList = new ArrayList<>();
        titleList.add(new String[]{"title_11", "title_12", "title_13", "title_14", "title_15", "title_16"});
        titleList.add(new String[]{"title_21", "title_22", "title_23"});
        titleList.add(new String[]{"title_31", "title_32", "title_33", "title_34"});

        ArrayList<String[]> subtitleList = new ArrayList<>();
        subtitleList.add(new String[]{"subtitle_11", "subtitle_12", "subtitle_13", "subtitle_14", "subtitle_15", "subtitle_16"});
        subtitleList.add(new String[]{"subtitle_21", "subtitle_22", "subtitle_23"});
        subtitleList.add(new String[]{"subtitle_31", "subtitle_32", "subtitle_33", "subtitle_34"});

        ArrayList<int[]> iconList = new ArrayList<>();
        iconList.add(new int[]{R.drawable.ic_cat1_item,R.drawable.ic_cat1_item,R.drawable.ic_cat1_item,R.drawable.ic_cat1_item,R.drawable.ic_cat1_item,R.drawable.ic_cat1_item});
        iconList.add(new int[]{R.drawable.ic_cat2_item,R.drawable.ic_cat2_item,R.drawable.ic_cat2_item,});
        iconList.add(new int[]{R.drawable.ic_cat3_item,R.drawable.ic_cat3_item,R.drawable.ic_cat3_item,R.drawable.ic_cat3_item});

        // _______________________ Update cat_list
        for(int j=0;j<titleList.size();j++) {
            Category cat = new Category(cat_name[j], titleList.get(j).length);
            Set title_set= new HashSet();
            Set subtitle_set= new HashSet();
            Set icon_set= new HashSet();
            for (int i = 0; i < titleList.get(j).length; i++) {
                ItemClass item = new ItemClass();
                item.title = titleList.get(j)[i];
                item.subTitle = subtitleList.get(j)[i];
                item.icon = iconList.get(j)[i];
                cat.itemList.add(item);
                title_set.add(titleList.get(j)[i]);
                Log.i("init",""+subtitleList.get(j)[i]);
                subtitle_set.add(subtitleList.get(j)[i]);
                icon_set.add(iconList.get(j)[i]);
            }
            cat_list.add(cat);
            String spName="CAT_" + Integer.toString(j,3);
            SharedPreferences.Editor editor = getSharedPreferences(spName, MODE_PRIVATE).edit();
            editor.putString("NAME","cat_xx");
            editor.putStringSet("ITEM_TITLE", title_set);
            editor.putStringSet("ITEM_SUBTITLE", subtitle_set);
            editor.commit();
        }

        // _____________________ save init data in SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(_settingFile,MODE_PRIVATE).edit();
        editor.putBoolean(_initFlag,true);
        editor.putInt(_cat_no,cat_list.size());
        editor.commit();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        context = this;
        SharedPreferences setting = getSharedPreferences(_settingFile, MODE_PRIVATE);
        if (setting.contains(_initFlag) && setting.getBoolean(_initFlag, false)) {        // is initialized
            readData(setting);
            Log.i("init","read from memory");
        }
        else{
            inizializeCats();
            Log.i("init", "init");
        }
//        inizializeCats();
    }

    private void readData(SharedPreferences setting){
        int catCount=setting.getInt(_cat_no,0);
        if(catCount>0){
            for(int i=0;i<catCount;i++) {
                String catName = "CAT_" + Integer.toString(i, 3);
                SharedPreferences catData = getSharedPreferences(catName, MODE_PRIVATE);
                String name = catData.getString("NAME", "naName");
                Set<String> itemTitle=catData.getStringSet("ITEM_TITLE", null);
                Set<String> itemSubttitle=catData.getStringSet("ITEM_SUBTITLE", null);
                int itemCount = itemTitle.size();

                Category cat = new Category(name,itemCount);

                for(int j=0;j<itemCount;j++){
                    ItemClass item = new ItemClass();
                    item.title=(itemTitle.toArray())[j].toString();
                    item.subTitle=(itemSubttitle.toArray())[j].toString();
                    item.icon=R.drawable.ic_cat1_item;
                    cat.itemList.add(item);
                }
                cat_list.add(cat);
            }
        }

    }

    public void btnClick(View view){
        catId=Integer.parseInt(view.getTag().toString());
        ExtendedArrayAdapter<String> aa =  new ExtendedArrayAdapter(context,cat_list.get(catId-1).itemList);
        ((ListView)findViewById(R.id.mList)).setAdapter(aa);
    }

    class ExtendedArrayAdapter<String> extends ArrayAdapter<ItemClass>{
        private Context context;
        private ArrayList<ItemClass> items;
        public ExtendedArrayAdapter(Context context, ArrayList<ItemClass> items){
            super(context,-1,items);
            this.context = context;
            this.items=items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.lv_item_bg,parent,false);
            TextView itemTitle = (TextView)itemView.findViewById(R.id.itemTitle);
            TextView itemSubTitle = (TextView)itemView.findViewById(R.id.itemSubTitle);
            ImageView itemIcon = (ImageView)itemView.findViewById(R.id.itemIcon);

            itemTitle.setText(items.get(position).title);
            itemSubTitle.setText(items.get(position).subTitle);
            itemIcon.setBackgroundResource(items.get(position).icon);
            return itemView;
        }
    }
    class ItemClass {
        String title="";
        String subTitle="";
        String unit="";
        int icon;
        int quantity;
        int parentID;
        int orderInList;
    }
    class Category{
        ArrayList<ItemClass> itemList;
        int orderInList;
        int item_count;
        String name;

        public Category(String name,int count){
            item_count=count;
            this.name=name;
            itemList=new ArrayList<>();
//            SharedPreferences cat=getSharedPreferences(name,MODE_PRIVATE);
//            for(int i=0;i<item_count;i++){
//                //ItemClass obj=new ItemClass();
//                String s=cat.getString("ITEM_"+Integer.toString(i),"no_item");
//                Log.w(name,s);
//            }
        }
    }







    private void saveDate(String _FileName , String _Name , String _Value){
        SharedPreferences preferences = getSharedPreferences(_FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(_Name, _Value); // value to store
        editor.commit();
    }
    private void saveDate(String _FileName , String _Name , int _Value){
        SharedPreferences preferences = getSharedPreferences(_FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(_Name, _Value); // value to store
        editor.commit();
    }
    private void saveDate(String _FileName , String _Name , boolean _Value){
        SharedPreferences preferences = getSharedPreferences(_FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(_Name, _Value); // value to store
        editor.commit();
    }
    private String loadString(String _FileName , String _VarName){
        SharedPreferences preferences = getSharedPreferences(_FileName, Context.MODE_PRIVATE);
        return preferences.getString(_VarName, "nothing");
    }
    private int loadInt(String _FileName , String _VarName){
        SharedPreferences preferences = getSharedPreferences(_FileName, Context.MODE_PRIVATE);
        return preferences.getInt(_VarName, 321321);
    }
    private boolean loadBool(String _FileName , String _VarName){
        SharedPreferences preferences = getSharedPreferences(_FileName, Context.MODE_PRIVATE);
        return preferences.getBoolean(_VarName, false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        String fileName="MAIN_MENU";
        String dataLine="";
        String[] mainMenu=new String[]{
                "cyt_01"+";"+R.drawable.ic_main_cat1,
                "cat_02"+";"+R.drawable.ic_main_cat1,
                "cat_03"+";"+R.drawable.ic_main_cat1};
        String[] cat_01 = new String[]{
                "itemTitle"
        };
        if(id==R.id.action_init){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
