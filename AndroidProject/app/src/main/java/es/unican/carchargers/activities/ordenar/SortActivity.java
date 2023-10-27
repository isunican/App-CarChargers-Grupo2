package es.unican.carchargers.activities.ordenar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import es.unican.carchargers.R;
import es.unican.carchargers.model.Charger;

public class SortActivity extends AppCompatActivity implements View.OnClickListener {

    public boolean ascendente = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Button btnAsc = findViewById(R.id.btnAsc);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tiposOrdenacion,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOrdenar.setAdapter(adapter);

        private void setOrder(String tipoOrdenacion) {
            tipoOrdenacion = spnOrdenar.getSelectedItem().toString();
        }

        btnAsc.setOnClickListener(new View.OnClickListener()) {
            @Override
            public void onClick(View view) {
                ascendente = true;
            }
        }


    }


}
