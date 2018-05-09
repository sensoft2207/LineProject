package com.mxi.lineapp.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mxi.lineapp.R;
import com.mxi.lineapp.dragevent.DragController;
import com.mxi.lineapp.dragevent.DragLayer;
import com.mxi.lineapp.network.CommonClass;

import java.io.File;

/**
 * Created by vishal on 26/3/18.
 */

public class SmileyActivityTwo extends AppCompatActivity implements  View.OnDragListener, View.OnClickListener{

    CommonClass cc;

    ImageView iv_capture_image;

    Bitmap bitmapFormation;

    FrameLayout fl_smiley_layout;

    GridView gridSmiley;

    LinearLayout ln_smiley_one,ln_smiley_two,ln_smiley_three,ln_smiley_four;

    int x_touch = 0;
    int y_touch = 0;

    int x_touch_bottom = 0;
    int y_touch_bottom = 0;


    private DragController mDragController,mDragControllerBottom;   // Object that sends out drag-drop events while a view is being moved.
    private DragLayer mDragLayer;

    int positionMain;

    String smiley_first;

    int item2 ;

    LinearLayout ln_delete_bottom,ln_share_screen;

    LinearLayout ln_drop_delete;

    ImageView shape;

    View viewShap;

     static int[] osImagesOne = {R.mipmap.a1,R.mipmap.a2,R.mipmap.a4,R.mipmap.a5,R.mipmap.a6,R.mipmap.a7,R.mipmap.a8,R.mipmap.a9,R.mipmap.a10
            ,R.mipmap.a11,R.mipmap.a12,R.mipmap.a13,R.mipmap.a14,R.mipmap.a15,R.mipmap.a16,R.mipmap.a17,R.mipmap.a18,R.mipmap.a19,R.mipmap.a20
            ,R.mipmap.a21,R.mipmap.a22,R.mipmap.a23,R.mipmap.a24,R.mipmap.a25,R.mipmap.a26,R.mipmap.a27,R.mipmap.a28,R.mipmap.a29,R.mipmap.a30
            ,R.mipmap.a31,R.mipmap.a32,R.mipmap.a33,R.mipmap.a34,R.mipmap.a35,R.mipmap.a36,R.mipmap.a37,R.mipmap.a38,R.mipmap.a39,R.mipmap.a40
            ,R.mipmap.a41,R.mipmap.a42,R.mipmap.a43,R.mipmap.a44,R.mipmap.a45,R.mipmap.a46,R.mipmap.a47,R.mipmap.a48,R.mipmap.a49,R.mipmap.a50
            ,R.mipmap.a51,R.mipmap.a52,R.mipmap.a53,R.mipmap.a54,R.mipmap.a55,R.mipmap.a56,R.mipmap.a57,R.mipmap.a58,R.mipmap.a59,R.mipmap.a60
            ,R.mipmap.a61,R.mipmap.a62,R.mipmap.a63,R.mipmap.a64,R.mipmap.a65,R.mipmap.a66,R.mipmap.a67,R.mipmap.a68,R.mipmap.a69,R.mipmap.a70
            ,R.mipmap.a71,R.mipmap.a72,R.mipmap.a73,R.mipmap.a74,R.mipmap.a75,R.mipmap.a76,R.mipmap.a77,R.mipmap.a78,R.mipmap.a79,R.mipmap.a80
            ,R.mipmap.a81,R.mipmap.a83,R.mipmap.a84,R.mipmap.a85,R.mipmap.a86,R.mipmap.a87,R.mipmap.a88,R.mipmap.a89,R.mipmap.a90
            ,R.mipmap.a91,R.mipmap.a92,R.mipmap.a93,R.mipmap.a94,R.mipmap.a95,R.mipmap.a96,R.mipmap.a97,R.mipmap.a98,R.mipmap.a99,R.mipmap.a100
            ,R.mipmap.a101,R.mipmap.a102,R.mipmap.a103,R.mipmap.a104,R.mipmap.a105,R.mipmap.a106,R.mipmap.a107,R.mipmap.a108,R.mipmap.a109,R.mipmap.a110
            ,R.mipmap.a111,R.mipmap.a112,R.mipmap.a113,R.mipmap.a114,R.mipmap.a115,R.mipmap.a116,R.mipmap.a117,R.mipmap.a118,R.mipmap.a119,R.mipmap.a120
            ,R.mipmap.a121,R.mipmap.a122,R.mipmap.a123,R.mipmap.a124,R.mipmap.a125,R.mipmap.a126,R.mipmap.a127,R.mipmap.a128,R.mipmap.a129,R.mipmap.a130
            ,R.mipmap.a131,R.mipmap.a132,R.mipmap.a133,R.mipmap.a134,R.mipmap.a135,R.mipmap.a136,R.mipmap.a137,R.mipmap.a138,R.mipmap.a139,R.mipmap.a140
            ,R.mipmap.a141,R.mipmap.a142,R.mipmap.a143,R.mipmap.a144,R.mipmap.a145,R.mipmap.a146,R.mipmap.a147,R.mipmap.a148,R.mipmap.a149,R.mipmap.a150
            ,R.mipmap.a151,R.mipmap.a152,R.mipmap.a153,R.mipmap.a154,R.mipmap.a155,R.mipmap.a156,R.mipmap.a157,R.mipmap.a158,R.mipmap.a159,R.mipmap.a160
            ,R.mipmap.a161,R.mipmap.a162,R.mipmap.a163,R.mipmap.a164,R.mipmap.a165,R.mipmap.a166,R.mipmap.a167,R.mipmap.a168,R.mipmap.a169,R.mipmap.a170
            ,R.mipmap.a171,R.mipmap.a172,R.mipmap.a173,R.mipmap.a174,R.mipmap.a175,R.mipmap.a176,R.mipmap.a177,R.mipmap.a178,R.mipmap.a179,R.mipmap.a180
            ,R.mipmap.a181,R.mipmap.a182,R.mipmap.a183,R.mipmap.a184,R.mipmap.a185,R.mipmap.a186,R.mipmap.a187,R.mipmap.a188,R.mipmap.a189,R.mipmap.a190
            ,R.mipmap.a191,R.mipmap.a192,R.mipmap.a193,R.mipmap.a194,R.mipmap.a195,R.mipmap.a196,R.mipmap.a197,R.mipmap.a198,R.mipmap.a199,R.mipmap.a200
            ,R.mipmap.a201,R.mipmap.a202,R.mipmap.a203,R.mipmap.a204
            ,R.mipmap.fsg_1,R.mipmap.fsg_2,R.mipmap.fsg_3,R.mipmap.fsg_4,R.mipmap.fsg_5,R.mipmap.fsg_6,R.mipmap.fsg_7,R.mipmap.fsg_8,R.mipmap.fsg_9,R.mipmap.fsg_10
            ,R.mipmap.fsg_11,R.mipmap.fsg_12,R.mipmap.fsg_13,R.mipmap.fsg_14,R.mipmap.fsg_15,R.mipmap.fsg_16,R.mipmap.fsg_17,R.mipmap.fsg_18,R.mipmap.fsg_19,R.mipmap.fsg_20
            ,R.mipmap.fsg_21,R.mipmap.fsg_22,R.mipmap.fsg_23,R.mipmap.fsg_24,R.mipmap.fsg_25,R.mipmap.fsg_26,R.mipmap.fsg_27,R.mipmap.fsg_28,R.mipmap.fsg_29,R.mipmap.fsg_30
            ,R.mipmap.fsg_31,R.mipmap.fsg_32,R.mipmap.fsg_33,R.mipmap.fsg_34,R.mipmap.fsg_35,R.mipmap.fsg_36,R.mipmap.fsg_37,R.mipmap.fsg_38,R.mipmap.fsg_39,R.mipmap.fsg_40
            ,R.mipmap.fsg_41,R.mipmap.fsg_42,R.mipmap.fsg_43,R.mipmap.fsg_44,R.mipmap.fsg_45,R.mipmap.fsg_46,R.mipmap.fsg_47,R.mipmap.fsg_48,R.mipmap.fsg_49,R.mipmap.fsg_50
            ,R.mipmap.fsg_51,R.mipmap.fsg_52,R.mipmap.fsg_53,R.mipmap.fsg_54,R.mipmap.fsg_55,R.mipmap.fsg_56,R.mipmap.fsg_57,R.mipmap.fsg_58,R.mipmap.fsg_59,R.mipmap.fsg_60
            ,R.mipmap.f1,R.mipmap.f2,R.mipmap.f3,R.mipmap.f4,R.mipmap.f5,R.mipmap.f6,R.mipmap.f7,R.mipmap.f8,R.mipmap.f9,R.mipmap.f10
            ,R.mipmap.f11,R.mipmap.f12,R.mipmap.f13,R.mipmap.f14,R.mipmap.f15,R.mipmap.f16,R.mipmap.f17,R.mipmap.f18,R.mipmap.f19,R.mipmap.f20
            ,R.mipmap.f21,R.mipmap.f22,R.mipmap.f23,R.mipmap.f24,R.mipmap.f25,R.mipmap.f26,R.mipmap.f27,R.mipmap.f28,R.mipmap.f29,R.mipmap.f30
            ,R.mipmap.f31,R.mipmap.f32,R.mipmap.f33,R.mipmap.f34,R.mipmap.f35,R.mipmap.f36,R.mipmap.f37,R.mipmap.f38,R.mipmap.f39,R.mipmap.f40
            ,R.mipmap.f41,R.mipmap.f42,R.mipmap.f43,R.mipmap.f44,R.mipmap.f45,R.mipmap.f46,R.mipmap.f47,R.mipmap.f48,R.mipmap.f49,R.mipmap.f50
            ,R.mipmap.f51,R.mipmap.f52,R.mipmap.f53,R.mipmap.f54,R.mipmap.f55,R.mipmap.f56,R.mipmap.f57,R.mipmap.f58,R.mipmap.f59,R.mipmap.f60
            ,R.mipmap.f61,R.mipmap.f62,R.mipmap.f63,R.mipmap.f64,R.mipmap.f65,R.mipmap.f66,R.mipmap.f67,R.mipmap.f68,R.mipmap.f69,R.mipmap.f70
            ,R.mipmap.f71,R.mipmap.f72,R.mipmap.f73,R.mipmap.f74,R.mipmap.f75,R.mipmap.f76,R.mipmap.f77,R.mipmap.f78,R.mipmap.f79,R.mipmap.f80
            ,R.mipmap.f81,R.mipmap.f82,R.mipmap.f83,R.mipmap.f84,R.mipmap.f85,R.mipmap.f86,R.mipmap.f87,R.mipmap.f88,R.mipmap.f89,R.mipmap.f90
            ,R.mipmap.f91,R.mipmap.f92,R.mipmap.f93,R.mipmap.f94,R.mipmap.f95,R.mipmap.f96,R.mipmap.f97,R.mipmap.f98,R.mipmap.f99,R.mipmap.f100
            ,R.mipmap.f101,R.mipmap.f102,R.mipmap.f103,R.mipmap.f104,R.mipmap.f105,R.mipmap.f106,R.mipmap.f107,R.mipmap.f108,R.mipmap.f109,R.mipmap.f110
            ,R.mipmap.f111,R.mipmap.f112,R.mipmap.f113,R.mipmap.f114,R.mipmap.f115,R.mipmap.f116,R.mipmap.f117,R.mipmap.f118,R.mipmap.f119,R.mipmap.f120
            ,R.mipmap.f121,R.mipmap.f122,R.mipmap.f123,R.mipmap.f124,R.mipmap.f125,R.mipmap.f126,R.mipmap.f127,R.mipmap.f128,R.mipmap.f129,R.mipmap.f130
            ,R.mipmap.f131,R.mipmap.f132,R.mipmap.f133,R.mipmap.f134,R.mipmap.f135,R.mipmap.f136,R.mipmap.f137,R.mipmap.f138,R.mipmap.f139,R.mipmap.f140
            ,R.mipmap.f141,R.mipmap.f142,R.mipmap.f143,R.mipmap.f144,R.mipmap.f145,R.mipmap.f146,R.mipmap.f147,R.mipmap.f148,R.mipmap.f149,R.mipmap.f150
            ,R.mipmap.f151,R.mipmap.f152,R.mipmap.f153,R.mipmap.f154,R.mipmap.f155,R.mipmap.f156,R.mipmap.f157,R.mipmap.f158,R.mipmap.f159,R.mipmap.f160
            ,R.mipmap.f161,R.mipmap.f162,R.mipmap.f163,R.mipmap.f164,R.mipmap.f165,R.mipmap.f166,R.mipmap.f167,R.mipmap.f168,R.mipmap.f169,R.mipmap.f170
            ,R.mipmap.f171,R.mipmap.f172,R.mipmap.f173,R.mipmap.f174,R.mipmap.f175,R.mipmap.f176,R.mipmap.f177,R.mipmap.f178,R.mipmap.f179,R.mipmap.f180
            ,R.mipmap.f181,R.mipmap.f182,R.mipmap.f183,R.mipmap.f184,R.mipmap.f185,R.mipmap.f186,R.mipmap.f187,R.mipmap.f188,R.mipmap.f189,R.mipmap.f190
            ,R.mipmap.f191,R.mipmap.f192,R.mipmap.f193,R.mipmap.f194,R.mipmap.f195};

    public static int[] osImagesTwo = {R.mipmap.n0,R.mipmap.n1,R.mipmap.n2,R.mipmap.n3,R.mipmap.n4,R.mipmap.n5,R.mipmap.n6,R.mipmap.n7,R.mipmap.n8,R.mipmap.n9,R.mipmap.n10
            ,R.mipmap.n11,R.mipmap.n12,R.mipmap.n13,R.mipmap.n14,R.mipmap.n15,R.mipmap.n16,R.mipmap.n17,R.mipmap.n18,R.mipmap.n19,R.mipmap.n20
            ,R.mipmap.n21,R.mipmap.n22,R.mipmap.n23,R.mipmap.n24,R.mipmap.n25,R.mipmap.n26,R.mipmap.n27,R.mipmap.n28,R.mipmap.n29,R.mipmap.n30
            ,R.mipmap.n31,R.mipmap.n32,R.mipmap.n33,R.mipmap.n34,R.mipmap.n35,R.mipmap.n36,R.mipmap.n37,R.mipmap.n38,R.mipmap.n39,R.mipmap.n40
            ,R.mipmap.n41,R.mipmap.n42,R.mipmap.n43,R.mipmap.n44,R.mipmap.n45,R.mipmap.n46,R.mipmap.n47,R.mipmap.n48,R.mipmap.n49,R.mipmap.n50
            ,R.mipmap.n51,R.mipmap.n52,R.mipmap.n53,R.mipmap.n54,R.mipmap.n55,R.mipmap.n56,R.mipmap.n57,R.mipmap.n58,R.mipmap.n59,R.mipmap.n60
            ,R.mipmap.n61,R.mipmap.n62,R.mipmap.n63,R.mipmap.n64,R.mipmap.n65,R.mipmap.n66,R.mipmap.n67,R.mipmap.n68,R.mipmap.n69,R.mipmap.n70
            ,R.mipmap.n71,R.mipmap.n72,R.mipmap.n73,R.mipmap.n74,R.mipmap.n75,R.mipmap.n76,R.mipmap.n77,R.mipmap.n78,R.mipmap.n79,R.mipmap.n80
            ,R.mipmap.n81,R.mipmap.n82,R.mipmap.n83,R.mipmap.n84,R.mipmap.n85,R.mipmap.n86,R.mipmap.n87,R.mipmap.n88,R.mipmap.n89,R.mipmap.n90
            ,R.mipmap.n91,R.mipmap.n92,R.mipmap.n93,R.mipmap.n94,R.mipmap.n95,R.mipmap.n96,R.mipmap.n97,R.mipmap.n98,R.mipmap.n99,R.mipmap.n100
            ,R.mipmap.n101,R.mipmap.n102,R.mipmap.n103,R.mipmap.n104,R.mipmap.n105,R.mipmap.n106,R.mipmap.n107,R.mipmap.n108,R.mipmap.n109,R.mipmap.n110
            ,R.mipmap.n111,R.mipmap.n112,R.mipmap.n113,R.mipmap.n114,R.mipmap.n115,R.mipmap.n116,R.mipmap.n117,R.mipmap.n118,R.mipmap.n119,R.mipmap.n120
            ,R.mipmap.n121,R.mipmap.n122,R.mipmap.n123,R.mipmap.n124,R.mipmap.n125,R.mipmap.n126,R.mipmap.n127,R.mipmap.n128,R.mipmap.n129,R.mipmap.n130
            ,R.mipmap.n131,R.mipmap.n132,R.mipmap.n133,R.mipmap.n134,R.mipmap.n135,R.mipmap.n136,R.mipmap.n137,R.mipmap.n138,R.mipmap.n139};

    public static int[] osImagesThree = {R.mipmap.ft_1,R.mipmap.ft_2,R.mipmap.ft_3,R.mipmap.ft_4,R.mipmap.ft_5,R.mipmap.ft_6,R.mipmap.ft_7,R.mipmap.ft_8,R.mipmap.ft_9,R.mipmap.ft_10
            ,R.mipmap.ft_11,R.mipmap.ft_12,R.mipmap.ft_13,R.mipmap.ft_14,R.mipmap.ft_15,R.mipmap.ft_16,R.mipmap.ft_17,R.mipmap.ft_18,R.mipmap.ft_19,R.mipmap.ft_20
            ,R.mipmap.ft_21,R.mipmap.ft_22,R.mipmap.ft_23,R.mipmap.ft_24,R.mipmap.ft_25,R.mipmap.ft_26,R.mipmap.ft_27,R.mipmap.ft_28,R.mipmap.ft_29,R.mipmap.ft_30
            ,R.mipmap.ft_31,R.mipmap.ft_32,R.mipmap.ft_33,R.mipmap.ft_34,R.mipmap.ft_35,R.mipmap.ft_36,R.mipmap.ft_37,R.mipmap.ft_38,R.mipmap.ft_39,R.mipmap.ft_40
            ,R.mipmap.ft_41,R.mipmap.ft_42,
            R.mipmap.start1,R.mipmap.start2,R.mipmap.start3,R.mipmap.start4,R.mipmap.start5,R.mipmap.start6,R.mipmap.start7,R.mipmap.start8,R.mipmap.start9,R.mipmap.start10
    };

    public static int[] osImagesFour = {R.mipmap.p1,R.mipmap.p2,R.mipmap.p3,R.mipmap.p4,R.mipmap.p5,R.mipmap.p6,R.mipmap.p7,R.mipmap.p8,R.mipmap.p9,R.mipmap.p10
            ,R.mipmap.p11,R.mipmap.p12,R.mipmap.p13,R.mipmap.p14,R.mipmap.p15,R.mipmap.p16,R.mipmap.p17,R.mipmap.p18,R.mipmap.p19,R.mipmap.p20,R.mipmap.p21};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smiley_activity_two);

        init();

    }

    private void init() {

        mDragController = new DragController(this);
        mDragControllerBottom = new DragController(this);

        fl_smiley_layout = (FrameLayout) findViewById(R.id.ln_main_drop);

        iv_capture_image = (ImageView)findViewById(R.id.iv_capture_image);

        ln_smiley_one = (LinearLayout) findViewById(R.id.ln_smiley_one);
        ln_smiley_two = (LinearLayout) findViewById(R.id.ln_smiley_two);
        ln_smiley_three = (LinearLayout) findViewById(R.id.ln_smiley_three);
        ln_smiley_four = (LinearLayout) findViewById(R.id.ln_smiley_four);
        ln_delete_bottom = (LinearLayout) findViewById(R.id.ln_delete_bottom);
        ln_drop_delete = (LinearLayout) findViewById(R.id.ln_drop_delete);
        ln_share_screen = (LinearLayout) findViewById(R.id.ln_share_screen);


        gridSmiley = (GridView) findViewById(R.id.smiley_grid);
        gridSmiley.setAdapter(new CustomAdapter(getApplicationContext(),osImagesThree));

        smiley_first = "one";

        DragController dragController = mDragController;
        DragController dragControllerBottom = mDragControllerBottom;

        mDragLayer = (DragLayer) findViewById(R.id.fl_drag_view);
        mDragLayer.setOnDragListener(this);
        mDragLayer.setDragController(dragController);
        dragController.addDropTarget(mDragLayer);



        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images/Image.jpg");
        Bitmap bmp = BitmapFactory.decodeFile(myDir.getAbsolutePath());
        iv_capture_image.setImageBitmap(bmp);


        ln_smiley_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gridSmiley = (GridView) findViewById(R.id.smiley_grid);
                gridSmiley.setAdapter(new CustomAdapter(getApplicationContext(),osImagesThree));

                smiley_first = "one";
            }
        });

        ln_smiley_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gridSmiley = (GridView) findViewById(R.id.smiley_grid);
                gridSmiley.setAdapter(new CustomAdapter(getApplicationContext(),osImagesOne));

                smiley_first = "two";
            }
        });

        ln_smiley_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gridSmiley = (GridView) findViewById(R.id.smiley_grid);
                gridSmiley.setAdapter(new CustomAdapter(getApplicationContext(),osImagesTwo));

                smiley_first = "three";
            }
        });

        ln_smiley_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gridSmiley = (GridView) findViewById(R.id.smiley_grid);
                gridSmiley.setAdapter(new CustomAdapter(getApplicationContext(),osImagesFour));

                smiley_first = "four";
            }
        });


        ln_drop_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                mDragLayer.removeView(viewShap);

                ln_delete_bottom.setVisibility(View.INVISIBLE);


            }
        });

        ln_share_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareDialog();
            }
        });

    }

    private void shareDialog() {

        ln_delete_bottom.setVisibility(View.INVISIBLE);

        fl_smiley_layout.destroyDrawingCache();
        fl_smiley_layout.setDrawingCacheEnabled(true);
        Bitmap bitmap = fl_smiley_layout.getDrawingCache();

        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"Share", null);
        Uri bitmapUri = Uri.parse(bitmapPath);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_by));
        startActivity(Intent.createChooser(intent, "Share"));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onDrag(View view, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
//                fl_drag_view.setBackgroundColor(GREEN);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
//                fl_drag_view.setBackgroundColor(RED);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
//                fl_drag_view.setBackgroundColor(WHITE);
                break;
            case DragEvent.ACTION_DROP:
                Log.e("Drop", "Yes its Activity Drop");
                final float dropX = event.getX();
                final float dropY = event.getY();
                final DragData state = (DragData) event.getLocalState();

                final RelativeLayout ln_main = (RelativeLayout) LayoutInflater.from(this).inflate(
                        R.layout.smiley_grid_item, mDragLayer, false);


                shape = (ImageView)ln_main.findViewById(R.id.os_images);
                shape.setImageResource(state.item);

                if(shape.getParent()!=null){

                    ((ViewGroup)shape.getParent()).removeView(shape); // <- fix

                }else {

                    ln_main.addView(shape);

                }

                shape.setOnClickListener(this);

                int x = (int) (dropX - (float) state.width / 2);
                int y = (int) (dropY - (float) state.height / 2);
                DragLayer.LayoutParams params;

                params = new DragLayer.LayoutParams(DragLayer.LayoutParams.WRAP_CONTENT, DragLayer.LayoutParams.WRAP_CONTENT, x, y);

                if(shape.getParent()!=null){

                    ((ViewGroup)shape.getParent()).removeView(shape); // <- fix

                }else {

                    mDragLayer.addView(shape, params);

                }

                /*Pending vishal.................*/


                shape.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent ev) {
                        boolean handledHere = false;

                        final int action = ev.getAction();

                        switch (action) {
                            case MotionEvent.ACTION_DOWN:

                                x_touch = (int) ev.getX();
                                y_touch = (int) ev.getY();

                                handledHere = startDrag(v);
                                if (handledHere) {
                                    v.performClick();
                                }

                                break;

                            case MotionEvent.ACTION_UP:
                                x_touch = (int) ev.getX();
                                y_touch = (int) ev.getY();

                                break;
                        }

                        return handledHere;
                    }
                });


                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(final View view_main) {

        viewShap = view_main;



     /*   if (smiley_first.equals("one")){

            item2 = osImagesOne[positionMain];

        }else if (smiley_first.equals("two")){

            item2 = osImagesTwo[positionMain];

        }else if (smiley_first.equals("three")){

            item2 = osImagesThree[positionMain];

        }else if (smiley_first.equals("four")){

            item2 = osImagesFour[positionMain];

        }*/

        /*final DragData state = new DragData(item2, view_main.getWidth(), view_main.getHeight());
        final View.DragShadowBuilder shadow = new View.DragShadowBuilder(view_main);
        ViewCompat.startDragAndDrop(view_main, null, shadow, state, 0);
*/


        ln_delete_bottom.setVisibility(View.VISIBLE);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                ln_delete_bottom.setVisibility(View.INVISIBLE);

            }
        }, 3000);


        Log.e("@@ONLONG","Delete");

    }

  public class CustomAdapter extends BaseAdapter {

        Context context;
        int [] imageId;

        int item;

        private  LayoutInflater inflater=null;

        public CustomAdapter(Context contextt,int[] osImages) {
            // TODO Auto-generated constructor stub

            context = contextt;
            imageId=osImages;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageId.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            ImageView os_img;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.smiley_grid_item, null);
            holder.os_img =(ImageView) rowView.findViewById(R.id.os_images);

            holder.os_img.setImageResource(imageId[position]);

            final View shape = holder.os_img;

            holder.os_img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (smiley_first.equals("one")){

                        item = osImagesThree[position];

                    }else if (smiley_first.equals("two")){

                        item = osImagesOne[position];

                    }else if (smiley_first.equals("three")){

                        item = osImagesTwo[position];

                    }else if (smiley_first.equals("four")){

                        item = osImagesFour[position];

                    }

                    final DragData state = new DragData(item, shape.getWidth(), shape.getHeight());
                    final View.DragShadowBuilder shadow = new View.DragShadowBuilder(shape);
                    ViewCompat.startDragAndDrop(shape, null, shadow, state, 0);

                    positionMain = position;

                    return true;
                }
            });

            return rowView;
        }

    }


    public class DragData {

        public final int item;
        public final int width;
        public final int height;

        public DragData(int item, int width, int height) {
            this.item = item;
            this.width = width;
            this.height = height;
        }

    }

    public boolean startDrag(View v) {

        Object obj = v;
        mDragController.startDrag(v, mDragLayer, obj, DragController.DRAG_ACTION_MOVE);

        return true;

    }



}
