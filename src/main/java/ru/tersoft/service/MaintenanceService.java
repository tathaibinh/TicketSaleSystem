package ru.tersoft.service;

import ru.tersoft.entity.Maintenance;

import java.util.Date;
import java.util.UUID;

public interface MaintenanceService {
    Iterable<Maintenance> getAll(UUID attractionid);
    Iterable<Maintenance> getByDate(Date today, UUID attractionid);
    Maintenance get(UUID id);
    Maintenance add(Maintenance maintenance);
    Boolean delete(UUID id);
    Boolean edit(Maintenance maintenance);
}
