package maas.com.mx.maas.entidades;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by damserver on 26/04/2015.
 */
public class MySpinnerAdapter extends ArrayAdapter<objectItem> {

    private Context context;
    private objectItem[] myObjs;

    public MySpinnerAdapter(Context context, int textViewResourceId,objectItem[] myObjs) {
        super(context, textViewResourceId, myObjs);
        this.context = context;
        this.myObjs = myObjs;
    }

    public int getCount(){
        return myObjs.length;
    }

    public objectItem getItem(int position){
        return myObjs[position];
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setText("-"+myObjs[position].TEXT);
        label.setTag(myObjs[position].VALUE);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setText("-"+myObjs[position].TEXT);
        label.setTag(myObjs[position].VALUE);
        return label;
    }
}


