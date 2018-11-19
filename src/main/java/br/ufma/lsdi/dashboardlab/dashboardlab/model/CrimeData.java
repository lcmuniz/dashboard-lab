package br.ufma.lsdi.dashboardlab.dashboardlab.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class CrimeData {
    private List<Crime> data = new ArrayList<>();
}
