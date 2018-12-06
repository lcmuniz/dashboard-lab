package br.ufma.lsdi.dashboardlab.dashboardlab.repository;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.database.DBContextData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBContextDataRepository extends JpaRepository<DBContextData, Long> {
}
