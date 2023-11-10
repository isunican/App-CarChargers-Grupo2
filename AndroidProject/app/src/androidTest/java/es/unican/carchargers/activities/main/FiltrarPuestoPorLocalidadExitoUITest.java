/*
package es.unican.carchargers.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasToString;
import static es.unican.carchargers.utils.Matchers.isNotEmpty;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import es.unican.carchargers.R;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;
import es.unican.carchargers.common.RepositoriesModule;
import es.unican.carchargers.utils.HTTPIdlingResource;
*/
/**
 * Example UI Test using Hilt dependency injection
 * Documentation: https://developer.android.com/training/dependency-injection/hilt-testing
 * This test also uses an HTTP Idling Resource
 */
/*
@HiltAndroidTest
@UninstallModules(RepositoriesModule.class)
public class FiltrarPuestoPorLocalidadExitoUITest {

    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule(MainView.class);

    // necesito el context para acceder a recursos, por ejemplo un json con datos fake
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    // inject a fake repository that loads the data from a local json file
    // IMPORTANT: all the tests in this class must use this repository
    @BindValue IRepository repository = Repositories
            .getFake(context.getResources().openRawResource(R.raw.chargers_es_100));

    @Test
    public void FiltroProvinciaTest() throws InterruptedException {

        //Comprobar estado inicial y entrar en fitros
        onView(withId(R.id.lvChargers)).check(matches(isNotEmpty()));
        onView(withId(R.id.btnFilters)).perform(click());

        //Comprobar valor de los textos
        onView(withId(R.id.tvFiltrar)).check(matches(withText("Filtrar")));
        onView(withId(R.id.tvProvincia)).check(matches(withText("Provincia")));
        onView(withId(R.id.tvLocalidad)).check(matches(withText("Localidad")));

        //Comprobar que la lista de localidades cambia con las provincias
        onView(withId(R.id.spnProvincia)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Comunidad de Madrid"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spnLocalidad)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Aranjuez"))).inRoot(isPlatformPopup()).perform(click());


        onView(withId(R.id.spnProvincia)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Extremadura"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spnLocalidad)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Azuaga"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spnCompanhia)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("IBERDROLA"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.btnBuscar)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.lvChargers)).check(matches(hasChildCount(1)));
        //onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(0).perform(click());

    }
}
*/
