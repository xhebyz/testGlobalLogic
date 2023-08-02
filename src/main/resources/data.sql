-- Insertar datos de prueba para la tabla UserEntity
INSERT INTO users(name, email, password) VALUES ('Usuario 1', 'usuario1@example.com', 'clave1');
INSERT INTO users(name, email, password) VALUES ('Usuario 2', 'usuario2@example.com', 'clave2');


-- Insertar datos de prueba para la tabla phone_entity
INSERT INTO phones(number, citycode, contrycode, user_id) VALUES ('123456789', 2, 'CL', 1);
INSERT INTO phones(number, citycode, contrycode, user_id) VALUES ('987654321', 3, 'CL', 1);
INSERT INTO phones(number, citycode, contrycode, user_id) VALUES ('55555555', 4, 'US', 2);