package es.unican.carchargers.activities.main;

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
                .setMaxResults(50);

        ICallBack callback = new ICallBack() {
            @Override
            public void onSuccess(List<Charger> chargers) {
                MainPresenter.this.shownChargers =
                        chargers != null ? chargers : Collections.emptyList();
                filteredChargers = shownChargers;
                provinces = mappingProvinces(chargers);
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
    public void showFiltered(String companhia){
        filteredChargers = shownChargers.stream().filter
                        (charger -> charger.operator.title.toLowerCase().equals(companhia.toLowerCase()))
                .collect(Collectors.toList());
        view.showChargers(filteredChargers);
    }

    @Override
    public void showChargers(){
        filteredChargers = shownChargers;
        view.showChargers(shownChargers);
    }

    @Override
    public void onDialogRequested() {
        view.showFilterDialog(provinces);
    }

    protected static Map<String, Set<String>> mappingProvinces(List<Charger> chargers) {

        Map<String, Set<String>> mapProvinces = new HashMap<>();

        /* Get rid of chargers with no information about its province or town */
        chargers.removeIf(charger -> {
            Address address = charger.address;
            return address == null || address.province == null || address.town == null;
        });

        for (Charger c: chargers) {
            String province = c.address.province;
            String town = c.address.town;
            if (mapProvinces.containsKey(province)) {
                Set<String> setTowns = mapProvinces.get(province);
                setTowns.add(town);
            } else {
                Set<String> setTowns = new HashSet<>();
                setTowns.add(town);
                mapProvinces.put(province, setTowns);
            }
        }
        return mapProvinces;
    }
}


