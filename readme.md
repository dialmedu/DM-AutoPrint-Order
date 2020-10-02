# Instalación

## Instalación Base de Datos.

Crea una base de datos o usar una existente y agregar la siguiente sentencia SQL, esto
creará una tabla llamadas pedidos.

```sql
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
```

## Instalación WEBHOOK

Agregar en servidor el contenido de la carpeta `webhook\`, abirir el archivo `new_orden.php` y
agregar sus datos de conexion a base de datos en la linea.

`$mysqli = new mysqli("HOST", "USER", "PASS", "DB");`

Crear un subdominio que apunte hacia esa carpeta, ejemplo: `webhook.worpdress.org` agregar en la 
configuracion de woocommerce en `Woocommerce` > `Settings` > `Advance` > `Webhook` creand un webhook
que se active el crear nueva orden y la ruta hacia el archivo, ejemplo: `webhook.worpdress.com\new_orden.php`.

## Instalacion .JAR

Los archivos de configuración para el proyecto en JAVA, se encuentran en la carpeta `/source/` el 
archivo `db.config_example` renombrarlo a `db.config` y agregar sus datos de acceso a base
de datos. Luego ejecutar podra ejecutar `WOOAP.JAR`.

La carpeta para el .jar deberá tener la siguiente estructura.

```
wooap\
... source/
    ... db.config
    ... PedidoReporte.jaspert
... WOOAP.jar

```

# Desarollo

## Reportes

La impresion de los tickets se realiza usando JASPERT REPORT, este es una libreria y herramienta que 
permite crear plantillas para impresion usando IReport Desing (requiere Java 7) y JaspertReport.jar el 
sdk para poder crear e imprimir desde java.

## Webhook

Para poder guardar los pedidos se requiere que se agrege archivo order_new.php en alguna ruta y esta
se le pase a `Woocommerce` > `Settings` > `Advance` > `Webhook`. Woocommerce genera un token que es enviado
en la peticion pero actualmente ese componente de seguridad no se aplica.


## Herramientas de Desarollo

Si deseas colaborar descarga.

Netbeans 12 : https://netbeans.apache.org/download/index.html
Ireport 5.6: https://sourceforge.net/projects/ireport/files/iReport/iReport-5.6.0/iReport-5.6.0.zip/download
Ireporte requiere Java 7: https://www.oracle.com/java/technologies/javase/javase7-archive-downloads.html

Es un proyecto con MAVEN asi que Neteans pedira que descargue las dependencias es un proceso automatico por el IDE;
la configuración de Irepor es necesario editar un archivo `iReport-5.6.0\etc\ireport.conf` agregar la ruta de
instalación de Java 7 en Windows se veria así `jdkhome="C:\Program Files\Java\jdk1.7.0_80"`, ejecutar usando 
`iReport-5.6.0\bin\ireport.exe`.


## Metodo de Impresion.

El envio de los datos para impresión de realiza sobre la clases `Controller` en el paquete `io.github.dialmedu.wooap.autoprint`
el metodos resiva el resultado de la consulta y agregar los parametros que solicite el reporte.

```
public static void printPedido(String reporteFile, ResultSet pedido) throws SQLException{
    boolean ver = false;
    boolean imprimir = true;
    Map parametros =  new HashMap();
    parametros.put("pedido_numero",  pedido.getString("pedido_numero"));
    parametros.put("cantidad", pedido.getString("cantidad"));
    parametros.put("cliente_nombre", pedido.getString("cliente_nombre"));
    ManagerReportJaspert.createReport(reporteFile,parametros,"PedidoReporte",ver,imprimir);
}
```

Si crea otro reporte con los mismos parametros puede usar el mismo metodo y pasar el
nombre del reporte en para argumento `String reportFile`, si crea otro reporte con distintos
parametros crear otro metodo con esta estructura.