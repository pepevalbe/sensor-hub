-- Configuration table: public.config_variable
-- This configuration is needed for the app to work. 
-- The script is executed on startup when spring.jpa.hibernate.ddl-auto is set to create or create-drop


INSERT INTO public.config_variable(id, var_key, var_value) VALUES (1, 'APP_BASE_URL', 'http://localhost:8080');
INSERT INTO public.config_variable(id, var_key, var_value) VALUES (2, 'EMAIL_USERNAME', '?@gmail.com');
INSERT INTO public.config_variable(id, var_key, var_value) VALUES (3, 'EMAIL_PASSWORD', '?');
INSERT INTO public.config_variable(id, var_key, var_value) VALUES (4, 'WEATHER_URL', 'http://openweathermap.org/data/2.5/weather?q=Madrid,es&units=metric&appid=?');