<?php
//Include the application-wide header
include 'libs/system_header.php';
//Initiate the results variable
$__results = array();
//Create our handler
$__handler = new PDOHandler();
//Set the connection data
$__handler->set_connection_data();
//Check if the code being authenticated was posted
if(isset($_POST["authentication_code"]))
{
    //Get the posted authentication code
    $__authentication_code = $_POST["authentication_code"];
    //Get the username being authenticated
    $__user_name = $_POST["user_name"];
    //Get the url being authenticated
    $__url = $_POST["authenticated_url"];
    //Pass the data into the handler
    $__results = $__handler->authenticate_url($__url, $__user_name, $__authentication_code);
}

//Output the json
echo json_encode($__results);
?>