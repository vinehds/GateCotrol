package com.viniciusdalaqua.GateControl.services;

import com.viniciusdalaqua.GateControl.entities.Driver;
import com.viniciusdalaqua.GateControl.entities.Vehicle;
import com.viniciusdalaqua.GateControl.repositories.DriverRepository;
import com.viniciusdalaqua.GateControl.repositories.VehicleRepository;
import com.viniciusdalaqua.GateControl.services.exception.DataBaseException;
import com.viniciusdalaqua.GateControl.services.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    public VehicleService(VehicleRepository vehicleRepository, DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    public List<Vehicle> findByDriverId(Long id) {
        try {
            return vehicleRepository.findByDriverId(id);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    public Vehicle findByPlate(String plate) {
        return vehicleRepository.findByPlate(plate);
    }

    public List<Vehicle> findAll() {
        try {
            return vehicleRepository.findAll();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public Vehicle findById(Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Vehicle insert(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Long id, Vehicle obj) {
        try {
            Vehicle entity = vehicleRepository.getReferenceById(id);

            // Verificar se o motorista existe antes de atualizar o veículo
            Driver driver = driverRepository.findById(obj.getOwner().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(obj.getOwner().getId()));

            obj.setOwner(driver);  // Atualizar o motorista no objeto Vehicle

            updateData(entity, obj);  // Atualiza os dados do veículo

            return vehicleRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    public void delete(Long id){
        try {
            vehicleRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e){
            throw new DataBaseException(e.getMessage());
        }
    }

    public void updateData(Vehicle entity, Vehicle obj) {
        entity.setPlate(obj.getPlate());
        entity.setModel(obj.getModel());
        entity.setColor(obj.getColor());
        entity.setOwner(obj.getOwner());
    }

}
