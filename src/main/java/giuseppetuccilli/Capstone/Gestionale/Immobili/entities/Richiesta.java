package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "richieste")
@NoArgsConstructor
@Getter
@Setter
public class Richiesta {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;
    private double prezzoMassimo;
    private double superficieMinimo;
    private double superficieMassimo;
    private int vaniMinimo;
    private int vaniMassimo;
    private int localiMinimo;
    private int localiMassimo;
    private boolean cantina;
    private boolean ascensore;
    private boolean postoAuto;
    private boolean giardinoPrivato;
    private boolean terrazzo;
    private boolean arredato;
    private String comune;
    private String provincia;

    public Richiesta(double superficieMinimo, double prezzoMassimo, double superficieMassimo,
                     int vaniMinimo, int vaniMassimo, int localiMinimo, int localiMassimo,
                     boolean cantina, boolean ascensore, boolean postoAuto, boolean giardinoPrivato,
                     boolean terrazzo, boolean arredato, String comune, String provincia) {
        this.superficieMinimo = superficieMinimo;
        this.prezzoMassimo = prezzoMassimo;
        this.superficieMassimo = superficieMassimo;
        this.vaniMinimo = vaniMinimo;
        this.vaniMassimo = vaniMassimo;
        this.localiMinimo = localiMinimo;
        this.localiMassimo = localiMassimo;
        this.cantina = cantina;
        this.ascensore = ascensore;
        this.postoAuto = postoAuto;
        this.giardinoPrivato = giardinoPrivato;
        this.terrazzo = terrazzo;
        this.arredato = arredato;
        this.comune = comune;
        this.provincia = provincia;
    }
}
