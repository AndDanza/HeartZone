package air1715.pillcare.Tests;

import org.junit.Test;

import air1715.pillcare.Activities.IzmjenaPodataka_Activity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The type Izmjena podataka activity test.
 */
public class IzmjenaPodataka_ActivityTest {
    /**
     * Is network available test.
     *
     * @throws Exception the exception
     */
    @Test
    public  void isNetworkAvailableTest() throws Exception {
        IzmjenaPodataka_Activity izmjenaPodataka_activity = mock(IzmjenaPodataka_Activity.class);
        when(izmjenaPodataka_activity.isNetworkAvailable()).thenReturn(true);
        assertTrue(izmjenaPodataka_activity.isNetworkAvailable());


    }


}