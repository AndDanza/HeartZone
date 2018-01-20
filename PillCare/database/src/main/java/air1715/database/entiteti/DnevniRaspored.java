package air1715.database.entiteti;

/**
 * Created by hrzz00dq on 20/01/2018.
 */

public class DnevniRaspored {

    Double pojedinacnaDoza;

    Lijek lijek;

    String termin;

    String biljeska;

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

    public Double getPojedinacnaDoza() {
        return pojedinacnaDoza;
    }

    public void setPojedinacnaDoza(Double pojedinacnaDoza) {
        this.pojedinacnaDoza = pojedinacnaDoza;
    }

    public Lijek getLijek() {
        return lijek;
    }

    public void setLijek(Lijek lijek) {
        this.lijek = lijek;
    }


    @Override
    public String toString() {
        String poruka = "";
        if(lijek != null)
            poruka = "Morate uzeti:" + System.getProperty("line.separator")
                + "Lijek: " + getLijek().getNaziv() + System.getProperty("line.separator")
                + "Pojedinacna doza: " + getPojedinacnaDoza() + System.getProperty("line.separator");
        else
            poruka = "Imate pregled: " + System.getProperty("line.separator")
                + "Termin: " + getTermin() + System.getProperty("line.separator")
                + "Biljeska: " + getBiljeska() + System.getProperty("line.separator");

        return poruka;
    }
}
