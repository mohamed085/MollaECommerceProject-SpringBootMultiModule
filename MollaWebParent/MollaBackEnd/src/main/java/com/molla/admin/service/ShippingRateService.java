package com.molla.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.molla.admin.paging.PagingAndSortingHelper;
import com.molla.admin.repository.CountryRepository;
import com.molla.admin.repository.ShippingRateRepository;
import com.molla.admin.service.impl.IShippingRateService;
import com.molla.common.entity.Country;
import com.molla.common.entity.ShippingRate;
import com.molla.common.exception.ShippingRateAlreadyExistsException;
import com.molla.common.exception.ShippingRateNotFoundException;

@Service
@Transactional
public class ShippingRateService implements IShippingRateService{

    public static final int RATES_PER_PAGE = 10;

    private ShippingRateRepository shipRepo;

    private CountryRepository countryRepo;

    @Autowired
    public ShippingRateService(ShippingRateRepository shipRepo, CountryRepository countryRepo) {
        super();
        this.shipRepo = shipRepo;
        this.countryRepo = countryRepo;
    }

    @Override
    public void listByPage(int pageNum, PagingAndSortingHelper helper) {
        // TODO Auto-generated method stub
        helper.listEntities(pageNum, RATES_PER_PAGE, shipRepo);
    }

    @Override
    public List<Country> listAllCountries() {
        // TODO Auto-generated method stub
        return countryRepo.findAllByOrderByNameAsc();
    }

    @Override
    public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
        // TODO Auto-generated method stub
        ShippingRate rateInDB = shipRepo.findByCountryAndState(
                rateInForm.getCountry().getId(), rateInForm.getState());
        boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
        boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null && !rateInDB.equals(rateInForm);

        if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
            throw new ShippingRateAlreadyExistsException("There's already a rate for the destination "
                    + rateInForm.getCountry().getName() + ", " + rateInForm.getState());
        }
        shipRepo.save(rateInForm);
    }

    @Override
    public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
        // TODO Auto-generated method stub
        try {
            return shipRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
        }
    }

    @Override
    public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
        // TODO Auto-generated method stub
        Long count = shipRepo.countById(id);
        if (count == null || count == 0) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
        }

        shipRepo.updateCODSupport(id, codSupported);
    }

    @Override
    public void delete(Integer id) throws ShippingRateNotFoundException {
        // TODO Auto-generated method stub
        Long count = shipRepo.countById(id);
        if (count == null || count == 0) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);

        }
        shipRepo.deleteById(id);
    }

}

