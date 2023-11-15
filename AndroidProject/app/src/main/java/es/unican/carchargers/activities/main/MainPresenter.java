package es.unican.carchargers.activities.main;

import android.content.Context;
import android.widget.Toast;
import android.content.Context;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import es.unican.carchargers.model.Address;
import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.constants.ELocation;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;
import hilt_aggregated_deps._dagger_hilt_android_internal_modules_ApplicationContextModule;

public class MainPresenter implements IMainContract.Presenter {

    /** the view controlled by this presenter */
    private IMainContract.View view;

    /** a cached list of charging stations currently shown */
    private List<Charger> shownChargers;
    private List<Charger> filteredChargers;

    private Map<String, Set<String>> provinces;

    @Override
    public void init(IMainContract.View view) {
        this.view = view;
        view.init();
        load();
    }

    /**
     * Constructor used for unit testing porpuses.
     * @param view MainView for testing the class.
     * @param shownChargers list of chargers for testing the class.
     */
    protected MainPresenter(MainView view, List<Charger> shownChargers) {
        this.view = view;
        this.shownChargers = shownChargers;
    }

    /**
     * The regular empty constructor
     */
    public MainPresenter() {
    }

    /**
     * This method requests a list of charging stations from the repository, and requests
     * the view to show them.
     */
    private void load() {
        IRepository repository = view.getRepository();

        // set API arguments to retrieve charging stations that match some criteria
        APIArguments args = APIArguments.builder()
                .setCountryCode(ECountry.SPAIN.code)
                .setLocation(ELocation.SANTANDER.lat, ELocation.SANTANDER.lon)
                .setMaxResults(500);

        ICallBack callback = new ICallBack() {
            @Override
            public void onSuccess(List<Charger> chargers) {
                MainPresenter.this.shownChargers =
                        chargers != null ? chargers : Collections.emptyList();
                filteredChargers = shownChargers;
                view.showChargers(MainPresenter.this.shownChargers);
                view.showLoadCorrect(MainPresenter.this.shownChargers.size());
            }

            @Override
            public void onFailure(Throwable e) {
                MainPresenter.this.shownChargers = Collections.emptyList();
                filteredChargers = shownChargers;
                view.showLoadError();
            }
        };

        repository.requestChargers(args, callback);

    }

    @Override
    public void onChargerClicked(int index) {
        if (filteredChargers != null && index < filteredChargers.size()) {
            Charger charger = filteredChargers.get(index);
            view.showChargerDetails(charger);
        }
        /*
        if (sortedChargers != null && index < sortedChargers.size()) {
            Charger charger = sortedChargers.get(index);
            view.showChargerDetails(charger);
        }
        */
    }

    @Override
    public void onMenuInfoClicked() {
        view.showInfoActivity();
    }


    @Override
    public void onFilteredClicked(String companhia, int minPower, int maxPower) {
        if (companhia.equals("-")) {
            filteredChargers = shownChargers;
        } else if (companhia.equals("OTROS")) {
            filterByOtherBusinesses();
        } else {
            filteredChargers = shownChargers.stream().filter
                            (charger -> charger.operator != null && charger.operator.title.toLowerCase().equals(companhia.toLowerCase()))
                    .collect(Collectors.toList());
        }

        filteredChargers = filteredChargers.stream().filter
                (charger -> charger.maxPower() >= minPower && charger.maxPower() <= maxPower).collect(Collectors.toList());

        if (filteredChargers.isEmpty()) {
            view.showFilterEmpty();
        }

        view.showChargers(filteredChargers);
    }

    public void filterByOtherBusinesses() {
        filteredChargers = shownChargers.stream().filter
                        (charger -> charger.operator != null && charger.operator.title.toLowerCase().equals("(Business Owner at Location)".toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public void onShowChargersFiltered() {
        filteredChargers = shownChargers;
        view.showChargers(shownChargers);
    }

    @Override
    public void onSortedClicked(String criterio, Boolean ascendente) {
        if (criterio.equals("NINGUNO")) {
            view.showRuleEmpty();
        }

        if (criterio.equals("POTENCIA")) {
            if (ascendente == null) {
                view.showChargers(filteredChargers);
                view.showAscDescEmpty();
            }
            else if (ascendente == true) {
                filteredChargers = (List<Charger>) filteredChargers.stream().sorted(new Comparator<Charger>() {
                    Collator collator = Collator.getInstance();
                    @Override
                    public int compare(Charger ch1, Charger ch2) {
                        if(ch1.maxPower() == ch2.maxPower()) {
                            if (ch1.operator == null || ch2.operator == null) {
                                return -1;
                            } else {
                                return collator.compare(ch1.operator.title, ch2.operator.title);
                            }
                        }
                        return Double.compare(ch1.maxPower(), ch2.maxPower());
                    }
                }).collect(Collectors.toList());
            } else if (ascendente == false) {
                filteredChargers = (List<Charger>) filteredChargers.stream().sorted(new Comparator<Charger>() {
                    Collator collator = Collator.getInstance();
                    @Override
                    public int compare(Charger ch1, Charger ch2) {
                        if(ch1.maxPower() == ch2.maxPower()) {
                            if(ch1.operator == null || ch2.operator == null) {
                                return -1;
                            } else {
                                return collator.compare(ch1.operator.title, ch2.operator.title);
                            }
                        }
                        return Double.compare(ch2.maxPower(), ch1.maxPower());
                    }
                }).collect(Collectors.toList());
            } else {
                filteredChargers = (List<Charger>) filteredChargers.stream().collect(Collectors.toList());
            }

            if (filteredChargers.isEmpty()) {
                view.showSortedEmpty();
            }
            view.showChargers(filteredChargers);
        } else {
            if (filteredChargers.isEmpty()) {
                view.showSortedEmpty();
            }
            view.showChargers(filteredChargers);
        }
    }

    @Override
    public void onShowChargersSorted() {
        filteredChargers = shownChargers;
        view.showChargers((shownChargers));
    }

    @Override
    public void showChargers(){
        filteredChargers = shownChargers;
        view.showChargers(shownChargers);
    }

    public void onDialogRequested() {
        view.showFilterDialog(shownChargers.stream().map(f -> f.maxPower()).min(Comparator.comparing(Double::valueOf)).get(),
                shownChargers.stream().map(f -> f.maxPower()).max(Comparator.comparing(Double::valueOf)).get());
    }
}