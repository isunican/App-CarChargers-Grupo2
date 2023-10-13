package es.unican.carchargers.activities.details;

import static android.widget.TextView.AUTO_SIZE_TEXT_TYPE_NONE;
import static android.widget.TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
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
        Button btHipervinculo = findViewById(R.id.btHipervinculo);

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
        //tvWebsite.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_NONE);
        //tvWebsite.setText(strWebsite);

        btHipervinculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri _link = Uri.parse(strWebsite);
                Intent i = new Intent (Intent.ACTION_VIEW, _link);
                startActivity(i);
            }
        });


    }
}