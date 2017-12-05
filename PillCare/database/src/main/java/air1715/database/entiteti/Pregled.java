package air1715.database.entiteti;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andrea on 30.10.2017.
 */
@Table(database = NaslovnicaBazePodataka.class)
public class Pregled extends BaseModel implements Serializable {

    @PrimaryKey
    @Column int id;

    @Column String termin;

    @Column String biljeska;

    @Column String vrijemeUpozorenja;

    @Column boolean aktivan;

    @Column int korisnikId;

    @ForeignKey(tableClass = Korisnik.class)
    @Column Korisnik korisnik;

    public Pregled(String terminDatumVrijeme, String biljeska, String upozorenjeDatumVrijeme, boolean aktivan, int korisnikId) {
        this.termin = terminDatumVrijeme;
        this.biljeska = biljeska;
        this.vrijemeUpozorenja = upozorenjeDatumVrijeme;
        this.aktivan = aktivan;
        this.korisnikId =  korisnikId;
    }

    public Pregled(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.termin = jsonObject.getString("termin");
        this.biljeska = jsonObject.getString("biljeska");
        this.vrijemeUpozorenja = jsonObject.getString("vrijeme");
        int active=jsonObject.getInt("aktivan");
        if (active<1)
            this.aktivan = false;
        else
            this.aktivan = true;
        this.korisnikId = jsonObject.getInt("korisnik_id");
    }

    public Pregled(){}

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

    public List<Pregled> getAll(){
        return SQLite.select().from(Pregled.class).queryList();
    }

    @Override
    public String toString() {
        return "Termin: "+termin+ System.getProperty ("line.separator")
                +"Biljeska: "+biljeska+ System.getProperty ("line.separator")
                +"Upozori me: "+vrijemeUpozorenja+ System.getProperty ("line.separator");
    }
}
