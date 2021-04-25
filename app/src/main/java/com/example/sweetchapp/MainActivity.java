package com.example.sweetchapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sweetchapp.custom.MarqueeView;
import com.example.sweetchapp.custom.SampleAdapter;
import com.example.sweetchapp.custom.SectionedGridRecyclerViewAdapter;
import com.example.sweetchapp.custom.SimpleAdapter2;
import com.example.sweetchapp.custom.StickyHeaderGridLayoutManager;
import com.example.sweetchapp.customTable.HorizontalScroll;
import com.example.sweetchapp.customTable.VerticalScroll;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.wwdablu.soumya.wzip.WZip;
import com.wwdablu.soumya.wzip.WZipCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements WZipCallback, HorizontalScroll.ScrollViewListener, VerticalScroll.ScrollViewListener  {


    private static int SCREEN_HEIGHT;
    private static int SCREEN_WIDTH;
    RelativeLayout relativeLayoutMain;

    RelativeLayout relativeLayoutA;
    RelativeLayout relativeLayoutAHidden;
    RelativeLayout relativeLayoutB;
    RelativeLayout relativeLayoutHidden;
    RelativeLayout relativeLayoutC;
    RelativeLayout relativeLayoutD;

    TableLayout tableLayoutA;
    TableLayout tableLayoutAHiden;
    TableLayout tableLayoutB;
    TableLayout tableLayoutHidden;
    TableLayout tableLayoutC;
    TableLayout tableLayoutD;

    TableRow tableRow;
    TableRow tableRow2;
    TableRow tableRowB;
    TableRow tableRowHidden;

    HorizontalScroll horizontalScrollViewB;
    HorizontalScroll horizontalScrollViewHidden;
    HorizontalScroll horizontalScrollViewD;

    VerticalScroll scrollViewC;
    VerticalScroll scrollViewD;

    /*
         This is for counting how many columns are added in the row.
    */
    int tableColumnCountB= 0;

    /*
         This is for counting how many row is added.
    */
    int tableRowCountC= 0;






    public static final int REQUEST_CODE = 100;
    public static final String URL_DOWNLOAD_LINK_1 = "https://test-assets-mobile.s3-us-west-2.amazonaws.com/125%402.zip";
    public static final String URL_DOWNLOAD_LINK_2 = "https://test-assets-mobile.s3-us-west-2.amazonaws.com/127%402.zip";
    public static final String FILE_1 = "file1";
    public static final String FILE_2 = "file2";
    public static final String LAST_DISPLAY_URL = "LAST_DISPLAY_URL";
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.progress1)
    ProgressBar progressBar1;
    @BindView(R.id.mView)
    FrameLayout mView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.capture_mode)
    TextView capture_mode;
    boolean isExpanded = false;
    private static final int SPAN_SIZE = 3;
    private static final int SECTIONS = 10;
    private static final int SECTION_ITEMS = 5;

    private RecyclerView mRecycler;
    private StickyHeaderGridLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mView.setClipToOutline(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CustomAdapter adapter = new CustomAdapter( new String[]{"asd"});
        recyclerView.setAdapter(adapter);
        capture_mode.setSelected(true);
        setTable();
       /* final MarqueeView mv = (MarqueeView) findViewById(R.id.marqv);
        mv.setSpeed(10);
        mv.setInterpolator();*/
      /*  getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                mv.startMarquee();
            }
        })*/;
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int height = 0;
                if (isExpanded) height = 250;
                else
                    height = 500;
                ValueAnimator anim = ValueAnimator.ofInt(mView.getMeasuredHeight(), toDp(height));
                anim.addUpdateListener(valueAnimator -> {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
                    layoutParams.height = val;
                    mView.setLayoutParams(layoutParams);
                });
                anim.setDuration(500);
                anim.start();
                isExpanded = !isExpanded;
            }
        });
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE);



       /* // Setup recycler
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));

        //Your RecyclerView.Adapter
        SimpleAdapter2 mAdapter = new SimpleAdapter2(this);

        //This is the code to provide a sectioned grid
        List<SectionedGridRecyclerViewAdapter.Section> sections =
                new ArrayList<SectionedGridRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new SectionedGridRecyclerViewAdapter.Section(0,"Section 1"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(5,"Section 2"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(12,"Section 3"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(14,"Section 4"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(20,"Section 5"));

        //Add your adapter to the sectionAdapter
        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
        SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                SectionedGridRecyclerViewAdapter(this,R.layout.section,R.id.section_text,mRecyclerView,mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        mRecyclerView.setAdapter(mAdapter);*/
    }
    LinearLayout hiddenLl;

    private void setTable() {
        relativeLayoutMain= (RelativeLayout)findViewById(R.id.relativeLayoutMain);
        getScreenDimension();
        initializeRelativeLayout();
        initializeScrollers();
        initializeTableLayout();
        horizontalScrollViewB.setScrollViewListener(this);
        horizontalScrollViewD.setScrollViewListener(this);
        horizontalScrollViewB.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        horizontalScrollViewD.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        horizontalScrollViewHidden.setScrollViewListener(this);
        horizontalScrollViewHidden.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        scrollViewC.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        scrollViewD.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        scrollViewC.setScrollViewListener(this);
        scrollViewD.setScrollViewListener(this);
        addRowToTableA();
        initializeRowForTableB();
        final boolean[] enable = {true};

        relativeLayoutHidden.setVisibility(View.GONE);
        relativeLayoutAHidden.setVisibility(View.GONE);

        ((NestedScrollView)findViewById(R.id.nestedScrollView)).setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int i2, int i3) {
                ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
                ViewGroup.LayoutParams layoutParams2 = relativeLayoutAHidden.getLayoutParams();

                if (toDp(scrollY) >= (toDp(layoutParams.height) + toDp(layoutParams2.height)   ) ) {
                    relativeLayoutHidden.setVisibility(View.VISIBLE);
                    relativeLayoutAHidden.setVisibility(View.VISIBLE);
                } else {
                    relativeLayoutHidden.setVisibility(View.GONE);
                    relativeLayoutAHidden.setVisibility(View.GONE);


                }

            }
        });

//        ((NestedScrollView)findViewById(R.id.nestedScrollView)).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return !enable[0];
//            }
//        });

         hiddenLl = (LinearLayout ) findViewById(R.id.hiddenLl);

        for(int i=0; i<9; i++){
            addColumnsToTableB("Head" + i, i);
            addColumnsToHidden("Head" + i);
        }
        for(int i=0; i<20; i++){
            initializeRowForTableD(i);
            addRowToTableC("Row"+ i);
            for(int j=0; j<tableColumnCountB; j++){
                addColumnToTableAtD(i, "D "+ i + " " + j);
            }
        }
    }

    private void addColumnsToHidden(String s) {
//        TextView label_date = new TextView(getApplicationContext());
//        label_date.setText(s);
//        label_date.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
//        hiddenLl.addView(label_date);

    }

    private int toDp(int val){
        Resources r = getResources();
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, val,r.getDisplayMetrics()));
        return
                px;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void downloadAndUnzipContent(String url) {
        String zipFileName = url.equals(URL_DOWNLOAD_LINK_1) ? "/content1.zip" : "/content12.zip";
        DownloadFileAsync download = new DownloadFileAsync(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +
                zipFileName,
                this, file -> {

                    // check unzip file now
                    WZip unzip = new WZip();
                    unzip.unzip(file,
                            new File(MainActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()),
                            url.equals(URL_DOWNLOAD_LINK_1) ? FILE_1 : FILE_2,
                            MainActivity.this);


                });
        download.execute(url);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(SharedPrefUtils.getStringData(MainActivity.this, LAST_DISPLAY_URL).equals(FILE_1)) {
                        downloadAndUnzipContent(URL_DOWNLOAD_LINK_2);
                        SharedPrefUtils.saveData(MainActivity.this,LAST_DISPLAY_URL, FILE_2);
                    } else {
                        downloadAndUnzipContent(URL_DOWNLOAD_LINK_1);
                        SharedPrefUtils.saveData(MainActivity.this,LAST_DISPLAY_URL, FILE_1);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onStarted(String identifier) {
    }

    @Override
    public void onZipCompleted(File zipFile, String identifier) {
    }

    @Override
    public void onUnzipCompleted(String identifier) {
        String pngFileName = identifier.equals(FILE_1) ? "/25.png" : "/27.png";
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + pngFileName);
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                if (file.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                    progressBar1.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onError(Throwable throwable, String identifier) {

    }



    private void getScreenDimension(){
        WindowManager wm= (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        SCREEN_WIDTH= size.x;
        SCREEN_HEIGHT = size.y;
    }

    private void initializeRelativeLayout(){
        relativeLayoutA= new RelativeLayout(getApplicationContext());
        relativeLayoutA.setId(R.id.relativeLayoutA);
        relativeLayoutA.setPadding(0,0,0,0);

        relativeLayoutAHidden= new RelativeLayout(getApplicationContext());
        relativeLayoutAHidden.setId(R.id.relativeLayoutAHidden);
        relativeLayoutAHidden.setPadding(0,0,0,0);

        relativeLayoutB= new RelativeLayout(getApplicationContext());
        relativeLayoutB.setId(R.id.relativeLayoutB);
        relativeLayoutB.setPadding(0,0,0,0);

        relativeLayoutHidden= new RelativeLayout(getApplicationContext());
        relativeLayoutHidden.setId(R.id.relativeLayoutHidden);
        relativeLayoutHidden.setPadding(0,0,0,0);


        relativeLayoutC= new RelativeLayout(getApplicationContext());
        relativeLayoutC.setId(R.id.relativeLayoutC);
        relativeLayoutC.setPadding(0,0,0,0);

        relativeLayoutD= new RelativeLayout(getApplicationContext());
        relativeLayoutD.setId(R.id.relativeLayoutD);
        relativeLayoutD.setPadding(0,0,0,0);

        relativeLayoutA.setLayoutParams(new RelativeLayout.LayoutParams(SCREEN_WIDTH/5,SCREEN_HEIGHT/20));
        relativeLayoutAHidden.setLayoutParams(new RelativeLayout.LayoutParams(SCREEN_WIDTH/5,SCREEN_HEIGHT/20));
        this.relativeLayoutMain.addView(relativeLayoutA);

        RelativeLayout.LayoutParams layoutParamsRelativeLayoutAHidden= new RelativeLayout.LayoutParams(SCREEN_WIDTH/5,SCREEN_HEIGHT/20);
        layoutParamsRelativeLayoutAHidden.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        relativeLayoutAHidden.setLayoutParams(layoutParamsRelativeLayoutAHidden);
        ((RelativeLayout)findViewById(R.id.mainFl)).addView(relativeLayoutAHidden);



        RelativeLayout.LayoutParams layoutParamsRelativeLayoutB= new RelativeLayout.LayoutParams(SCREEN_WIDTH- (SCREEN_WIDTH/5), SCREEN_HEIGHT/20);
        layoutParamsRelativeLayoutB.addRule(RelativeLayout.LEFT_OF, R.id.relativeLayoutA);
        relativeLayoutB.setLayoutParams(layoutParamsRelativeLayoutB);
        this.relativeLayoutMain.addView(relativeLayoutB);


        RelativeLayout.LayoutParams layoutParamsRelativeLayoutHidden= new RelativeLayout.LayoutParams(SCREEN_WIDTH- (SCREEN_WIDTH/5), SCREEN_HEIGHT/20);
        layoutParamsRelativeLayoutHidden.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParamsRelativeLayoutHidden.addRule(RelativeLayout.LEFT_OF, R.id.relativeLayoutAHidden);
        relativeLayoutHidden.setLayoutParams(layoutParamsRelativeLayoutHidden);
        ((RelativeLayout)findViewById(R.id.mainFl)).addView(relativeLayoutHidden);





        RelativeLayout.LayoutParams layoutParamsRelativeLayoutC= new RelativeLayout.LayoutParams(SCREEN_WIDTH/5, SCREEN_HEIGHT - (SCREEN_HEIGHT/20));
        layoutParamsRelativeLayoutC.addRule(RelativeLayout.BELOW, R.id.relativeLayoutA);
        relativeLayoutC.setLayoutParams(layoutParamsRelativeLayoutC);
        this.relativeLayoutMain.addView(relativeLayoutC);

        RelativeLayout.LayoutParams layoutParamsRelativeLayoutD= new RelativeLayout.LayoutParams(SCREEN_WIDTH- (SCREEN_WIDTH/5), SCREEN_HEIGHT - (SCREEN_HEIGHT/20));
        layoutParamsRelativeLayoutD.addRule(RelativeLayout.BELOW, R.id.relativeLayoutB);
        layoutParamsRelativeLayoutD.addRule(RelativeLayout.LEFT_OF, R.id.relativeLayoutC);
        relativeLayoutD.setLayoutParams(layoutParamsRelativeLayoutD);
        this.relativeLayoutMain.addView(relativeLayoutD);

    }

    private void initializeScrollers(){
        horizontalScrollViewB= new HorizontalScroll(getApplicationContext());
        horizontalScrollViewB.setPadding(0,0,0,0);

        horizontalScrollViewHidden= new HorizontalScroll(getApplicationContext());
        horizontalScrollViewHidden.setPadding(0,0,0,0);


        horizontalScrollViewD= new HorizontalScroll(getApplicationContext());
        horizontalScrollViewD.setPadding(0,0,0,0);

        scrollViewC= new VerticalScroll(getApplicationContext());
        scrollViewC.setPadding(0,0,0,0);

        scrollViewD= new VerticalScroll(getApplicationContext());
        scrollViewD.setPadding(0,0,0,0);

        horizontalScrollViewB.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH- (SCREEN_WIDTH/5), SCREEN_HEIGHT/20));
        horizontalScrollViewHidden.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH- (SCREEN_WIDTH/5), SCREEN_HEIGHT/20));
        scrollViewC.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH/5 ,SCREEN_HEIGHT - (SCREEN_HEIGHT/20)));
        scrollViewD.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH- (SCREEN_WIDTH/5), SCREEN_HEIGHT - (SCREEN_HEIGHT/20) ));
        horizontalScrollViewD.setLayoutParams(new ViewGroup.LayoutParams(SCREEN_WIDTH- (SCREEN_WIDTH/5), SCREEN_HEIGHT - (SCREEN_HEIGHT/20) ));

        this.relativeLayoutB.addView(horizontalScrollViewB);
        this.relativeLayoutHidden.addView(horizontalScrollViewHidden);
        this.relativeLayoutC.addView(scrollViewC);
        this.scrollViewD.addView(horizontalScrollViewD);
        this.relativeLayoutD.addView(scrollViewD);


    }

    private  void initializeTableLayout(){
        tableLayoutA= new TableLayout(getApplicationContext());
        tableLayoutA.setPadding(0,0,0,0);
        tableLayoutAHiden= new TableLayout(getApplicationContext());
        tableLayoutAHiden.setPadding(0,0,0,0);
        tableLayoutB= new TableLayout(getApplicationContext());
        tableLayoutHidden= new TableLayout(getApplicationContext());
        tableLayoutHidden.setPadding(0,0,0,0);
        tableLayoutB.setPadding(0,0,0,0);
        tableLayoutHidden.setId(R.id.tableLayoutHidden);
        tableLayoutB.setId(R.id.tableLayoutB);
        tableLayoutC= new TableLayout(getApplicationContext());
        tableLayoutC.setPadding(0,0,0,0);
        tableLayoutD= new TableLayout(getApplicationContext());
        tableLayoutD.setPadding(0,0,0,0);

        TableLayout.LayoutParams layoutParamsTableLayoutA= new TableLayout.LayoutParams(SCREEN_WIDTH/5, SCREEN_HEIGHT/20);
        tableLayoutA.setLayoutParams(layoutParamsTableLayoutA);
        tableLayoutA.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        this.relativeLayoutA.addView(tableLayoutA);

        TableLayout.LayoutParams layoutParamsTableLayoutAHidden= new TableLayout.LayoutParams(SCREEN_WIDTH/5, SCREEN_HEIGHT/20);
        tableLayoutAHiden.setLayoutParams(layoutParamsTableLayoutAHidden);
        tableLayoutAHiden.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        this.relativeLayoutAHidden.addView(tableLayoutAHiden);


        TableLayout.LayoutParams layoutParamsTableLayoutB= new TableLayout.LayoutParams(SCREEN_WIDTH -(SCREEN_WIDTH/5), SCREEN_HEIGHT/20);
        tableLayoutB.setLayoutParams(layoutParamsTableLayoutB);
        tableLayoutB.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        this.horizontalScrollViewB.addView(tableLayoutB);


        TableLayout.LayoutParams layoutParamsTableLayoutHidden= new TableLayout.LayoutParams(SCREEN_WIDTH -(SCREEN_WIDTH/5), SCREEN_HEIGHT/20);
        tableLayoutHidden.setLayoutParams(layoutParamsTableLayoutHidden);
        tableLayoutHidden.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        this.horizontalScrollViewHidden.addView(tableLayoutHidden);


        TableLayout.LayoutParams layoutParamsTableLayoutC= new TableLayout.LayoutParams(SCREEN_WIDTH/5, SCREEN_HEIGHT - (SCREEN_HEIGHT/20));
        tableLayoutC.setLayoutParams(layoutParamsTableLayoutC);
        tableLayoutC.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        this.scrollViewC.addView(tableLayoutC);

        TableLayout.LayoutParams layoutParamsTableLayoutD= new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        tableLayoutD.setLayoutParams(layoutParamsTableLayoutD);
        this.horizontalScrollViewD.addView(tableLayoutD);


        tableLayoutA.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        tableLayoutAHiden.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        tableLayoutB.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        tableLayoutHidden.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        tableLayoutC.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        tableLayoutD.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);



    }

    @Override
    public void onScrollChanged(HorizontalScroll scrollView, int x, int y, int oldx, int oldy) {
        if(scrollView == horizontalScrollViewB){
            horizontalScrollViewD.scrollTo(x,y);
        }
        if(scrollView == horizontalScrollViewHidden){
            horizontalScrollViewD.scrollTo(x,y);
        }

        else if(scrollView == horizontalScrollViewD){
            horizontalScrollViewB.scrollTo(x, y);
            horizontalScrollViewHidden.scrollTo(x, y);
        }

    }

    @Override
    public void onScrollChanged(VerticalScroll scrollView, int x, int y, int oldx, int oldy) {
        if(scrollView == scrollViewC){
            scrollViewD.scrollTo(x,y);
        }
        else if(scrollView == scrollViewD){
            scrollViewC.scrollTo(x,y);
        }
    }

    private void addRowToTableA(){
        tableRow= new TableRow(getApplicationContext());
        TableRow.LayoutParams layoutParamsTableRow= new TableRow.LayoutParams(SCREEN_WIDTH/5, SCREEN_HEIGHT/20);
        tableRow.setLayoutParams(layoutParamsTableRow);
        TextView label_date = new TextView(getApplicationContext());
        label_date.setText("Item/ID");
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        label_date.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
        tableRow.addView(label_date);
        this.tableLayoutA.addView(tableRow);


        tableRow= new TableRow(getApplicationContext());
        TableRow.LayoutParams layoutParamsTableRowAHidden= new TableRow.LayoutParams(SCREEN_WIDTH/5, SCREEN_HEIGHT/20);
        tableRow.setLayoutParams(layoutParamsTableRowAHidden);
        TextView label_date_hidden = new TextView(getApplicationContext());
        label_date_hidden.setText("Item/ID");
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        label_date_hidden.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
        tableRow.addView(label_date_hidden);
        this.tableLayoutAHiden.addView(tableRow);
    }

    private void initializeRowForTableB(){
        tableRowB= new TableRow(getApplicationContext());
        tableRowHidden= new TableRow(getApplicationContext());
        tableRowHidden.setPadding(0,0,0,0);
        this.tableLayoutB.addView(tableRowB);
        this.tableLayoutHidden.addView(tableRowHidden);
    }

    private synchronized void addColumnsToTableB(String text, final int id){
        tableRow= new TableRow(getApplicationContext());
        TableRow.LayoutParams layoutParamsTableRow= new TableRow.LayoutParams(SCREEN_WIDTH/5, SCREEN_HEIGHT/20);
        tableRow.setPadding(3,3,3,4);
        tableRow.setLayoutParams(layoutParamsTableRow);
        TextView label_date = new TextView(getApplicationContext());
        label_date.setText(text);
        label_date.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
        this.tableRow.addView(label_date);
        this.tableRow.setTag(id);
        this.tableRowB.addView(tableRow);
        tableColumnCountB++;


        tableRow= new TableRow(getApplicationContext());
        TableRow.LayoutParams layoutParamsTableRowHidden= new TableRow.LayoutParams(SCREEN_WIDTH/5, SCREEN_HEIGHT/20);
        tableRow.setPadding(3,3,3,4);
        tableRow.setLayoutParams(layoutParamsTableRowHidden);
        TextView label_dateHidden = new TextView(getApplicationContext());
        label_dateHidden.setText(text);
        label_dateHidden.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
        this.tableRow.addView(label_dateHidden);
        this.tableRow.setTag(id);
        this.tableRowHidden.addView(tableRow);
    }

    private synchronized void addRowToTableC(String text){
        TableRow tableRow1= new TableRow(getApplicationContext());
        TableRow.LayoutParams layoutParamsTableRow1= new TableRow.LayoutParams(SCREEN_WIDTH/5, SCREEN_HEIGHT/20);
        tableRow1.setPadding(3,3,3,4);
        tableRow1.setLayoutParams(layoutParamsTableRow1);
        TextView label_date = new TextView(getApplicationContext());
        label_date.setText(text);
        label_date.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
        tableRow1.addView(label_date);

        TableRow tableRow= new TableRow(getApplicationContext());
        TableRow.LayoutParams layoutParamsTableRow= new TableRow.LayoutParams(SCREEN_WIDTH/5, SCREEN_HEIGHT/20);
        tableRow.setPadding(0,0,0,0);
        tableRow.setLayoutParams(layoutParamsTableRow);
        tableRow.addView(tableRow1);
        this.tableLayoutC.addView(tableRow, tableRowCountC);
        tableRowCountC++;
    }

    private synchronized void initializeRowForTableD(int pos){
        TableRow tableRowB= new TableRow(getApplicationContext());
        TableRow.LayoutParams layoutParamsTableRow= new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, SCREEN_HEIGHT/20);
        tableRowB.setPadding(0,0,0,0);
        tableRowB.setLayoutParams(layoutParamsTableRow);
        this.tableLayoutD.addView(tableRowB, pos);
    }

    private synchronized void addColumnToTableAtD(final int rowPos, String text){
        TableRow tableRowAdd= (TableRow) this.tableLayoutD.getChildAt(rowPos);
        tableRow= new TableRow(getApplicationContext());
        TableRow.LayoutParams layoutParamsTableRow= new TableRow.LayoutParams(SCREEN_WIDTH/5, SCREEN_HEIGHT/20);
        tableRow.setPadding(3,3,3,4);
        tableRow.setBackgroundColor(Color.YELLOW);
        tableRow.setLayoutParams(layoutParamsTableRow);
        TextView label_date = new TextView(getApplicationContext());
        label_date.setText(text);
        label_date.setTextSize(getResources().getDimension(R.dimen.cell_text_size));
        tableRow.setTag(label_date);
        this.tableRow.addView(label_date);
        tableRowAdd.addView(tableRow);
    }

    private void createCompleteColumn(String value){
        int i=0;
        int j=tableRowCountC-1;
        for(int k=i; k<=j; k++){
            addColumnToTableAtD(k, value);
        }
    }

    private void createCompleteRow(String value){
        initializeRowForTableD(0);
        int i=0;
        int j=tableColumnCountB-1;
        int pos= tableRowCountC-1;
        for(int k=i; k<=j; k++){
            addColumnToTableAtD(pos, value);
        }
    }



}