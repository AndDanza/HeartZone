package air1715.pillcare;

import android.support.test.filters.SmallTest;
import android.test.ActivityInstrumentationTestCase2;
import org.junit.Test;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import air1715.pillcare.Activities.Registracija_Activity;

/**
 * Created by Toni on 19.1.2018..
 */

public class CheckUserTest extends ActivityInstrumentationTestCase2<Registracija_Activity> {
    public CheckUserTest(Class<Registracija_Activity> activityClass) {
        super(activityClass);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void checkUser(){


        assertEquals(equals);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
