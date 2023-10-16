package es.unican.carchargers.activities.details;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
        String strPrecio = String.format("Precio No Disponible");
        if (charger.usageCost != null) {
            strPrecio = String.format(charger.usageCost);
            if (strPrecio.equals("")) {
                strPrecio = String.format("Precio No Disponible");
            }
        }
        tvPrecio.setText(strPrecio);

        String strWebsite = String.format(charger.operator.website);
        tvWebsite.setText(strWebsite);


        wvMapa.setWebViewClient(new WebViewClient());
        //String mapaString = "<iframe src=\"https://maps.google.com/maps/embed?hl=en&amp;coord=52.70967533219885,-8.020019531250002&amp\" width=\"100%\" height=\"100%\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>";
        //String mapaString = "<iframe src=\"https://maps.google.com/maps/embed?hl=en&amp;coord=52.70967533219885,-8.020019531250002&amp\" width=\"100%\" height=\"100%\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>";
        String mapaString = "<iframe width=\"100%\" height=\"100%\" allowfullscreen=\"\" loading=\" lazy\" referrerpolicy=\"no-referrer-when-downgrade\" frameborder=\"0\" src=\"https://maps.google.com/maps?q=" + charger.address.latitude +"," + charger.address.longitude + "&hl=es;z=14&amp;output=embed\"></iframe>";
        wvMapa.loadData(mapaString, "text/html", null);
        wvMapa.getSettings().setJavaScriptEnabled(true);

        wvMapa.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true; //True if the listener has consumed the event, false otherwise.
            }
        });

    }
}