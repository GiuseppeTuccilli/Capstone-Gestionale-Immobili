package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "richieste")
@NoArgsConstructor
@Getter
@Setter
public class Richiesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    public Richiesta(double prezzoMassimo, double superficieMinimo, double superficieMassimo,
                     int vaniMinimo, int vaniMassimo, int localiMinimo, int localiMassimo,
                     boolean cantina, boolean ascensore, boolean postoAuto, boolean giardinoPrivato,
                     boolean terrazzo, boolean arredato, String comune, String provincia, Cliente cliente) {
        this.prezzoMassimo = prezzoMassimo;
        this.superficieMinimo = superficieMinimo;
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
        this.cliente = cliente;
        this.data = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Richiesta{" +
                "id=" + id +
                ", prezzoMassimo=" + prezzoMassimo +
                ", superficieMinimo=" + superficieMinimo +
                ", vaniMinimo=" + vaniMinimo +
                ", vaniMassimo=" + vaniMassimo +
                '}';
    }
}
