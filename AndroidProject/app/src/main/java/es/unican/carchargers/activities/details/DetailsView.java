package es.unican.carchargers.activities.details;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import es.unican.carchargers.R;
import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Connection;
import okio.Utf8;

/**
 * Charging station details view. Shows the basic information of a charging station.
 */
public class DetailsView extends AppCompatActivity implements View.OnClickListener {

    public static final String INTENT_CHARGER = "INTENT_CHARGER";

    TextView tvResPower;
    TextView tvResConnectorType;
    Charger charger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        // Link to view elements
        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvId = findViewById(R.id.tvId);
        TextView tvInfoAddress = findViewById(R.id.tvInfoAddress);
        TextView tvInfoNumberOfPoints = findViewById(R.id.tvInfoNumberOfPoints);
        TextView tvPrecio = findViewById(R.id.tvPrecio);
        TextView tvWebsite = findViewById(R.id.tvWebsite);
        tvResPower = findViewById(R.id.tvResPower);
        tvResConnectorType = findViewById(R.id.tvResConnectorType);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        //WebView wvMapa = findViewById(R.id.wvMapa);

        // Get Charger from the intent that triggered this activity
        charger = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_CHARGER));

        // Set logo
        int resourceId = EOperator.fromId(charger.operator.id).logo;
        ivLogo.setImageResource(resourceId);

        // Set Infos
        tvTitle.setText(charger.operator.title);
        tvId.setText(charger.id);
        String strInfoAddress = String.format("%s, (%s, %s)", charger.address.title, charger.address.town, charger.address.province);
        tvInfoAddress.setText(strInfoAddress);

        String strInfoNumberOfPoints = String.format("Número de cargadores:");
        tvInfoNumberOfPoints.setText(strInfoNumberOfPoints);

        /*
        String strInfoNumberOfPoints = String.format("Número de cargadores: %d", charger.numberOfPoints);
        tvInfoNumberOfPoints.setText(strInfoNumberOfPoints);
        */

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        for (int i = 0; i < charger.connections.size(); i++) {
            TextView tv = new TextView(this);
            tv.setText(Integer.toString(i + 1));
            Connection connection = charger.connections.get(i);
            tv.setTag(connection);
            params.setMargins(10,0,0,0);
            tv.setLayoutParams(params);
            tv.setOnClickListener(this);
            linearLayout.addView(tv);
        }

        /*
        String strPrecio = String.format(charger.usageCost);
        if (strPrecio.equals("")) {
            strPrecio = String.format("Precio No Disponible");
        }
        tvPrecio.setText(strPrecio);

        String strWebsite = String.format(charger.operator.website);
        if (strWebsite == null) {
            strWebsite = String.format("Web No Disponible");
        } else {
            tvWebsite.setText(strWebsite);
        }

         */
    }
    @Override
    public void onClick(View v) {
        Connection connection = (Connection) v.getTag();
        tvResConnectorType.setText(connection.connectionType.formalName);
        tvResPower.setText(String.format(connection.powerKW + " KW"));
    }
}