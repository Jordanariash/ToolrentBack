package cl.usach.toolrent.services;

import cl.usach.toolrent.repositories.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class KardexService {
    @Autowired
    private KardexRepository kardexRepository;

}

/*
6. ReportService

Para reportes y consultas (Épica 6).

List<BorrowEntity> getActiveBorrowsReport()

List<ClientEntity> getClientsWithDelays()

List<ToolEntity> getMostBorrowedTools()
*/