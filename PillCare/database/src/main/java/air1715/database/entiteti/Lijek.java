package air1715.database.entiteti;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by Andrea on 30.10.2017.
 */

@Table(database = NaslovnicaBazePodataka.class)
public class Lijek {
    @PrimaryKey
    @Column int id;

    @Column String naziv;

    @Column int jacina;

    @Column int brojTableta;

    @Column String pakiranje;

    @Column String upute;

    @Column int proizvodacId;

    @ForeignKey(tableClass = Proizvodac.class)
    @Column Proizvodac proizvodac;
}
