package uk.ac.le.co2103.part2;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

// PLEASE READ
// Sometimes when I have imported this on lab PCs, it gives an error about 'R' not existing
// It certainly does exist and the tests work fine, I believe its an issue with the file syncing on lab PCs
// The tests all run fine on their own or as a whole, but due to threading issues, database clearing can be dodgy
@RunWith(AndroidJUnit4ClassRunner.class)
public class ShoppingListTest {

    @Rule
    public ActivityTestRule rule = new ActivityTestRule<>(MainActivity.class);


    @BeforeClass
    public static void clearProductDB() {
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase("products ");
    }

    @BeforeClass
    public static void clearShoppingListDB() {
        InstrumentationRegistry.getInstrumentation().getTargetContext().deleteDatabase("shopping_lists");
    }

    @Before
    public void clearDB() {
        clearProductDB();
        clearShoppingListDB();
    }

    @Test
    public void testAddNewList() {

        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.editTextName)).perform(typeText("Birthday Party"), closeSoftKeyboard());
        onView(withId(R.id.button_create_list)).perform(click());

        onView(withText("Birthday Party")).check(matches(isDisplayed()));

    }

    @Test
    public void testDeleteList() {

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("Birthday Party"), closeSoftKeyboard());
        onView(withId(R.id.button_create_list)).perform(click());

        onView(withText("Birthday Party")).perform(longClick());
        onView(withText(R.string.button_yes)).perform(click());

        onView(withText("Birthday Party")).check(doesNotExist());

    }

    @Test
    public void testAddProduct() {

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("Birthday Party"), closeSoftKeyboard());
        onView(withId(R.id.button_create_list)).perform(click());

        onView(withText("Birthday Party")).perform(click());


        onView(withId(R.id.fabAddProduct)).perform(click());

        onView(withId(R.id.editTextName)).perform(typeText("Cake"), closeSoftKeyboard());
        onView(withId(R.id.editTextQuantity)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.spinner)).perform(click());
        onView(withText("Unit")).perform(click());

        onView(withId(R.id.button_create_product)).perform(click());
        onView(withText("Cake (1 Unit)")).check(matches(isDisplayed()));

    }

    @Test
    public void testAddDuplicateProduct() {

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("Birthday Party"), closeSoftKeyboard());
        onView(withId(R.id.button_create_list)).perform(click());

        onView(withText("Birthday Party")).perform(click());


        onView(withId(R.id.fabAddProduct)).perform(click());

        onView(withId(R.id.editTextName)).perform(typeText("Cake"), closeSoftKeyboard());
        onView(withId(R.id.editTextQuantity)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.spinner)).perform(click());
        onView(withText("Unit")).perform(click());
        onView(withId(R.id.button_create_product)).perform(click());

        onView(withId(R.id.fabAddProduct)).perform(click());

        onView(withId(R.id.editTextName)).perform(typeText("Cake"), closeSoftKeyboard());
        onView(withId(R.id.editTextQuantity)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.spinner)).perform(click());
        onView(withText("Unit")).perform(click());
        onView(withId(R.id.button_create_product)).perform(click());

        onView(withText("Product already exists")).inRoot(withDecorView(not(rule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testEditProduct() {

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("Birthday Party"), closeSoftKeyboard());
        onView(withId(R.id.button_create_list)).perform(click());

        onView(withText("Birthday Party")).perform(click());


        onView(withId(R.id.fabAddProduct)).perform(click());

        onView(withId(R.id.editTextName)).perform(typeText("Cake"), closeSoftKeyboard());
        onView(withId(R.id.editTextQuantity)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.spinner)).perform(click());
        onView(withText("Unit")).perform(click());

        onView(withId(R.id.button_create_product)).perform(click());

        onView(withText("Cake (1 Unit)")).perform((click()));
        onView(withText(R.string.edit_product_button)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.incrementButton)).perform(click());
        onView(withId(R.id.incrementButton)).perform(click());

        onView(withId(R.id.saveProductChangesButton)).perform(click());
        onView(withText("Cake (3 Unit)")).check(matches(isDisplayed()));

    }

    @Test
    public void testDeleteProduct() {

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.editTextName)).perform(typeText("Birthday Party"), closeSoftKeyboard());
        onView(withId(R.id.button_create_list)).perform(click());

        onView(withText("Birthday Party")).perform(click());


        onView(withId(R.id.fabAddProduct)).perform(click());

        onView(withId(R.id.editTextName)).perform(typeText("Cake"), closeSoftKeyboard());
        onView(withId(R.id.editTextQuantity)).perform(typeText("1"), closeSoftKeyboard());
        onView(withId(R.id.spinner)).perform(click());
        onView(withText("Unit")).perform(click());

        onView(withId(R.id.button_create_product)).perform(click());
        onView(withText("Cake (1 Unit)")).perform((click()));
        onView(withText(R.string.edit_product_button)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

        onView(withText("Cake (1 Unit")).check(doesNotExist());
    }
}