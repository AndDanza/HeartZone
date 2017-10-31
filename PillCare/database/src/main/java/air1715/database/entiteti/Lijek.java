package air1715.database.entiteti;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by Andrea on 30.10.2017.
 */

public class Lijek implements Serializable {

    int id;

    String naziv;

    int jacina;

    int brojTableta;

    String pakiranje;

    String upute;

    int proizvodacId;

    Proizvodac proizvodac;

    public Lijek() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getJacina() {
        return jacina;
    }

    public void setJacina(int jacina) {
        this.jacina = jacina;
    }

    public int getBrojTableta() {
        return brojTableta;
    }

    public void setBrojTableta(int brojTableta) {
        this.brojTableta = brojTableta;
    }

    public String getPakiranje() {
        return pakiranje;
    }

    public void setPakiranje(String pakiranje) {
        this.pakiranje = pakiranje;
    }

    public String getUpute() {
        return upute;
    }

    public void setUpute(String upute) {
        this.upute = upute;
    }

    public int getProizvodacId() {
        return proizvodacId;
    }

    public void setProizvodacId(int proizvodacId) {
        this.proizvodacId = proizvodacId;
    }

    public Proizvodac getProizvodac() {
        return proizvodac;
    }

    public void setProizvodac(Proizvodac proizvodac) {
        this.proizvodac = proizvodac;
    }
}