package com.easyhelp.application.service.stored_blood;

import com.easyhelp.application.model.blood.BloodComponent;
import com.easyhelp.application.model.blood.StoredBlood;
import com.easyhelp.application.model.dto.misc.outgoing.BloodStockDTO;
import com.easyhelp.application.model.locations.DonationCenter;
import com.easyhelp.application.repository.StoredBloodRepository;
import com.easyhelp.application.service.donationcenter.DonationCenterServiceInterface;
import com.easyhelp.application.utils.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoredBloodServiceImpl implements StoredBloodServiceInterface {

    @Autowired
    private StoredBloodRepository storedBloodRepository;

    @Autowired
    private DonationCenterServiceInterface donationCenterService;

    @Override
    public StoredBlood storeBlood(StoredBlood storedBlood) {
        return storedBloodRepository.save(storedBlood);
    }

    @Override
    public StoredBlood findById(Long storedBloodId) throws EntityNotFoundException {
        Optional<StoredBlood> storedBloodOptional = storedBloodRepository.findById(storedBloodId);

        if (storedBloodOptional.isPresent()) {
            return storedBloodOptional.get();
        }

        throw new EntityNotFoundException("Stored blood with that id is not in the db");
    }

    @Override
    public List<StoredBlood> getAvailableBloodInDC(Long donationCenterId) throws EntityNotFoundException {
        DonationCenter donationCenter = donationCenterService.findById(donationCenterId);

        return donationCenter.getStoredBloodSet().stream()
                .filter(storedBlood -> storedBlood.getDonationCommitment() == null)
                .filter(StoredBlood::getIsUsable)
                .filter(storedBlood -> !storedBlood.isExpired())
                .collect(Collectors.toList());
    }

    @Override
    public void removeBlood(Long storedBloodId) throws EntityNotFoundException {
        StoredBlood storedBlood = findById(storedBloodId);

        storedBlood.getDonationCenter().getStoredBloodSet().remove(storedBlood);
        storedBlood.getDonor().getStoredBloodSet().remove(storedBlood);
        storedBlood.getSeparatedBloodType().getStoredBloodSet().remove(storedBlood);

        storedBloodRepository.delete(storedBlood);
    }

    @Override
    public List<StoredBlood> getExpiredBloodInDC(Long donationCenterId) {
        return storedBloodRepository
                .findAll()
                .stream()
                .filter(storedBlood -> storedBlood.getDonationCenter().getId().equals(donationCenterId))
                .filter(StoredBlood::isExpired)
                .collect(Collectors.toList());
    }

    @Override
    public List<BloodStockDTO> getBloodStocksInCountry() {
        try {
            Connection connection = DriverManager.getConnection(
                    System.getenv("JDBC_DATABASE_URL"),
                    System.getenv("JDBC_DATABASE_USERNAME"),
                    System.getenv("JDBC_DATABASE_PASSWORD")
            );

            Statement stm = connection.createStatement();
            String statement = "SELECT Sum(t.amount), t2.component FROM public.stored_bloods t\n" +
                    "inner join separated_blood_type t2 on t.fk_separated_blood_type = t2.id\n" +
                    "where t.is_usable=true\n" +
                    "group by t2.component";
            ResultSet rs = stm.executeQuery(statement);

            List<BloodStockDTO> dtoList = new ArrayList<>();

            while (rs.next()) {
                Double amount = rs.getDouble(1);
                BloodComponent component = BloodComponent.getFromDBValue(rs.getInt(2));
                BloodStockDTO newBlood = new BloodStockDTO();
                newBlood.setAmount(amount);
                newBlood.setComponent(component);
                dtoList.add(newBlood);
            }

            return dtoList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<BloodStockDTO> getBloodStocksInDC(Long donationCenterId) {
        try {
            Connection connection = DriverManager.getConnection(
                    System.getenv("JDBC_DATABASE_URL"),
                    System.getenv("JDBC_DATABASE_USERNAME"),
                    System.getenv("JDBC_DATABASE_PASSWORD")
            );

            Statement stm = connection.createStatement();
            String statement = "SELECT Sum(t.amount), t2.component FROM public.stored_bloods t\n" +
                    "inner join separated_blood_type t2 on t.fk_separated_blood_type = t2.id and t.fk_donation_center=" + donationCenterId + "\n" +
                    "where t.is_usable=true\n" +
                    "group by t2.component";
            ResultSet rs = stm.executeQuery(statement);

            List<BloodStockDTO> dtoList = new ArrayList<>();

            while (rs.next()) {
                Double amount = rs.getDouble(1);
                BloodComponent component = BloodComponent.getFromDBValue(rs.getInt(2));
                BloodStockDTO newBlood = new BloodStockDTO();
                newBlood.setAmount(amount);
                newBlood.setComponent(component);
                dtoList.add(newBlood);
            }

            return dtoList;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
