package es.unican.carchargers.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static es.unican.carchargers.utils.Matchers.isNotEmpty;

import android.content.Context;

import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import es.unican.carchargers.R;
import es.unican.carchargers.common.RepositoriesModule;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

@HiltAndroidTest
@UninstallModules(RepositoriesModule.class)
public class OrdenarCosteTotalCargaExitoAscUITest {

    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule(MainView.class);

    // necesito el context para acceder a recursos, por ejemplo un json con datos fake
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    // inject a fake repository that loads the data from a local json file
    // IMPORTANT: all the tests in this class must use this repository
    @BindValue IRepository repository = Repositories
            .getFake(context.getResources().openRawResource(R.raw.chargers_es_6));

    /*
      La informacion del repositorio fake con la que voy a trabajar en el test es:
      CHARGER1:
           -UsageCost = 0.39€/kWh
           -Title (OperatorInfo) = Zunder
           -PowerKW = 22

      CHARGER2:
           -UsageCost = 0.29€/kWh
           -Title (OperatorInfo) = Endesa

      CHARGER3:
           -UsageCost = 0,55€/kWh DC - 0,39€/kWh AC
           -Title (OperatorInfo) = Iberdrola
           -PowerKW = 22, 90

      CHARGER4:
           -UsageCost = ""
           -Title (OperatorInfo) = (Business Owner at Location)
           -PowerKW = 3.7

      CHARGER5:
           -UsageCost = 0,40€/kWh DC - 0,30€/kWh AC
           -Title (OperatorInfo) = GIC

      CHARGER6:
           -UsageCost = parking fee
           -Title (OperatorInfo) = (Business Owner at Location)
           -PowerKW = 4.7
     */
    @Test
    public void ordenarCosteTotalCargaExitoAscTest() {
        onView(withId(R.id.lvChargers)).check(matches(isNotEmpty()));

        onView(withId(R.id.btnOrdenar)).perform(click());

        onView(withId(R.id.spnCriterio)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("COSTE TOTAL"))).inRoot(isPlatformPopup()).perform(click());

        /*
            Trabajo con capacidad de la batería valor 100 y porcentaje de bateria actual del vehiculo
            90 para saber que el coste total de carga es simplemente multiplicar por 10 el menor usageCost
            del puesto y facilitar las comprobaciones.
         */
        onView(withId(R.id.etCapacidadBateria)).perform(typeText("100"));
        onView(withId(R.id.etPorcentajeBateria)).perform(typeText("90"));

        onView(withId(R.id.radioButtonAsc)).perform(click());

        onView(withId(R.id.btnBuscarOrden)).perform(click());

        /*
            Comprobamos que los primeros cargadores que salen en la lista son aquellos que no tienen
            usageCost (como Charger4 que es "") y que no tienen el precio en su usageCost (como
            Charger6 que es "parking fee"). Como ambos van a tener el mismo coste total de carga (vacio),
            su orden vendrá determinado por su potencia maxima. De este modo, el primer cargador que sale en
            la lista es Charger6 (powerKW 4.7) y el segundo es Charger4 (powerKw 3.7)
         */
        DataInteraction interaction = onData(anything())
                .inAdapterView(withId(R.id.lvChargers)).atPosition(0);
        interaction.onChildView(withId(R.id.tvTitle)).check(matches(withText("(Business Owner at Location)")));
        interaction.onChildView(withId(R.id.tvCosteTotalCarga)).check(matches(withText("")));
        interaction.onChildView(withId(R.id.tvResPower)).check(matches(withText("4.7")));
        interaction.onChildView(withId(R.id.tvResPower)).check(matches(not(withText("3.7"))));

        interaction = onData(anything())
                .inAdapterView(withId(R.id.lvChargers)).atPosition(1);
        interaction.onChildView(withId(R.id.tvTitle)).check(matches(withText("(Business Owner at Location)")));
        interaction.onChildView(withId(R.id.tvCosteTotalCarga)).check(matches(withText("")));
        interaction.onChildView(withId(R.id.tvPower)).check(matches(withText("3.7")));

        /*
            Comprobamos que el tercer cargador es el Charger2, que tiene como coste total de carga 2.9€.
            Comprobamos que el cuarto cargador es el Charger5, que tiene como coste total de carga 3.0€.
         */
        interaction = onData(anything())
                .inAdapterView(withId(R.id.lvChargers)).atPosition(2);
        interaction.onChildView(withId(R.id.tvTitle)).check(matches(withText("Endesa")));
        interaction.onChildView(withId(R.id.tvCosteTotalCarga)).check(matches(withText("Coste Total de Carga: 2.9€")));

        interaction = onData(anything())
                .inAdapterView(withId(R.id.lvChargers)).atPosition(3);
        interaction.onChildView(withId(R.id.tvTitle)).check(matches(withText("GIC")));
        interaction.onChildView(withId(R.id.tvCosteTotalCarga)).check(matches(withText("Coste Total de Carga: 3.0€")));

        /*
            Para la comprobacion de los ultimos dos cargadores, sucede lo mismo que en los dos primeros,
            el coste total de carga es el mismo (3.9€). Por ello, al igual que antes, se mostrara primero
            aquel cargador con mayor potencia de carga. El Charger1 tiene potencia de 22, mientras que
            el Charger3 tiene potencia de 22 y 90, por lo tanto, el Charger3 tendrá que aparecer primero.
         */
        interaction = onData(anything())
                .inAdapterView(withId(R.id.lvChargers)).atPosition(4);
        interaction.onChildView(withId(R.id.tvTitle)).check(matches(withText("Iberdrola")));
        interaction.onChildView(withId(R.id.tvCosteTotalCarga)).check(matches(withText("Coste Total de Carga: 3.9€")));
        interaction.onChildView(withId(R.id.tvResPower)).check(matches(withText("90")));
        interaction.onChildView(withId(R.id.tvTitle)).check(matches(not(withText("Zunder"))));

        interaction = onData(anything())
                .inAdapterView(withId(R.id.lvChargers)).atPosition(5);
        interaction.onChildView(withId(R.id.tvTitle)).check(matches(withText("(Business Owner at Location)")));
        interaction.onChildView(withId(R.id.tvCosteTotalCarga)).check(matches(withText("Coste Total de Carga: 3.9€")));
        interaction.onChildView(withId(R.id.tvPower)).check(matches(withText("22")));
    }
}
