package es.unican.carchargers.activities.main;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static es.unican.carchargers.utils.Matchers.isNotEmpty;

import android.content.Context;

import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import es.unican.carchargers.R;
import es.unican.carchargers.common.RepositoriesModule;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;
import es.unican.carchargers.utils.HTTPIdlingResource;

@HiltAndroidTest
@UninstallModules(RepositoriesModule.class)
public class OrdenarPorPotenciaExitoAscUITest {
    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule(MainView.class);

    // necesito el context para acceder a recursos, por ejemplo un json con datos fake
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @BeforeClass
    public static void setupClass() {
        // si usamos un repository fake que realmente no accede por HTTP, no necesitamos
        // activar este Idling Resource. Lo dejo para tener una referencia.
        HTTPIdlingResource.getInstance().init();
    }

    @AfterClass
    public static void teardownClass() {
        HTTPIdlingResource.getInstance().finish();
    }

    // inject a fake repository that loads the data from a local json file
    // IMPORTANT: all the tests in this class must use this repository
    @BindValue IRepository repository = Repositories
            .getFake(context.getResources().openRawResource(R.raw.chargers_es_10));

    @Test
    public void ordenarPotenciaExitoAscTest() throws InterruptedException {
        onView(withId(R.id.lvChargers)).check(matches(isNotEmpty()));

        onView(withId(R.id.btnOrdenar)).perform(click());

        onView(withId(R.id.spnCriterio)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("POTENCIA"))).inRoot(isPlatformPopup()).perform(click());

        onView(withId(R.id.radioButtonAsc)).perform(click());

        onView(withId(R.id.btnBuscarOrden)).perform(click());

        DataInteraction interaction = onData(anything())
                .inAdapterView(withId(R.id.lvChargers)).atPosition(0);
        interaction.onChildView(withId(R.id.tvAddress)).check(matches(withText("Hotel Catalonia Giralda")));

        interaction = onData(anything())
                .inAdapterView(withId(R.id.lvChargers)).atPosition(4);
        interaction.onChildView(withId(R.id.tvAddress)).check(matches(withText("Hotel Catalonia Giralda")));

        interaction = onData(anything())
                .inAdapterView(withId(R.id.lvChargers)).atPosition(9);
        interaction.onChildView(withId(R.id.tvAddress)).check(matches(withText("Hotel Catalonia Giralda")));

    }
}
