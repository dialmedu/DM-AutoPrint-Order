# Instalación

Los archivos de configuración para el proyecto en JAVA, se encuentran en la carpeta /soruce/ el 
db.config_example renombrarlo o crear el db.config y agregar sus datos de acceso a base
de datos, para el receptor del Webhook se realiza sobre el mismo archivo order_new.php, 
ya que es un simple script que captura la orden y la guarda en base de datos.

# Reportes

La impresion de los tickets se realiza usando JASPERT REPORT, este es una libreria y herramienta que 
permite crear plantillas para impresion usando IReport Desing (requiere Java 7) y JaspertReport.jar el 
sdk para poder crear e imprimir desde java.

# Webhook

Para poder guardar los pedidos se requiere que se agrege archivo order_new.php en alguna ruta y esta
se le pase a Woocommerce > Settings > Advance > Webhook.

# Sql

Para no hacer mas complejo este codigo con migraciones, dejo aqui el sql de la tabla en la que se guardarán
datos del pedido recibidos por el webhook.

CREATE TABLE `pedidos` (
  `id` int(11) NOT NULL,
  `pedido_numero` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cliente_nombre` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cliente_email` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cliente_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cantidad` double(10,2) DEFAULT NULL,
  `json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `printed` tinyint(4) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;