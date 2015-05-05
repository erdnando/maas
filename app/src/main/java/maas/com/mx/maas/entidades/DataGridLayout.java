package maas.com.mx.maas.entidades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import maas.com.mx.maas.R;
import maas.com.mx.maas.frmBandeja;
import maas.com.mx.maas.frmMenuPrincipal;
import maas.com.mx.maas.frmNuevaSolicitud;
import maas.com.mx.maas.negocio.Negocio;

/**
 * Created by damserver on 22/04/2015.
 */
public class DataGridLayout extends RelativeLayout {

    public final String TAG = "DataGridLayout.java";

    // set the header titles
    String headers[] = {
            "Estatus ",
            "ID Solicitud ",
            "Nombre ",
            "Fecha alta ",
            "Comentario ",
    };

    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;

    ScrollView scrollViewC;
    ScrollView scrollViewD;

    Context context;
    ArrayList<Solicitud> solicitudes;

    List<Header> sampleObjects;// = this.headers();

    int headerCellsWidth[] = new int[headers.length];


    public DataGridLayout(Context context,ArrayList<Solicitud> _solicitudes) {

        super(context);

        this.context = context;

        this.solicitudes = _solicitudes;
        this.sampleObjects = this.headers();
        // initialize the main components (TableLayouts, HorizontalScrollView, ScrollView)
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();


        // no need to assemble component A, since it is just a table
        this.horizontalScrollViewB.addView(this.tableB);

        this.scrollViewC.addView(this.tableC);

        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);

        // add the components to be part of the main layout
        this.addComponentToMainLayout();
        this.setBackgroundColor(Color.TRANSPARENT);


        // add some table rows
        this.addTableRowToTableA();
        this.addTableRowToTableB();

        this.resizeHeaderHeight();

        this.getTableRowHeaderCellWidth();

        this.generateTableC_AndTable_B();

        this.resizeBodyTableRowHeight();
    }

    // this is just the sample data
    List<Header> headers(){
        List<Header> sampleObjectsx = new ArrayList<Header>();
        try {
            for (Solicitud sol: solicitudes) {

                Header sampleObject = new Header(
                        sol.Color.toString(),
                        sol.IdSolicitud.toString(),
                        sol.Nombre.toString(),
                        sol.FechaAlta.toString(),
                        sol.Comentario.toString()
                );

                sampleObjectsx.add(sampleObject);
            }
        }catch(Exception ex){
                 String h="";
            }
        return sampleObjectsx;

    }

    // initalized components
    private void initComponents(){

        this.tableA = new TableLayout(this.context);
        this.tableB = new TableLayout(this.context);
        this.tableC = new TableLayout(this.context);
        this.tableD = new TableLayout(this.context);

        this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);

        this.scrollViewC = new MyScrollView(this.context);
        this.scrollViewD = new MyScrollView(this.context);

        this.tableA.setBackgroundColor(Color.LTGRAY);
        this.horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);
    }

    // set essential component IDs
    private void setComponentsId(){
        this.tableA.setId(1);
        this.horizontalScrollViewB.setId(2);
        this.scrollViewC.setId(3);
        this.scrollViewD.setId(4);
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag(){

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    // we add the components here in our TableMainLayout
    private void addComponentToMainLayout(){

        // RelativeLayout params were very useful here
        // the addRule method is the key to arrange the components properly
        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        this.addView(this.tableA);
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(this.scrollViewC, componentC_Params);
        this.addView(this.scrollViewD, componentD_Params);

    }

    private void addTableRowToTableA(){
        this.tableA.addView(this.componentATableRow());
    }

    private void addTableRowToTableB(){
        this.tableB.addView(this.componentBTableRow());
    }

    // generate table row of table A--Estatus/ID Solicitud headers
    TableRow componentATableRow(){

        GradientDrawable gd=null;
        gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] {
                Color.parseColor("#003D7A"), Color.parseColor("#035DB7"), Color.parseColor("#003D7A") });
        gd.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gd.setGradientRadius(140.0f);
        gd.setGradientCenter(0.0f, 0.45f);
        gd.setCornerRadius(1);
        gd.setStroke(1, Color.parseColor("#FFFFFF"));


        TableRow componentATableRow = new TableRow(this.context);

        TextView textView = this.headerTextView(this.headers[0]);
        textView.setWidth(100);
        textView.setTextColor(Color.WHITE);
        //textView.setPadding(15,3,15,3);//top/right/bottom/left
        textView.setBackgroundDrawable(gd);
        componentATableRow.addView(textView);

        TextView textView2 = this.headerTextView(this.headers[1]);
        textView2.setWidth(120);
        textView2.setTextColor(Color.WHITE);
        textView2.setBackgroundDrawable(gd);

        componentATableRow.addView(textView2);

        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow(){

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        params.setMargins(2, 0, 0, 0);

        GradientDrawable gd=null;
        gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] {
                Color.parseColor("#003D7A"), Color.parseColor("#035DB7"), Color.parseColor("#003D7A") });
        gd.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gd.setGradientRadius(140.0f);
        gd.setGradientCenter(0.0f, 0.45f);
        gd.setCornerRadius(1);
        gd.setStroke(1, Color.parseColor("#FFFFFF"));

        for(int x=0; x<(headerFieldCount-2); x++){
            TextView textView = this.headerTextView(this.headers[x+2]);
            textView.setLayoutParams(params);

            switch(x+2){
                case 2:textView.setWidth(230);break;
                case 3:textView.setWidth(180);break;
                case 4:textView.setWidth(330);break;
            }

            textView.setTextColor(Color.WHITE);
            //textView.setPadding(15,10,15,10);//top/right/bottom/left
            textView.setBackgroundDrawable(gd);

            componentBTableRow.addView(textView);
        }
        return componentBTableRow;
    }

    // generate table row of table C and table D
    private void generateTableC_AndTable_B(){

        for(Header sampleObject : this.sampleObjects){
            TableRow tableRowForTableC = this.tableRowForTableC(sampleObject);
            TableRow taleRowForTableD = this.taleRowForTableD(sampleObject);

            tableRowForTableC.setBackgroundColor(Color.WHITE);
            taleRowForTableD.setBackgroundColor(Color.WHITE);

            this.tableC.addView(tableRowForTableC);
            this.tableD.addView(taleRowForTableD);
        }
    }

    // a TableRow for table C
    TableRow tableRowForTableC(Header sampleObject){

        TableRow.LayoutParams params = new TableRow.LayoutParams( this.headerCellsWidth[0],LayoutParams.WRAP_CONTENT);
        //params.setMargins(30, 0, 0, 0);//left-top-right-bottom
        params.leftMargin=30;
        params.rightMargin=30;
        params.width=45;

        TableRow.LayoutParams params2 = new TableRow.LayoutParams( this.headerCellsWidth[1],LayoutParams.MATCH_PARENT);
        params2.setMargins(0, 2, 0, 0);


        TableRow tableRowForTableC = new TableRow(this.context);

        TextView textView =new TextView(this.context);// this.bodyTextView(sampleObject.header1);
        textView.setText(sampleObject.header1);
        textView.setBackgroundColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        //------------------------------------------------------
        // prepare
        int strokeWidth = 6;
        int roundRadius = 25;
        int fillColor=0;
        int strokeColor = Color.parseColor("#FFFFFF");

        Colores c=Colores.valueOf(textView.getText().toString());

        GradientDrawable gd=null;

        switch (c){
            case Red :gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] {
                 Color.parseColor("#FF0000"), Color.parseColor("#cc0808"), Color.WHITE });textView.setText("");textView.setWidth(25);break;
            case Yellow: gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] {
                    Color.parseColor("#F7FE2E"), Color.parseColor("#AEB404"), Color.WHITE });textView.setText("");textView.setWidth(25);break;
            case Gray: gd =new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] {
                    Color.parseColor("#BDBDBD"), Color.parseColor("#A4A4A4"), Color.WHITE });textView.setText("");textView.setWidth(25);break;
            case Blue: gd =new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] {
                    Color.parseColor("#0174DF"), Color.parseColor("#2E9AFE"), Color.WHITE });textView.setText("");textView.setWidth(25);break;
        }
        //textView.setPadding(2,20,2,20);//top/right/bottom/left

        textView.setWidth(30);
        textView.setTextSize(8);
        gd.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gd.setGradientRadius(140.0f);
        gd.setGradientCenter(0.0f, 0.45f);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);

        textView.setBackgroundDrawable(gd);

        //-------------------------------------------------------
        tableRowForTableC.addView(textView, params);

        //new
        TextView textView2 = this.bodyTextView(sampleObject.header2);
        textView2.setTag(textView2.getText());
        tableRowForTableC.addView(textView2, params2);


        tableRowForTableC.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log.i("click detected:" + ((TableRow) v).getChildAt(1).getTag(), "yes");
                try{
                    Intent myIntent = new Intent(v.getContext(), frmNuevaSolicitud.class);

                    myIntent.putExtra("idSolicitud", ((TableRow) v).getChildAt(1).getTag().toString()  );
                    myIntent.putExtra("objSolicitud", "db" );
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                    v.getContext().startActivity(myIntent);

               }catch(Exception er){
                    String hh="";
                }
            }
        });


        return tableRowForTableC;
    }

    private enum Colores {
        Red, Blue, Yellow, Gray;
    }


    TableRow taleRowForTableD(Header sampleObject){
        TableRow taleRowForTableD = new TableRow(this.context);

        int loopCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();
        String info[] = {
                sampleObject.header3,
                sampleObject.header4,
                sampleObject.header5
        };

        for(int x=0 ; x<loopCount; x++){
            TableRow.LayoutParams params = new TableRow.LayoutParams( headerCellsWidth[x+2],LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB,params);
        }

        return taleRowForTableD;

    }

    // table cell standard TextView
    TextView bodyTextView(String label){

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);

        return bodyTextView;
    }

    // header standard TextView
    TextView headerTextView(String label){

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.WHITE);
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setPadding(5, 5, 5, 5);

        return headerTextView;
    }

    // resizing TableRow height starts here
    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow)  this.tableB.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth(){

        int tableAChildCount = ((TableRow)this.tableA.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();

        this.headerCellsWidth[0]=100;
        this.headerCellsWidth[1]=120;
        this.headerCellsWidth[2]=230;
        this.headerCellsWidth[3]=180;
        this.headerCellsWidth[4]=330;
        /*for(int x=0; x<(tableAChildCount+tableBChildCount); x++){

            if(x<2){
                this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableA.getChildAt(0)).getChildAt(x));
            }else{
                this.headerCellsWidth[x] = this.viewWidth(((TableRow)this.tableB.getChildAt(0)).getChildAt(x-2));
            }

        }*/
    }

    // resize body table row height
    void resizeBodyTableRowHeight(){

        int tableC_ChildCount = this.tableC.getChildCount();

        for(int x=0; x<tableC_ChildCount; x++){

            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = (TableRow)  this.tableD.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if(tableRow.getChildCount()==1){

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return ;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    // check if the view has the highest height in a TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    // read a view's height
    private int viewHeight(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // horizontal scroll view custom class
    class MyHorizontalScrollView extends HorizontalScrollView {

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();

            if(tag.equalsIgnoreCase("horizontal scroll view b")){
                horizontalScrollViewD.scrollTo(l, 0);
            }else{
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }

    }

    // scroll view custom class
    class MyScrollView extends ScrollView {

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();

            if(tag.equalsIgnoreCase("scroll view c")){
                scrollViewD.scrollTo(0, t);
            }else{
                scrollViewC.scrollTo(0,t);
            }
        }
    }


}