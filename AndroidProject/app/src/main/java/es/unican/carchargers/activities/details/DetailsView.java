package es.unican.carchargers.activities.details;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import es.unican.carchargers.R;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import okio.Utf8;

/**
 * Charging station details view. Shows the basic information of a charging station.
 */
public class DetailsView extends AppCompatActivity {

    public static final String INTENT_CHARGER = "INTENT_CHARGER";

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
        WebView wvMapa = findViewById(R.id.wvMapa);

        // Get Charger from the intent that triggered this activity
        Charger charger = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_CHARGER));

        // Set logo
        int resourceId = EOperator.fromId(charger.operator.id).logo;
        ivLogo.setImageResource(resourceId);

        // Set Infos
        tvTitle.setText(charger.operator.title);
        tvId.setText(charger.id);
        String strInfoAddress = String.format("%s, (%s, %s)", charger.address.title, charger.address.town, charger.address.province);
        tvInfoAddress.setText(strInfoAddress);

        String strInfoNumberOfPoints = String.format("Número de cargadores: %d", charger.numberOfPoints);
        tvInfoNumberOfPoints.setText(strInfoNumberOfPoints);

        String strPrecio = String.format(charger.usageCost);
        if (strPrecio.equals("")) {
            strPrecio = String.format("Precio No Disponible");
        }
        tvPrecio.setText(strPrecio);

        String strWebsite = String.format(charger.operator.website);
        tvWebsite.setText(strWebsite);
    }
}