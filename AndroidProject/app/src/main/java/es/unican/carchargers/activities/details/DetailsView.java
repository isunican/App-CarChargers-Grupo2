package es.unican.carchargers.activities.details;

import static android.widget.TextView.AUTO_SIZE_TEXT_TYPE_NONE;
import static android.widget.TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import es.unican.carchargers.R;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;

import es.unican.carchargers.model.Favourite;

import es.unican.carchargers.model.Connection;

import java.util.logging.Logger;

/**
 * Charging station details view. Shows the basic information of a charging station.
 */
public class DetailsView extends AppCompatActivity implements View.OnClickListener {

    Logger logger = Logger.getLogger("lgDetailsView");

    public static final String INTENT_CHARGER = "INTENT_CHARGER";

    boolean isFavourite;

    Charger charger = new Charger();

    Favourite favourite = new Favourite();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        //Bloquear pantalla en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Link to view elements
        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvId = findViewById(R.id.tvId);
        TextView tvAddress = findViewById(R.id.tvDireccion);

        ImageView ivFavoritos = findViewById(R.id.ivFavoritos);

        TextView tvInfoAddress = findViewById(R.id.tvInfoAddress);
        TextView tvPrecio = findViewById(R.id.tvPrecio);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        WebView wvMapa = findViewById(R.id.wvMapa);


        // Get Charger from the intent that triggered this activity
        charger = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_CHARGER));

        String strWebsite = null;
        if (charger.operator != null && charger.operator.website != null) {
            strWebsite = String.format(charger.operator.website);
        }

        String finalStrWebsite = strWebsite;
        // Set logo
        int resourceId = EOperator.fromId(-1).logo;
        if (charger.operator != null) {
            resourceId = EOperator.fromId(charger.operator.id).logo;
        }
        ivLogo.setImageResource(resourceId);
        ivLogo.setOnClickListener(v ->  {
            if (finalStrWebsite != null) {
                Uri _link = Uri.parse(finalStrWebsite);
                Intent i = new Intent(Intent.ACTION_VIEW, _link);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "El proveedor no tiene enlace web", Toast.LENGTH_LONG).show();
            }
        });

        // Set Infos
        if (charger.operator != null) {
            tvTitle.setText(charger.operator.title);
        } else {
            tvTitle.setText("Operador no disponible");
        }
        tvId.setText(charger.id);
        String strInfoAddress = String.format("%s, (%s, %s)", charger.address.title, charger.address.town, charger.address.province);
        tvInfoAddress.setText(strInfoAddress);

        String strAddress = String.format("%s", charger.address.addressLine1);
        if (strAddress == null) {
            strAddress = String.format("%s", charger.address.addressLine2);
            if (strAddress == null) {
                strAddress = "Dirección no disponible";
            }
        }
        tvAddress.setText(strAddress);

        // Set params for all connection views
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);

        // Show all connections
        for (int i = 0; i < charger.connections.size(); i++) {
            TextView tv = new TextView(this);
            tv.setText(Integer.toString(i + 1));
            Connection connection = charger.connections.get(i);
            tv.setTag(connection);
            tv.setGravity(Gravity.CENTER);

            // Set a background for the connection
            try {
                if (connection.statusType.isOperational) {
                    // Green when is operational
                    tv.setBackgroundColor(Color.GREEN);
                } else {
                    // Red when is not operational
                    tv.setBackgroundColor(Color.RED);
                }
            } catch(NullPointerException n) {
                // Gray when we do not know
                tv.setBackgroundColor(Color.GRAY);
                logger.info("NullPointerException thrown");
            }
            params.setMargins(20,0,0,0);
            tv.setLayoutParams(params);
            tv.setOnClickListener(this);
            linearLayout.addView(tv);
        }


        String strPrecio;
        if (charger.usageCost == null) {
            strPrecio = String.format("Precio No Disponible");
        } else {
            strPrecio = String.format(charger.usageCost);


            if (strPrecio.equals("") || strPrecio == null) {
                strPrecio = String.format("Precio No Disponible");
            }
        }
        tvPrecio.setText(strPrecio);

        wvMapa.setWebViewClient(new WebViewClient());
        String mapaString = "<iframe width=\"100%\" height=\"100%\" allowfullscreen=\"\" loading=\" lazy\" referrerpolicy=\"no-referrer-when-downgrade\" frameborder=\"0\" src=\"https://maps.google.com/maps?q=" + charger.address.latitude +"," + charger.address.longitude + "&hl=es;z=14&amp;output=embed\"></iframe>";
        wvMapa.loadData(mapaString, "text/html", null);
        wvMapa.getSettings().setJavaScriptEnabled(true);

        wvMapa.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true; //True if the listener has consumed the event, false otherwise.
            }
        });


        // SharedPreferences
        SharedPreferences sharedPref = this.getSharedPreferences("favoritos",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Favoritos
        // Se comprueba si el cargador esta en favoritos
        isFavourite = sharedPref.getBoolean(charger.id, false);
        if (isFavourite) {
            ivFavoritos.setImageResource(R.drawable.favoritoactivo);
        } else {
            ivFavoritos.setImageResource(R.drawable.favoritosnoactivo);
        }

        ivFavoritos.setOnClickListener(v ->  {
            charger.isFavourite = !charger.isFavourite;
            if (charger.isFavourite) {
                ivFavoritos.setImageResource(R.drawable.favoritoactivo);
                editor.putBoolean(charger.id, true);
                editor.apply();
                favourite.addCharger(charger.id);
                Toast.makeText(getApplicationContext(), "Anhadido correctamente a favoritos", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onClick(View v) {
        // Link to view elements
        TextView tvResPower = findViewById(R.id.tvResPower);
        TextView tvResConnectorType = findViewById(R.id.tvResConnectorType);
        TextView tvResDisponibility = findViewById(R.id.tvResQuantity);

        // Get the connection clicked
        Connection connection = (Connection) v.getTag();

        // Change her properties
        tvResConnectorType.setText(connection.connectionType.title);
        tvResPower.setText(String.format(connection.powerKW + " KW"));
        tvResDisponibility.setText(String.valueOf(connection.quantity));
    }
}