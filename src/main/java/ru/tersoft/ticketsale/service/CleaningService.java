package ru.tersoft.ticketsale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tersoft.ticketsale.entity.Attraction;
import ru.tersoft.ticketsale.entity.Maintenance;
import ru.tersoft.ticketsale.repository.AttractionRepository;
import ru.tersoft.ticketsale.repository.MaintenanceRepository;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * Project ticketsale.
 * Created by ivyanni on 18.01.2017.
 */
@Service("CleaningService")
@Transactional
public class CleaningService {
    private final MaintenanceRepository maintenanceRepository;
    private final AttractionRepository attractionRepository;

    @Autowired
    public CleaningService(MaintenanceRepository maintenanceRepository, AttractionRepository attractionRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.attractionRepository = attractionRepository;
    }

    @Scheduled(cron = "0 1 0 * * *")
    @Async
    public void cleanMaintenances() {
        Logger log = Logger.getLogger(this.getClass().getName());
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, -1);
        List<Attraction> attractions = attractionRepository.findAll();
        log.info("Cleaning expired maintenances...");
        for(Attraction attraction : attractions) {
            Maintenance maintenance = attraction.getMaintenance();
            if(maintenance != null) {
                if (maintenance.getEnddate() != null) {
                    if (maintenance.getEnddate().before(today.getTime())) {
                        attraction.setMaintenance(null);
                        maintenanceRepository.delete(maintenance);
                        log.info("Deleted expired maintenance "
                                + maintenance.getId().toString()
                                + " of attraction " + attraction.getId().toString());
                    }
                }
            }
        }
    }
}
