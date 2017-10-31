package air1715.database.entiteti;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Andrea on 30.10.2017.
 */

public class Korisnik implements Serializable {

    int id;

    String ime;

    String prezime;

    String email;

    String korisnickoIme;

    String lozinka;

    public Korisnik() {
    }

    public Korisnik(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.ime = jsonObject.getString("ime");
        this.prezime = jsonObject.getString("prezime");
        this.korisnickoIme = jsonObject.getString("korisnicko_ime");
        this.email = jsonObject.getString("email");
        this.lozinka = jsonObject.getString("lozinka");
    }

    public Korisnik(String ime, String prezime, String email, String korisnickoIme, String lozinka) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.korisnickoIme = korisnickoIme;
        this.lozinka =  lozinka;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}
