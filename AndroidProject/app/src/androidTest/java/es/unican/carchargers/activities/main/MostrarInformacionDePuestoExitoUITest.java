package es.unican.carchargers.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagKey;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static es.unican.carchargers.utils.Matchers.isNotEmpty;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

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

/**
 * Example UI Test using Hilt dependency injection
 * Documentation: https://developer.android.com/training/dependency-injection/hilt-testing
 * This test also uses an HTTP Idling Resource
 */
@HiltAndroidTest
@UninstallModules(RepositoriesModule.class)
public class MostrarInformacionDePuestoExitoUITest {

    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule(MainView.class);

    // necesito el context para acceder a recursos, por ejemplo un json con datos fake
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    // inject a fake repository that loads the data from a local json file
    // IMPORTANT: all the tests in this class must use this repository
    @BindValue IRepository repository = Repositories
            .getFake(context.getResources().openRawResource(R.raw.chargers_es_2));

    @Test
    public void MostrarInformacionDePuestoExitoTest() {

        /* Comprobar que las estaciones de carga aparecen en la lista con sus datos */
        onView(withId(R.id.lvChargers)).check(matches(isNotEmpty()));

        DataInteraction interaction = onData(anything())
                .inAdapterView(withId(R.id.lvChargers)).atPosition(1);

        interaction.onChildView(withId(R.id.tvTitle)).check(matches(withText("Zunder")));
        interaction.onChildView(withId(R.id.tvAddress))
                .check(matches(withText("Torre-Pacheco Club de Golf (Zunder) " +
                        "(Región de Murcia)")));

        /* Hacer click en la segunda estacion de la lista y comprobar la pantalla
           que se despliega tras presionarla */
        onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(1).perform(click());
        onView(withId(R.id.tvTitle)).check(matches(withText("Zunder")));
        onView(withId(R.id.tvInfoAddress))
                .check(matches(withText("Torre-Pacheco Club de Golf (Zunder)," +
                        " (Torre Pacheco, Región de Murcia)")));
        onView(withId(R.id.tvInfoNumberOfPoints)).check(matches(withText("NumberOfPoints")));
        onView(withId(R.id.tvConnectorType)).check(matches(withText("Tipo conector:")));
        onView(withId(R.id.tvPower)).check(matches(withText("Potencia")));
        onView(withId(R.id.tvQuantity)).check(matches(withText("Cantidad:")));
        onView(withId(R.id.tvId)).check(matches(withText("213053")));
        onView(withId(R.id.ivLogo)).check(matches(withTagKey(R.drawable.zunder)));

        /* Presionar en un punto de carga de la estacion y comprobar sus datos */
        onView(withContentDescription("tvChargerConnection1")).perform(click());
        onView(withId((R.id.tvResConnectorType))).check(matches(withText("Type 2 (Socket Only)")));
        onView(withId((R.id.tvResPower))).check(matches(withText("22.0 KW")));
        onView(withId((R.id.tvResQuantity))).check(matches(withText("4")));

    }
}