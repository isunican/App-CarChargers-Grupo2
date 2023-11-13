package es.unican.carchargers.activities.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.internal.bind.MapTypeAdapterFactory;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.carchargers.R;
import es.unican.carchargers.activities.details.DetailsView;
import es.unican.carchargers.activities.info.InfoActivity;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;

@AndroidEntryPoint
public class MainView extends AppCompatActivity implements IMainContract.View {

    /** repository is injected with Hilt */
    @Inject IRepository repository;

    /** presenter that controls this view */
    IMainContract.Presenter presenter;

    AlertDialog filterDialog;
    AlertDialog sortDialog;

    Spinner spnCompanhia;
    Spinner spnCriterio;
    RadioButton radioButtonAsc;
    RadioButton radioButtonDesc;

    Boolean ascendente = null;

    Spinner spnProvincia;
    Spinner spnLocalidad;
    TextView tvCapacidadBateria;
    TextView tvPorcentajeBateria;
    EditText etCapacidadBateria;
    EditText etPorcentajeBateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Initialize presenter-view connection
        presenter = new MainPresenter();
        presenter.init(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemInfo:
                presenter.onMenuInfoClicked();
                return true;
            case R.id.btnFilters:
                presenter.onDialogRequested();
                return true;
            case R.id.btnOrdenar:
                sortDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void init() {
        // initialize listener to react to touch selections in the list
        ListView lv = findViewById(R.id.lvChargers);
        lv.setOnItemClickListener((parent, view, position, id) -> presenter.onChargerClicked(position));
        /*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onChargerClicked(position);
            }
        });

         */
    }

    @Override
    public IRepository getRepository() {
        return repository;
    }

    @Override
    public void showChargers(List<Charger> chargers) {
        ChargersArrayAdapter adapter = new ChargersArrayAdapter(this, chargers);
        ListView listView = findViewById(R.id.lvChargers);
        listView.setAdapter(adapter);
    }

    @Override
    public void showLoadCorrect(int chargers) {
        Toast.makeText(this, String.format("Cargados %d cargadores", chargers),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadError() {
        Toast.makeText(this, "Error cargando cargadores", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showFilterEmpty() {
        Toast.makeText(this, "No hay resultados de búsqueda", Toast.LENGTH_LONG).show();
    }

    public void showSortedEmpty() {
        Toast.makeText(this, "No hay cargadores para ordenar", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRuleEmpty() {
        Toast.makeText(this, "No se ha elegido ningún criterio", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAscDescEmpty() {
        Toast.makeText(this, "No se ha elegido si la ordenación es ascendente o descendente",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showChargerDetails(Charger charger) {
        Intent intent = new Intent(this, DetailsView.class);
        intent.putExtra(DetailsView.INTENT_CHARGER, Parcels.wrap(charger));
        startActivity(intent);
    }

    @Override
    public void showInfoActivity() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void showFilterDialog(Map<String, Set<String>> provinces) {
        final Context context = this;
        LayoutInflater inflater= LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.filter_menu, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        filterDialog = builder.create();

        spnCompanhia = (Spinner)view.findViewById(R.id.spnCompanhia);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.companhiasArray,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCompanhia.setAdapter(adapter);

        spnProvincia = (Spinner)view.findViewById(R.id.spnProvincia);

        ArrayAdapter<String> adapterProvincia = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(provinces.keySet()));
        adapterProvincia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProvincia.setAdapter(adapterProvincia);

        spnLocalidad = (Spinner)view.findViewById(R.id.spnLocalidad);

        spnProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedProvince = spnProvincia.getSelectedItem().toString();
                Set<String> localities = provinces.get(selectedProvince);
                String[] localityArray = localities.toArray(new String[0]);
                ArrayAdapter<String> localityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, localityArray);
                localityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnLocalidad.setAdapter(localityAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
                spnLocalidad.setAdapter(emptyAdapter);
            }
        });

        filterDialog.show();

        Button btnBuscar = (Button)view.findViewById(R.id.btnBuscar);
        Button btnBuscarTodos = (Button)view.findViewById(R.id.btnBuscarTodos);
        btnBuscar.setOnClickListener(v -> {
            filterDialog.dismiss();
            setFilter();
        });

        btnBuscarTodos.setOnClickListener(v -> {
            filterDialog.dismiss();
            presenter.showChargers();
        });

    }
    private void setFilter() {
        String companhia = spnCompanhia.getSelectedItem().toString();
        String localidad = spnLocalidad.getSelectedItem().toString();
        presenter.onFilteredClicked(companhia, localidad);
    }

    public void sortDialog() {
        LayoutInflater inflater= LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.sort_menu, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        sortDialog = builder.create();

        spnCriterio = (Spinner)view.findViewById(R.id.spnCriterio);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.criteriosArray,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCriterio.setAdapter(adapter);

        sortDialog.show();

        tvCapacidadBateria = view.findViewById(R.id.tvCapacidadBateria);
        tvPorcentajeBateria = view.findViewById(R.id.tvPorcentajeBateria);
        etCapacidadBateria = view.findViewById(R.id.etCapacidadBateria);
        etPorcentajeBateria = view.findViewById(R.id.etPorcentajeBateria);

        spnCriterio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    tvPorcentajeBateria.setVisibility(View.VISIBLE);
                    tvCapacidadBateria.setVisibility(View.VISIBLE);
                    etCapacidadBateria.setVisibility(View.VISIBLE);
                    etPorcentajeBateria.setVisibility(View.VISIBLE);
                } else {
                    tvPorcentajeBateria.setVisibility(View.INVISIBLE);
                    tvCapacidadBateria.setVisibility(View.INVISIBLE);
                    etCapacidadBateria.setVisibility(View.INVISIBLE);
                    etPorcentajeBateria.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  tu código
            }
        });

        radioButtonAsc = (RadioButton) view.findViewById(R.id.radioButtonAsc);
        radioButtonDesc = (RadioButton) view.findViewById(R.id.radioButtonDesc);

        radioButtonAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ascendente = true;
            }
        });

        radioButtonDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ascendente = false;
            }
        });

        Button btnBuscarOrden = (Button)view.findViewById(R.id.btnBuscarOrden);
        btnBuscarOrden.setOnClickListener(v -> {
            sortDialog.dismiss();
            setOrdenacion(ascendente);
        });
    }

    private void setOrdenacion(Boolean ascendente) {
        String criterio = spnCriterio.getSelectedItem().toString();
        presenter.onSortedClicked(criterio, ascendente);
    }
}