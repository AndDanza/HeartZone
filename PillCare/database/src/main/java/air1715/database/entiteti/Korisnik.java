package air1715.database.entiteti;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by Andrea on 30.10.2017.
 */

@Table(database = NaslovnicaBazePodataka.class)
public class Korisnik {
    @PrimaryKey
    @Column int id;

    @Column String ime;

    @Column String prezime;

    @Column String email;

    @Column String korisnickoIme;

    @Column String lozinka;
}
