package cl.saraos.bank.repository;

import cl.saraos.bank.entity.PhoneEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends CrudRepository<PhoneEntity, Long> {
    // Aquí puedes definir métodos de consulta adicionales si los necesitas
    PhoneEntity findByNumber(Long number);

}