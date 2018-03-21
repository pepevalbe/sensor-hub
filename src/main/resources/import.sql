-- This script is executed on startup if spring.jpa.hibernate.ddl-auto is set to create or create-drop
-- It is meant to be used for the embedded H2 database in a local development enviroment, not for producion

-- Configuration table: public.config_variable
-- This configuration is needed for the app to work. It can be set with enviroment variables as well 
INSERT INTO public.config_variable(id, var_key, var_value) VALUES (1, 'APP_BASE_URL', 'http://localhost:8080');
INSERT INTO public.config_variable(id, var_key, var_value) VALUES (2, 'EMAIL_USERNAME', 'pepe.sensores@gmail.com');
INSERT INTO public.config_variable(id, var_key, var_value) VALUES (3, 'EMAIL_PASSWORD', 'gelpmbhpkvjxnsft');
INSERT INTO public.config_variable(id, var_key, var_value) VALUES (4, 'WEATHER_URL', 'http://openweathermap.org/data/2.5/weather?q=Madrid,es&units=metric&appid=?');

-- Adding data user in person table. Username: data, Password: data
INSERT INTO public.person(id, activated, creation_timestamp, door_register_active_flag, email, first_name, last_name, password, role, token, username)	VALUES (1, true, CURRENT_TIMESTAMP, true, 'data@email.com', 'dataName', 'dataLastName', '$2a$10$8e7ei9HAbMvmtn3qXiAoQOg3CJmFpgFGV9GXHx3qxBkxW/RI3PIGi', 'ADMIN', '8b47b8a7-9fd6-47a9-ab63-f4fd9fc9a22b', 'data');

-- Adding some temperature and humidity entries for data user
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (10, 20.0, 10.0, DATEADD('HOUR',-12, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (11, 20.1, 10.2, DATEADD('HOUR',-11, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (12, 20.2, 10.4, DATEADD('HOUR',-10, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (13, 20.3, 10.6, DATEADD('HOUR',-9, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (14, 20.4, 10.8, DATEADD('HOUR',-8, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (15, 20.4, 11.4, DATEADD('HOUR',-7, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (16, 20.4, 11.6, DATEADD('HOUR',-6, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (17, 20.3, 12.1, DATEADD('HOUR',-5, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (18, 20.3, 12.2, DATEADD('HOUR',-4, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (19, 20.2, 12.2, DATEADD('HOUR',-3, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (20, 20.2, 12.0, DATEADD('HOUR',-2, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (21, 20.2, 11.8, DATEADD('HOUR',-1, CURRENT_TIMESTAMP), 1);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (22, 20.1, 11.5, CURRENT_TIMESTAMP, 1);

-- This would be with a PostgreSQL database (using interval instead of dateadd)
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (10, 20.0, 10.0, CURRENT_TIMESTAMP - 12 * INTERVAL '1 hour', 1);
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (11, 20.1, 10.2, CURRENT_TIMESTAMP - 11 * INTERVAL '1 hour', 1);
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (12, 20.2, 10.4, CURRENT_TIMESTAMP - 10 * INTERVAL '1 hour', 1);
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (13, 20.3, 10.6, CURRENT_TIMESTAMP - 9 * INTERVAL '1 hour', 1);
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (14, 20.4, 10.8, CURRENT_TIMESTAMP - 8 * INTERVAL '1 hour', 1);
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (15, 20.4, 11.4, CURRENT_TIMESTAMP - 7 * INTERVAL '1 hour', 1);
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (16, 20.4, 11.6, CURRENT_TIMESTAMP - 6 * INTERVAL '1 hour', 1);
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (17, 20.3, 12.1, CURRENT_TIMESTAMP - 5 * INTERVAL '1 hour', 1);
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (18, 20.3, 12.2, CURRENT_TIMESTAMP - 4 * INTERVAL '1 hour', 1);
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (19, 20.2, 12.2, CURRENT_TIMESTAMP - 3 * INTERVAL '1 hour', 1);
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (20, 20.2, 12.0, CURRENT_TIMESTAMP - 2 * INTERVAL '1 hour', 1);
-- INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (21, 20.2, 11.8, CURRENT_TIMESTAMP - INTERVAL '1 HOUR', 1);
