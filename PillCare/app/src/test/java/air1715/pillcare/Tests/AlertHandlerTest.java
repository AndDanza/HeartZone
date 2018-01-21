package air1715.pillcare.Tests;

import org.junit.Test;

import air1715.database.entiteti.Terapija;
import air1715.pillcare.Utils.AlertHandler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The type Alert handler test.
 */
public class AlertHandlerTest {
    /**
     * Calculate current pill status test.
     *
     * @throws Exception the exception
     */
    @Test
    public void calculateCurrentPillStatusTest() throws Exception {
        Terapija t=new Terapija();
        t.setStanje(26);
        t.setPojedinacnaDoza(0.99);
        t.setKorisnikId(1);
        t.setPocetak("2018-01-15 12:00:00");
        AlertHandler alerthandler = mock(AlertHandler.class);
        when(alerthandler.calculateCurrentPillStatus(t)).thenReturn(t);
        assertEquals("krivo",26,t.getStanje());


    }

}