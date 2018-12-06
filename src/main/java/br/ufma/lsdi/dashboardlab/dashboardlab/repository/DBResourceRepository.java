package br.ufma.lsdi.dashboardlab.dashboardlab.repository;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.database.DBResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBResourceRepository extends JpaRepository<DBResource, String> {
}
