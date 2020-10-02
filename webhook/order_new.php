<?php
date_default_timezone_set("America/Bogota");
$mysqli = new mysqli("HOST", "USER", "PASS", "DB");
if ($mysqli->connect_errno) {
    header($_SERVER["SERVER_PROTOCOL"]." 404 Not Found", true, 404);
    die();
}
try {
$JSON = file_get_contents('php://input');
$data = json_decode($JSON);
// pedido
$pedido_numero = $data->id;
// cliente
$cliente_id = $data->customer_id;
$cliente_nombre = "";
$cliente_email = "";
if ($cliente_id != 0) {
    $cliente_nombre =  $data->billing->first_name + ' ' + $data->billing->last_name;   
    $cliente_email =  $data->billing->email;   
}
// productos
$productos = $data->line_items;
$cantidad = 0;
for ($i = 0; $i < count($productos); $i++) {
   $producto = $productos[$i];
   $cantidad += $producto->quantity;
}
// insert
$misql = "INSERT INTO `pedidos`(`pedido_numero`, `cliente_nombre`, `cliente_email`, `cliente_id`, `cantidad`, `json`) VALUES ('$pedido_numero','$cliente_nombre','$cliente_email','$cliente_id',$cantidad,'$JSON')";

if ($mysqli->query($misql) === TRUE) {
    header($_SERVER["SERVER_PROTOCOL"]." 404 New record created successfully", true, 202);
} else {
    header($_SERVER["SERVER_PROTOCOL"]." 404 Not Found", true, 404);
}
/* close connection */
$mysqli->close();
}
catch (Exception $e) {
    header($_SERVER["SERVER_PROTOCOL"]." 404 Not Found", true, 404);
    die();
}



