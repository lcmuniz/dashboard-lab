package br.ufma.lsdi.dashboardlab.dashboardlab.model.database;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class ResourceQuery {

    @Id
    @GeneratedValue
    Long id;

    String name;
    String request;

}
