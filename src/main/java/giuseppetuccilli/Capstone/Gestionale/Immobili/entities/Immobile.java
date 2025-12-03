package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import giuseppetuccilli.Capstone.Gestionale.Immobili.enums.MacroTipologiaImmobile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "immobili")
@NoArgsConstructor
@Getter
@Setter
public class Immobile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    @Enumerated(EnumType.STRING)
    private MacroTipologiaImmobile MacroTipologia;
    private double superficie;
    private int locali;
    private int vani;
    private String descrizione;
    private double prezzo;
    private boolean cantina = false;
    private boolean ascensore = false;
    private boolean postoAuto = false;
    private boolean giardinoPrivato = false;
    private boolean terrazzo = false;
    private boolean arredato = false;
    private String indirizzo;

    @ManyToOne
    @JoinColumn(name = "id_comune")
    private Comune comune;

    @ManyToOne
    @JoinColumn(name = "id_ditta")
    private Ditta ditta;

    /*
        @OneToMany(mappedBy = "immobile")
        private List<FotoImmobile> foto;
    */
    public Immobile(MacroTipologiaImmobile macroTipologia, double superficie, int locali, int vani,
                    String descrizione, double prezzo, boolean cantina, boolean ascensore,
                    boolean postoAuto, boolean giardinoPrivato, boolean terrazzo, boolean arredato,
                    String indirizzo, Comune comune, Ditta ditta) {
        MacroTipologia = macroTipologia;
        this.superficie = superficie;
        this.locali = locali;
        this.vani = vani;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.cantina = cantina;
        this.ascensore = ascensore;
        this.postoAuto = postoAuto;
        this.giardinoPrivato = giardinoPrivato;
        this.terrazzo = terrazzo;
        this.arredato = arredato;
        this.indirizzo = indirizzo;
        this.comune = comune;
        this.ditta = ditta;

    }

    @Override
    public String toString() {
        return "Immobile{" +
                "id=" + id +
                ", MacroTipologia=" + MacroTipologia +
                ", superficie=" + superficie +
                ", locali=" + locali +
                ", vani=" + vani +
                ", prezzo=" + prezzo +
                '}';
    }
}
