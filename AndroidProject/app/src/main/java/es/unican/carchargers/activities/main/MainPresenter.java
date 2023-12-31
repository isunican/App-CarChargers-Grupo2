package es.unican.carchargers.activities.main;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.constants.ELocation;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;

public class MainPresenter implements IMainContract.Presenter {

    /** the view controlled by this presenter */
    private IMainContract.View view;

    /** a cached list of charging stations currently shown */

    //Lo pongo público para probarlo en los tests
    public List<Charger> shownChargers;
    public List<Charger> filteredChargers;


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
    //Hago el método público para poder probarlo en los test
    @Override
    public void load() {
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
    public void onSortedClicked(String criterio, Boolean ascendente) {
        if (criterio.equals("NINGUNO")) {
            view.showRuleEmpty();
            return;
        }

        if (criterio.equals("COSTE TOTAL")) {
            if (view.returnCapacidadBateria() == -1 || view.returnPorcentajeBateria() == -1
                    || view.returnPorcentajeBateria() > 100) {
                view.showChargers(filteredChargers);
                view.showEtOrderTotalCostEmpty();
                ascendente = null;
                return;
            } else if (ascendente == null) {
                view.showChargers(filteredChargers);
                view.showAscDescEmpty();
                ascendente = null;
                return;
            }

            filteredChargers = shownChargers.stream()
                    .filter(charger -> charger.usageCost != null && !charger.usageCost.equals("")
                            && !charger.usageCost.contains("day") && !charger.usageCost.contains("night"))
                    .sorted(getChargerComparator(criterio, ascendente))
                    .collect(Collectors.toList());
            List<Charger> chargersWithoutPrice = shownChargers.stream()
                            .filter(charger -> charger.usageCost == null || charger.usageCost.equals("")
                                    || charger.usageCost.contains("day") || charger.usageCost.contains("night"))
                                    .collect(Collectors.toList());
            filteredChargers.addAll(chargersWithoutPrice);
            view.showChargers(filteredChargers);
            ascendente = null;
        } else {
            if (ascendente == null) {
                view.showChargers(filteredChargers);
                view.showAscDescEmpty();
                return;
            }
            Comparator<Charger> chargerComparator = getChargerComparator(criterio, ascendente);

            filteredChargers = filteredChargers.stream()
                    .sorted(chargerComparator)
                    .collect(Collectors.toList());

            if (filteredChargers.isEmpty()) {
                view.showSortedEmpty();
            }
            view.showChargers(filteredChargers);
        }
    }

    //Hago el método público para poder probarlo en los test
    public Comparator<Charger> getChargerComparator(String criterio, Boolean ascendente) {
        Comparator<Charger> comparator = null;

        if (criterio.equals("POTENCIA")) {
            comparator = Comparator.comparingDouble(Charger::maxPower);

            if (ascendente != null && !ascendente) {
                comparator = comparator.reversed();
            }

            comparator = comparator.thenComparing((ch1, ch2) -> {
                Collator collator = Collator.getInstance();
                if (ch1.operator == null || ch2.operator == null) {
                    return -1;
                }
                return collator.compare(ch1.operator.title, ch2.operator.title);
            });

        } else if (criterio.equals("COSTE TOTAL")) {
            comparator = Comparator.comparingDouble(ch ->
                    ch.costeTotalCarga(view.returnCapacidadBateria(), view.returnPorcentajeBateria()));

            if (ascendente != null && !ascendente) {
                comparator = comparator.reversed();
            }

            comparator = comparator.thenComparing((ch1, ch2) -> {
                Collator collator = Collator.getInstance();
                if (ch1.operator == null || ch2.operator == null) {
                    return -1;
                }
                return  Double.compare(ch2.maxPower(), ch1.maxPower());
            });

        }
        return comparator;
    }

    @Override
    public void onShowChargersClicked() {
        filteredChargers = shownChargers;
        view.showChargers((shownChargers));
    }

    @Override
    public void onDialogRequested() {
        view.showFilterDialog(shownChargers.stream().map(f -> f.maxPower()).min(Comparator.comparing(Double::valueOf)).get(),
                shownChargers.stream().map(f -> f.maxPower()).max(Comparator.comparing(Double::valueOf)).get());
    }

}
