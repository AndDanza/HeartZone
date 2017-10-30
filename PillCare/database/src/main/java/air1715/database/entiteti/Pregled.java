package air1715.database.entiteti;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by Andrea on 30.10.2017.
 */

@Table(database = NaslovnicaBazePodataka.class)
public class Pregled {
    @PrimaryKey
    @Column int id;

    @Column String termin;

    @Column String biljeska;

    @Column String vrijemeUpozorenja;

    @Column boolean aktivan;

    @Column int korisnikId;

    @ForeignKey(tableClass = Korisnik.class)
    @Column Korisnik korisnik;
}
