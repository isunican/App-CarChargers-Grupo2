package es.unican.carchargers.activities.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.parceler.Parcels;

import java.util.List;

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

    Spinner spnCompanhia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                filterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void init() {
        // initialize listener to react to touch selections in the list
        ListView lv = findViewById(R.id.lvChargers);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onChargerClicked(position);
            }
        });
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

    public void filterDialog() {
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

        filterDialog.show();

        String companhia = "";

        Button btnBuscar = (Button)view.findViewById(R.id.btnBuscar);
        Button btnBuscarTodos = (Button)view.findViewById(R.id.btnBuscarTodos);
        btnBuscar.setOnClickListener(v -> {
            filterDialog.dismiss();
            setFilter(companhia);
        });

        btnBuscarTodos.setOnClickListener(v -> {
            filterDialog.dismiss();
            presenter.showChargers();
        });

    }

    private void setFilter(String companhia) {
        companhia = spnCompanhia.getSelectedItem().toString();
         presenter.showFiltered(companhia);
    }
}