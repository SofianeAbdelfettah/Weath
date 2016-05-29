package etna.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Yunori on 28/04/2016.
 */
public class CustomAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> imagesvalues;
    private ArrayList<String> values;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_layout, null);
        }

        ImageView imageView = (ImageView) v.findViewById(R.id.icon);
        try {
            Picasso.with(context).load(this.imagesvalues.get(position)).into(imageView);
        } catch(Exception e) {
            e.printStackTrace();
        }
        TextView textView = (TextView) v.findViewById(R.id.label);
        textView.setText(values.get(position));

        return v;
    }

    public CustomAdapter(Context context, ArrayList<String> values, ArrayList<String> Imagesvalues) {
        super(context, R.layout.row_layout, values);
        this.context = context;
        this.imagesvalues = Imagesvalues;
        this.values = values;
    }
}