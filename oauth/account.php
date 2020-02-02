<?php
//Include the class-wide header
include 'libs/system_header.php';

//Create an empty results array to be spat out as json
$__results = array();

//Create a PDO Handler instance
$__handler = new PDOHandler();

//Set the handler's data
$__handler->set_connection_data();

//Check if the user wishes to get their account information, or set it
if(isset($_POST["get"]) and isset($_SESSION["user_name"]))
{
    //First, get their username
    $__user_name = $_SESSION["user_name"];
    //Push the username to the PDO Handler
    $__results = $__handler->get_account_data($__user_name);
}
elseif(isset($_POST["update"]) and isset($_SESSION["user_name"]))
{
    //Get the user's username
    $__user_name = $_SESSION["user_name"];
    //Get the data to be update
    $__key_to_update = $_POST["update"];
    //Get the updated value
    $__updated_value = $_POST["value_updated"];
    //Post the username, key, and value to the handler
    $__results = $__handler->update_user_data($__user_name, $__key_to_update, $__updated_value);
}
else
{
    //Error out, the user has not logged in
    array_push($__results, array("error" => TRUE, "result" => "You have not logged in yet"));
}

//Echo the results as json
echo json_encode($__results);
?>