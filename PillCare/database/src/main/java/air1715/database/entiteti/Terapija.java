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

public class Terapija extends BaseModel implements Serializable {
    @PrimaryKey
    @Column
    int id;

    @Column
    int lijekoviId;

    @Column
    int korisnikId;

    @Column
    String pocetak;

    @Column
    String kraj;

    @Column
    Double pojedinacnaDoza;

    @Column
    int brojDnevnihDoza;

    @Column
    int upozorenje;

    @Column
    int razmakDnevnihDoza;

    @Column
    int stanje;

    @ForeignKey(tableClass = Korisnik.class)
    @Column
    Korisnik korisnik;

    @ForeignKey(tableClass = Lijek.class)
    @Column
    Lijek lijek;

    public Terapija() {
    }

    public Terapija(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.lijekoviId = jsonObject.getInt("lijekoviId");
        this.korisnikId = jsonObject.getInt("korisnikId");
        this.pocetak = jsonObject.getString("pocetak");
        this.kraj = jsonObject.getString("kraj");
        this.pojedinacnaDoza = jsonObject.getDouble("pojedinacnaDoza");
        this.upozorenje = jsonObject.getInt("upozorenje");
        this.razmakDnevnihDoza = jsonObject.getInt("razmakDnevnihDoza");
        this.brojDnevnihDoza = jsonObject.getInt("brojDnevnihDoza");
        this.stanje = jsonObject.getInt("stanje");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStanje() {
        return stanje;
    }

    public void setStanje(int stanje) {
        this.stanje = stanje;
    }

    public int getLijekoviId() {
        return lijekoviId;
    }

    public void setLijekoviId(int lijekoviId) {
        this.lijekoviId = lijekoviId;
    }

    public int getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(int korisnikId) {
        this.korisnikId = korisnikId;
    }

    public String getPocetak() {
        return pocetak;
    }

    public void setPocetak(String pocetak) {
        this.pocetak = pocetak;
    }

    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }

    public Double getPojedinacnaDoza() {
        return pojedinacnaDoza;
    }

    public void setPojedinacnaDoza(Double pojedinacnaDoza) {
        this.pojedinacnaDoza = pojedinacnaDoza;
    }

    public int getBrojDnevnihDoza() {
        return brojDnevnihDoza;
    }

    public void setBrojDnevnihDoza(int brojDnevnihDoza) {
        this.brojDnevnihDoza = brojDnevnihDoza;
    }

    public int getUpozorenje() {
        return upozorenje;
    }

    public void setUpozorenje(int upozorenje) {
        this.upozorenje = upozorenje;
    }

    public int getRazmakDnevnihDoza() {
        return razmakDnevnihDoza;
    }

    public void setRazmakDnevnihDoza(int razmakDnevnihDoza) {
        this.razmakDnevnihDoza = razmakDnevnihDoza;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Lijek getLijek() {
        return lijek;
    }

    public void setLijek(Lijek lijek) {
        this.lijek = lijek;
    }

    public List<Terapija> getAll() {
        return SQLite.select().from(Terapija.class).queryList();
    }
}
