# Instalação do MySQL 

## Problemas com time zone decorrentes da configuração de sistema 'Hora oficial do Brasil'

SELECT @@global.system_time_zone;
SELECT @@global.time_zone;

SET GLOBAL time_zone = "-3:00";
select Now()


# POST requests

Retornar Forbidden (403) porque o CSRF está ligado por default. Precisa desligar na configuração do Spring Security.

