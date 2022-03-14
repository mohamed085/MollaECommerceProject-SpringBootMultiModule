package com.molla.site.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import com.molla.common.entity.AuthenticationType;
import com.molla.common.entity.Country;
import com.molla.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateCustomer1() {
        Integer countryId = 1; // Egypt
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Mohamed");
        customer.setLastName("Ahmed");
        customer.setPassword("password123");
        customer.setEmail("mohamed.ahmed@gmail.com");
        customer.setPhoneNumber("01012700000");
        customer.setAddressLine1("1927 West Drive");
        customer.setCity("Cairo");
        customer.setState("Cairo");
        customer.setPostalCode("323232");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = repo.save(customer);
        System.out.println(savedCustomer);
        assertNotNull(savedCustomer);
    }

    @Test
    public void testListCustomers() {
        Iterable<Customer> customers = repo.findAll();
        customers.forEach(System.out::println);

        assertThat(customers).hasSizeGreaterThan(1);
    }

    @Test
    public void testUpdateCustomer() {
        Integer customerId = 1;
        String lastName = "Stanfield";

        Customer customer = repo.findById(customerId).get();
        customer.setLastName(lastName);
        customer.setEnabled(true);

        Customer updatedCustomer = repo.save(customer);
        assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void testGetCustomer() {
        Integer customerId = 2;
        Optional<Customer> findById = repo.findById(customerId);

        assertThat(findById).isPresent();

        Customer customer = findById.get();
        System.out.println(customer);
    }

    @Test
    public void testDeleteCustomer() {
        Integer customerId = 2;
        repo.deleteById(customerId);

        Optional<Customer> findById = repo.findById(customerId);
        assertThat(findById).isNotPresent();
    }

    @Test
    public void testFindByEmail() {
        String email = "mohamed.ahmed5@gmail.com";
        Customer customer = repo.findByEmail(email);

        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testFindByVerificationCode() {
        String code = "code_123";
        Customer customer = repo.findByVerificationCode(code);

        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testEnableCustomer() {
        Integer customerId = 1;
        repo.enable(customerId);

        Customer customer = repo.findById(customerId).get();
        assertThat(customer.isEnabled()).isTrue();
    }

    @Test
    public void testUpdateAuthenticationType() {
        Integer id = 1;
        repo.updateAuthenticationType(id, AuthenticationType.DATABASE);

        Customer customer = repo.findById(id).get();

        assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);
    }
}