package es.unican.carchargers.activities.main;

import java.util.List;
import java.util.Map;
import java.util.Set;

import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;

/**
 * The Presenter-View contract for the Main activity.
 * The Main activity shows a list of charging stations.
 */
public interface IMainContract {

    /**
     * Methods that must be implemented in the Main Presenter.
     * Only the View should call these methods.
     */
    public interface Presenter {

        /**
         * Links the presenter with its view.
         * Only the View should call this method
         * @param view
         */
        public void init(View view);

        //Hago el método público para poder probarlo en los test
        void load();

        /**
         * The presenter is informed that a charging station has been clicked
         * Only the View should call this method
         * @param index the index of the clicked charging station
         */
        public void onChargerClicked(int index);

        /**
         * The presenter is informed that the Info item in the menu has been clicked
         * Only the View should call this method
         */
        public void onMenuInfoClicked();

        public void onFilteredClicked(String companhia);

        void onSortedClicked(String criterio, Boolean ascendente);

        public void onShowChargersClicked();

        public void onDialogRequested();
    }

    /**
     * Methods that must be implemented in the Main View.
     * Only the Presenter should call these methods.
     */
    public interface View {

        /**
         * Initialize the view. Typically this should initialize all the listeners in the view.
         * Only the Presenter should call this method
         */
        public void init();

        /**
         * Returns a repository that can be called by the Presenter to retrieve charging stations.
         * This method must be located in the view because Android resources must be accessed
         * in order to instantiate a repository (for example Internet Access). This requires
         * dependencies to Android. We want to keep the Presenter free of Android dependencies,
         * therefore the Presenter should be unable to instantiate repositories and must rely on
         * the view to create the repository.
         * Only the Presenter should call this method
         * @return
         */
        public IRepository getRepository();

        /**
         * The view is requested to display the given list of charging stations.
         * Only the Presenter should call this method
         * @param chargers the list of charging stations
         */
        public void showChargers(List<Charger> chargers);

        /**
         * The view is requested to display a notification indicating  that the charging
         * stations were loaded correctly.
         * Only the Presenter should call this method
         * @param chargers
         */
        public void showLoadCorrect(int chargers);

        /**
         * The view is requested to display a notificacion indicating that the charging
         * stations were not loaded correctly.
         * Only the Presenter should call this method
         */
        public void showLoadError();

        void showFilterEmpty();

        void showSortedEmpty();

        void showRuleEmpty();

        void showAscDescEmpty();

        void showEtOrderTotalCostEmpty();

        /**
         * The view is requested to display the detailed view of the given charging station.
         * Only the Presenter should call this method
         * @param charger the charging station
         */
        public void showChargerDetails(Charger charger);

        /**
         * The view is requested to open the info activity.
         * Only the Presenter should call this method
         */
        public void showInfoActivity();

        /**
         * The view is requested to display the alert dialog for filtering chargers.
         * Only the Presenter should call this method
         */
        public void showFilterDialog();

        double returnCapacidadBateria();

        double returnPorcentajeBateria();
    }
}