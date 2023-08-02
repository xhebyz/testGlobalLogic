package cl.saraos.bank.service;

import cl.saraos.bank.entity.PhoneEntity;
import cl.saraos.bank.entity.UserEntity;
import cl.saraos.bank.repository.PhoneRepository;
import cl.saraos.bank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PhoneService {

    private final PhoneRepository phoneRepository;

    public PhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }
    
    public PhoneEntity saveUser(PhoneEntity user) {
        return phoneRepository.save(user);
    }

    public Optional<PhoneEntity> getUserById(Long id) {
        return phoneRepository.findById(id);
    }

    public List<PhoneEntity> getAllUsers() {
        return StreamSupport.stream(phoneRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public PhoneEntity updateUser(PhoneEntity user) {
        return phoneRepository.save(user);
    }

    public void deleteUserById(Long id) {
        phoneRepository.deleteById(id);
    }
}
