package air1715.pillcare.Tests;

import org.junit.Test;

import air1715.pillcare.Activities.Registracija_Activity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Registracija_ActivityTest {
    @Test
    public void existsUsernameOrEmailTest() throws Exception {

        String name="korisnik";
        String pass="korisnik";
        Registracija_Activity r=new Registracija_Activity();

        Registracija_Activity registracija=mock(Registracija_Activity.class);
        when(registracija.existsUsernameOrEmail(name,pass)).thenReturn(true);
        assertTrue("krivo",registracija.existsUsernameOrEmail(name,pass));


    }

}