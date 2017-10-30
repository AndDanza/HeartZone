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

    public Terapija() {
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

    public boolean isAktivna() {
        return aktivna;
    }

    public void setAktivna(boolean aktivna) {
        this.aktivna = aktivna;
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
}
