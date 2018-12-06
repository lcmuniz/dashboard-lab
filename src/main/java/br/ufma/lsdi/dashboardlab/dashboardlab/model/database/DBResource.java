package br.ufma.lsdi.dashboardlab.dashboardlab.model.database;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "resource")
@Data
public class DBResource {

    @Id
    String uuid;
    String description;
    Double lat;
    Double lon;
    String capability1;
    String capability2;
    String capability3;

}
