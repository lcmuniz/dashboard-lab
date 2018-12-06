package br.ufma.lsdi.dashboardlab.dashboardlab.model.database;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "context_data_2")
@Data
public class DBContextData {

    @Id
    Long rowid;
    String resourceUuid;
    String resourceDescription;
    String capability;
    String timestamp;
    Double lat;
    Double lon;
    Double value;
    String bairro;
}
