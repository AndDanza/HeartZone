package air1715.database.entiteti;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by Andrea on 30.10.2017.
 */

@Table(database = NaslovnicaBazePodataka.class)
public class Terapija {
    @PrimaryKey
    @Column int lijekoviId;

    @PrimaryKey
    @Column int korisnikId;

    @Column String pocetak;

    @Column String kraj;

    @Column Double pojedinacnaDoza;

    @Column int brojDnevnihDoza;

    @Column boolean aktivna;

    @Column int upozorenje;

    @Column int razmakDnevnihDoza;

    @ForeignKey(tableClass = Korisnik.class)
    @Column Korisnik korisnik;

    @ForeignKey(tableClass = Lijek.class)
    @Column Lijek lijek;
}
