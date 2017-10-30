package air1715.database.entiteti;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Andrea on 30.10.2017.
 */

@Table(database = NaslovnicaBazePodataka.class)
public class Pregled extends BaseModel {
    @PrimaryKey
    @Column int id;

    @Column String termin;

    @Column String biljeska;

    @Column String vrijemeUpozorenja;

    @Column boolean aktivan;

    @Column int korisnikId;

    @ForeignKey(tableClass = Korisnik.class)
    @Column Korisnik korisnik;

    public Pregled() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTermin() {
        return termin;
    }

    public void setTermin(String termin) {
        this.termin = termin;
    }

    public String getBiljeska() {
        return biljeska;
    }

    public void setBiljeska(String biljeska) {
        this.biljeska = biljeska;
    }

    public String getVrijemeUpozorenja() {
        return vrijemeUpozorenja;
    }

    public void setVrijemeUpozorenja(String vrijemeUpozorenja) {
        this.vrijemeUpozorenja = vrijemeUpozorenja;
    }

    public boolean isAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }

    public int getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(int korisnikId) {
        this.korisnikId = korisnikId;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
