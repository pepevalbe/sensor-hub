-- This script is executed on startup if spring.jpa.hibernate.ddl-auto is set to create or create-drop
-- It is meant to be used for the embedded H2 database in a local development enviroment, not for producion

-- Adding data user in person table. Username: data, Password: data
INSERT INTO public.person(id, activated, creation_timestamp, door_register_active_flag, email, first_name, last_name, password, role, token, username)	VALUES (999, true, CURRENT_TIMESTAMP, true, 'data@email.com', 'dataName', 'dataLastName', '$2a$10$8e7ei9HAbMvmtn3qXiAoQOg3CJmFpgFGV9GXHx3qxBkxW/RI3PIGi', 'ADMIN', '8b47b8a7-9fd6-47a9-ab63-f4fd9fc9a22b', 'data');

-- Adding some temperature and humidity entries for data user
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (10, 20.0, 10.0, DATEADD('HOUR',-12, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (11, 20.1, 10.2, DATEADD('HOUR',-11, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (12, 20.2, 10.4, DATEADD('HOUR',-10, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (13, 20.3, 10.6, DATEADD('HOUR',-9, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (14, 20.4, 10.8, DATEADD('HOUR',-8, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (15, 20.4, 11.4, DATEADD('HOUR',-7, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (16, 20.4, 11.6, DATEADD('HOUR',-6, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (17, 20.3, 12.1, DATEADD('HOUR',-5, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (18, 20.3, 12.2, DATEADD('HOUR',-4, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (19, 20.2, 12.2, DATEADD('HOUR',-3, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (20, 20.2, 12.0, DATEADD('HOUR',-2, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (21, 20.2, 11.8, DATEADD('HOUR',-1, CURRENT_TIMESTAMP), 999);
INSERT INTO public.temp_humidity(id, humidity, temperature, timestamp, owner_id) VALUES (22, 20.1, 11.5, CURRENT_TIMESTAMP, 999);