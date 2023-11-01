package es.unican.carchargers.activities.main;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<Charger> sortedChargers;
    Charger charger1 = new Charger();
    Charger charger2 = new Charger();

    @Override
    public void init(IMainContract.View view) {
        this.view = view;
        view.init();
        load();
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
                //sortedChargers = shownChargers;
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
    public void onFilteredClicked(String companhia){
        filteredChargers = shownChargers.stream().filter
                        (charger -> charger.operator.title.toLowerCase().equals(companhia.toLowerCase()))
                .collect(Collectors.toList());
        view.showChargers(filteredChargers);
    }

    @Override
    public void onShowChargersFiltered() {
        filteredChargers = shownChargers;
        view.showChargers(shownChargers);
    }

    @Override
    public void onSortedClicked(String criterio, Boolean ascendente) {

        if (ascendente == true) {
            filteredChargers = (List<Charger>) filteredChargers.stream().sorted(new Comparator<Charger>() {
                Collator collator = Collator.getInstance();
                @Override
                public int compare(Charger ch1, Charger ch2) {
                    if(ch1.maxPower() == ch2.maxPower()) {
                        return collator.compare(ch1.operator.title, ch2.operator.title);
                    }
                    return (int) (ch1.maxPower() - ch2.maxPower());
                }
            }).collect(Collectors.toList());
        } else if (ascendente == false) {
            filteredChargers = (List<Charger>) filteredChargers.stream().sorted(new Comparator<Charger>() {
                Collator collator = Collator.getInstance();
                @Override
                public int compare(Charger ch1, Charger ch2) {
                    if(ch1.maxPower() == ch2.maxPower()) {
                        return collator.compare(ch1.operator.title, ch2.operator.title);
                    }
                    return (int) (ch2.maxPower() - ch1.maxPower());
                }
            }).collect(Collectors.toList());
        } else {
            filteredChargers = (List<Charger>) filteredChargers.stream().collect(Collectors.toList());
        }
        view.showChargers(filteredChargers);
    }

    @Override
    public void onShowChargersSorted() {
        filteredChargers = shownChargers;
        view.showChargers((shownChargers));
    }

}



