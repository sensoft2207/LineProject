package com.mxi.lineapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mxi.lineapp.R;
import com.mxi.lineapp.network.CommonClass;

/**
 * Created by Akshay on 05-Mar-17.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    CommonClass cc;

    public static Integer[] mThumbIds = {R.mipmap.alpha_1,R.mipmap.alpha_2,R.mipmap.alpha_2_1,R.mipmap.alpha_2_2,R.mipmap.alpha_2_3,R.mipmap.alpha_2_4,R.mipmap.alpha_2_5,R.mipmap.alpha_2_6,R.mipmap.alpha_2_7,R.mipmap.alpha_2_8
            ,R.mipmap.alpha_2_9,R.mipmap.alpha_2_10,R.mipmap.alpha_2_11,R.mipmap.alpha_2_12,R.mipmap.alpha_2_13,R.mipmap.alpha_2_14,R.mipmap.alpha_2_15
            ,R.mipmap.alpha_2_16,R.mipmap.alpha_2_17,R.mipmap.alpha_2_18,R.mipmap.alpha_2_19,R.mipmap.alpha_2_20,R.mipmap.alpha_2_21,R.mipmap.alpha_2_22,R.mipmap.alpha_2_23,R.mipmap.alpha_3,R.mipmap.alpha_3_1,R.mipmap.alpha_3_2,R.mipmap.alpha_3_3
            ,R.mipmap.alpha_3_4,R.mipmap.alpha_4,R.mipmap.alpha_4_1,R.mipmap.alpha_5,R.mipmap.alpha_5_1,R.mipmap.alpha_6,R.mipmap.alpha_9,R.mipmap.alpha_12,R.mipmap.alpha_13
            ,R.mipmap.alpha_19,R.mipmap.alpha_21,R.mipmap.alpha_23,R.mipmap.alpha_25,R.mipmap.alpha_27,R.mipmap.alpha_28,R.mipmap.alpha_30,R.mipmap.alpha_31,R.mipmap.alpha_32,R.mipmap.alpha_33
            ,R.mipmap.alpha_34,R.mipmap.alpha_35,R.mipmap.alpha_36,R.mipmap.alpha_37,R.mipmap.alpha_38,R.mipmap.alpha_39,R.mipmap.alpha_40,R.mipmap.alpha_42,R.mipmap.alpha_43,R.mipmap.alpha_44,R.mipmap.alpha_45
            ,R.mipmap.alpha_46,R.mipmap.alpha_47,R.mipmap.america3mod_75,R.mipmap.americamg1mod_75,R.mipmap.amkar1mod_75,R.mipmap.angers1mod_75,R.mipmap.antlers1mod_75,R.mipmap.anzhi1mod_75
            ,R.mipmap.argentinos1mod_75,R.mipmap.arouca1mod_75,R.mipmap.arsenal1mod_75,R.mipmap.arsenal11mod_75,R.mipmap.asteras1mod_75,R.mipmap.atalanta1mod_75,R.mipmap.atlanta1mod_75
            ,R.mipmap.atlas1mod_75,R.mipmap.atlkolkata1mod_75,R.mipmap.atlmineiro1mod_75,R.mipmap.atlparanaense1mod_75,R.mipmap.atromitos1mod_75,R.mipmap.augsburg1mod_75,R.mipmap.avispa1mod_75
            ,R.mipmap.az1mod_75,R.mipmap.banfield1mod_75,R.mipmap.bastia1mod_75,R.mipmap.belgrano1mod_75,R.mipmap.bellmare1mod_75,R.mipmap.betis1mod_75,R.mipmap.boavista1mod_75
            ,R.mipmap.bolivia_1mod_75,R.mipmap.bologna1mod_75,R.mipmap.bordeaux1mod_75,R.mipmap.bosnia1mod_75,R.mipmap.botafogo1mod_75,R.mipmap.bournemouth1mod_75,R.mipmap.braga1mod_75,R.mipmap.bremen1mod_75,
            R.mipmap.brisbane1mod_75,R.mipmap.brisbane2mod_75
            ,R.mipmap.burnley1mod_75,R.mipmap.caen1mod_75,R.mipmap.cagliari1mod_75,R.mipmap.cameroon1mod_75,R.mipmap.canada1mod_75,R.mipmap.celta1mod_75,R.mipmap.central1mod_75,R.mipmap.changchun1mod_75
            ,R.mipmap.chaves1mod_75,R.mipmap.chennaiyin1mod_75,R.mipmap.chiapas1mod_75,R.mipmap.chievo1mod_75,R.mipmap.chile_1mod_75,R.mipmap.chivas1mod_75,R.mipmap.colombia_1mod_75,R.mipmap.colon1mod_75,R.mipmap.colorado1mod_75
            ,R.mipmap.columbus1mod_75,R.mipmap.corinthians2mod_75,R.mipmap.coritiba1mod_75,R.mipmap.croatia1mod_75,R.mipmap.crotone1mod_75,R.mipmap.cruzazul1mod_75,R.mipmap.cruzeiro1mod_75
            ,R.mipmap.cruzeiro2mod_75,R.mipmap.crystalpalace1mod_75,R.mipmap.czech1mod_75,R.mipmap.dallas1mod_75,R.mipmap.dc1mod_75,R.mipmap.delhi2mod_75,R.mipmap.denmark1mod_75
            ,R.mipmap.deportivo1mod_75,R.mipmap.dijon1mod_75,R.mipmap.dorados1mod_75,R.mipmap.dyj1mod_75,R.mipmap.dynamo1mod_75,R.mipmap.ecuador_1mod_75,R.mipmap.egypt1mod_75,R.mipmap.eibar1mod_75
            ,R.mipmap.empoli1mod_75,R.mipmap.espanyol1mod_75,R.mipmap.estoril1mod_75,R.mipmap.estudiantes1mod_75,R.mipmap.everton1mod_75,R.mipmap.excelsior1mod_75,R.mipmap.feirense1mod_75
            ,R.mipmap.finland1mod_75,R.mipmap.fiorentina1mod_75,R.mipmap.fire1mod_75,R.mipmap.flamengo2mod_75,R.mipmap.fluminense1mod_75,R.mipmap.frankfurt1mod_75,R.mipmap.freiburg1mod_75
            ,R.mipmap.frontale1mod_75,R.mipmap.fuli1mod_75,R.mipmap.gaeagles1mod_75,R.mipmap.gamba1mod_75,R.mipmap.genoa1mod_75,R.mipmap.ghana1mod_75,R.mipmap.gijon1mod_75,R.mipmap.gimnasia1mod_75
            ,R.mipmap.godoy1mod_75,R.mipmap.grampus1mod_75,R.mipmap.granada1mod_75,R.mipmap.greece1mod_75,R.mipmap.greentown1mod_75,R.mipmap.gremio1mod_75,R.mipmap.groningen1mod_75
            ,R.mipmap.guingamp1mod_75,R.mipmap.guoan1mod_75,R.mipmap.hamburger1mod_75,R.mipmap.heerenveen1mod_75,R.mipmap.heracles1mod_75,R.mipmap.hertha1mod_75,R.mipmap.hoffenheim1mod_75,R.mipmap.honduras1mod_75
            ,R.mipmap.hull1mod_75,R.mipmap.huracan1mod_75,R.mipmap.iceland1mod_75,R.mipmap.ingolstadt1mod_75,R.mipmap.internacional1mod_75,R.mipmap.iraklis1mod_75,R.mipmap.ireland2mod_75,R.mipmap.ivorycoast1mod_75,R.mipmap.jamaica1mod_75
            ,R.mipmap.jiangsu1mod_75,R.mipmap.jubilo1mod_75,R.mipmap.kansas1mod_75,R.mipmap.kerala1mod_75,R.mipmap.kerkyra1mod_75,R.mipmap.koln1mod_75,R.mipmap.kosovo1mod_75,R.mipmap.krasnodar1mod_75,R.mipmap.krylia1mod_75
            ,R.mipmap.laspalmas1mod_75,R.mipmap.leganes1mod_75,R.mipmap.leicester1mod_75,R.mipmap.leipzig1mod_75,R.mipmap.leon1mod_75,R.mipmap.liberia1mod_75,R.mipmap.lille1mod_75,R.mipmap.lithuania1mod_75
            ,R.mipmap.lokomotiv1mod_75,R.mipmap.lorient1mod_75,R.mipmap.mainz1mod_75,R.mipmap.malaga1mod_75,R.mipmap.malta1mod_75,R.mipmap.marinos1mod_75,R.mipmap.maritimo1mod_75,R.mipmap.melbourne1mod_75
            ,R.mipmap.melbourne2mod_75,R.mipmap.metz1mod_75,R.mipmap.middlesbrough1mod_75,R.mipmap.mnut1mod_75,R.mipmap.monchengladbach1mod_75,R.mipmap.monterrey1mod_75,R.mipmap.montpellier1mod_75,R.mipmap.montreal1mod_75
            ,R.mipmap.moreirense1mod_75,R.mipmap.morelia1mod_75,R.mipmap.morocco1mod_75,R.mipmap.mumbai1mod_75,R.mipmap.nacional1mod_75,R.mipmap.nancy1mod_75,R.mipmap.nantes1mod_75
            ,R.mipmap.nec1mod_75,R.mipmap.newcastlejets1mod_75,R.mipmap.newells1mod_75,R.mipmap.nice1mod_75,R.mipmap.nigeria1mod_75,R.mipmap.northeast1mod_75,R.mipmap.norway1mod_75
            ,R.mipmap.nyc1mod_75,R.mipmap.olimpo1mod_75,R.mipmap.olympiakos1mod_75,R.mipmap.omiya1mod_75,R.mipmap.orenburg1mod_75,R.mipmap.orlando2mod_75,R.mipmap.osasuna1mod_75
            ,R.mipmap.pachuca1mod_75,R.mipmap.pacosferreira1mod_75,R.mipmap.palermo1mod_75,R.mipmap.palmeiras1mod_75,R.mipmap.panathinaikos1mod_75,R.mipmap.panionios1mod_75,R.mipmap.paok1mod_75
            ,R.mipmap.paraguay_1mod_75,R.mipmap.pasgiannina1mod_75,R.mipmap.patronato1mod_75,R.mipmap.perthglory1mod_75,R.mipmap.peru_1mod_75,R.mipmap.pescara1mod_75,R.mipmap.pontepreta1mod_75
            ,R.mipmap.portland2mod_75,R.mipmap.portugal1mod_75,R.mipmap.psv1mod_75,R.mipmap.puerto_rico1mod_75,R.mipmap.pumas1mod_75,R.mipmap.punecity1mod_75,R.mipmap.queretaro1mod_75,R.mipmap.racing1mod_75
            ,R.mipmap.rennais1mod_75,R.mipmap.revs1mod_75,R.mipmap.reysol1mod_75,R.mipmap.rioave1mod_75,R.mipmap.roda1mod_75,R.mipmap.romania1mod_75,R.mipmap.rostov1mod_75,R.mipmap.rubin1mod_75
            ,R.mipmap.russia1mod_75,R.mipmap.sagantosu1mod_75,R.mipmap.sampdoria1mod_75,R.mipmap.sanfrecce1mod_75,R.mipmap.sanjose1mod_75,R.mipmap.santacruz1mod_75,R.mipmap.santos2mod_75,R.mipmap.santos11mod_75
            ,R.mipmap.saopaulo1mod_75,R.mipmap.saopaulo2mod_75,R.mipmap.sassuolo1mod_75,R.mipmap.scotland1mod_75,R.mipmap.sendai1mod_75,R.mipmap.senegal1mod_75,R.mipmap.serbia1mod_75
            ,R.mipmap.setubal1mod_75,R.mipmap.shandong1mod_75,R.mipmap.slovenia1mod_75,R.mipmap.sociedad1mod_75,R.mipmap.sounders1mod_75,R.mipmap.southafrica1mod_75,R.mipmap.southampton1mod_75
            ,R.mipmap.sparta1mod_75,R.mipmap.spartak1mod_75,R.mipmap.sport1mod_75,R.mipmap.sporting1mod_75,R.mipmap.stetienne1mod_75,R.mipmap.stoke1mod_75,R.mipmap.sunderland1mod_75
            ,R.mipmap.swansea1mod_75,R.mipmap.sweden1mod_75,R.mipmap.switzerland1mod_75,R.mipmap.sydney1mod_75,R.mipmap.terek1mod_75,R.mipmap.tigres1mod_75,R.mipmap.tijuana1mod_75
            ,R.mipmap.tokyo1mod_75,R.mipmap.toluca1mod_75,R.mipmap.tom1mod_75,R.mipmap.tondela1mod_75,R.mipmap.torino1mod_75,R.mipmap.toronto1mod_75,R.mipmap.toulouse1mod_75
            ,R.mipmap.tunisia1mod_75,R.mipmap.turkey1mod_75,R.mipmap.twente1mod_75,R.mipmap.udinese1mod_75,R.mipmap.ufa1mod_75,R.mipmap.ukraine1mod_75,R.mipmap.union1mod_75
            ,R.mipmap.ural1mod_75,R.mipmap.urawa1mod_75,R.mipmap.uruguay_1mod_75,R.mipmap.usa1mod_75,R.mipmap.utrecht1mod_75,R.mipmap.valencia2mod_75,R.mipmap.velez1mod_75
            ,R.mipmap.venezuela_1mod_75,R.mipmap.ventforet1mod_75,R.mipmap.veracruz1mod_75,R.mipmap.veria1mod_75,R.mipmap.victory1mod_75,R.mipmap.villarreal1mod_75,R.mipmap.visselkobe1mod_75
            ,R.mipmap.vitesse1mod_75,R.mipmap.vitoria1mod_75,R.mipmap.vitoria11mod_75,R.mipmap.wales1mod_75,R.mipmap.wanderers1mod_75,R.mipmap.watford1mod_75,R.mipmap.wba1mod_75
            ,R.mipmap.wellington1mod_75,R.mipmap.westham1mod_75,R.mipmap.whitecaps1mod_75,R.mipmap.willem1mod_75,R.mipmap.xanthi1mod_75,R.mipmap.zenit1mod_75,R.mipmap.zwolle1mod_75};


    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
        cc = new CommonClass(c);
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (imageView.getWidth() > 0) {
                    cc.savePrefInt("imageViewWidth", imageView.getWidth());
                    Log.e("ImageView Width", imageView.getWidth() + "");
                }

            }
        });

        return imageView;
    }

}