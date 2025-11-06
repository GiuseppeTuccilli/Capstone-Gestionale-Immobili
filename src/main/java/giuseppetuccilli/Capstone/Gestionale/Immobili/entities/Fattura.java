package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "fatture")
@NoArgsConstructor
@Getter
@Setter
public class Fattura {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long numero;
    private double importo;
    private LocalDate data;
    private String causale;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    public Fattura(double importo, Cliente cliente, String causale, LocalDate data) {
        this.importo = importo;
        this.data = data;
        this.cliente = cliente;
        this.causale = causale;
    }
}
