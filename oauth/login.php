<?php
//Include the system header
include 'libs/system_header.php';
//Initiate our results
$__results = array();
//Initiate the pdo handler
$__handler = new PDOHandler();
//Set the connection data
$__handler->set_connection_data();
//Check if the user_name post was set
if(isset($_POST["user_name"]) and isset($_POST["user_password"]))
{
    //Get the user name
    $__user_name = $_POST["user_name"];
    //Get the user password
    $__password = $_POST["user_password"];
    //Login to the server
    $__results = $__handler->login_user($__user_name, $__password);
}
else
{
    array_push($__results, array("error" => True, "result" => "Failed to login, no data was passed"));
}
//Output the json data
echo json_encode($__results);
?>