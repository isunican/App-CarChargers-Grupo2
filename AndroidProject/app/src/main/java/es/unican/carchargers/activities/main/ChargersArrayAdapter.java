package es.unican.carchargers.activities.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import es.unican.carchargers.R;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;

public class ChargersArrayAdapter extends ArrayAdapter<Charger> {

    public ChargersArrayAdapter(@NonNull Context context, @NonNull List<Charger> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // this is the car charger we want to show here
        Charger charger = getItem(position);

        // create the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_main_item, parent, false);
        }

        // logo
        {
            ImageView iv = convertView.findViewById(R.id.ivLogo);
            if (charger.operator != null) {
                EOperator operator = EOperator.fromId(charger.operator.id);
                iv.setImageResource(operator.logo);
            } else {
                EOperator operator = EOperator.fromId(-1);
                iv.setImageResource(operator.logo);
            }
        }

        // Title
        {
            TextView tv = convertView.findViewById(R.id.tvTitle);
            if (charger.operator != null) {
                tv.setText(charger.operator.title);
            } else {
                tv.setText("Operador no disponible");
            }
        }

        // Address
        {
            TextView tv = convertView.findViewById(R.id.tvAddress);
            String str = String.format("%s (%s)", charger.address.title, charger.address.province);
            tv.setText(str);
        }

        // Info
        {
            TextView tv = convertView.findViewById(R.id.tvInfoAddress);
            tv.setText(charger.usageCost);
        }

        // CosteTotalCarga
        {
            TextView tv = convertView.findViewById(R.id.tvCosteTotalCarga);
            if (charger.costeTotal <= 0) {
                tv.setText(" ");
            } else if (charger.usageCost.contains("parking")) {
                tv.setText(String.format("Coste Total de Carga: %.4s€ + parking fee", charger.costeTotal));
            } else {
                tv.setText(String.format("Coste Total de Carga: %.4s€", charger.costeTotal));
            }
        }

        return convertView;
    }
}
