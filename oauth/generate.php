<?php
//Include the system header
include 'libs/system_header.php';
//Initiate the results variable
$__results = array();
//Create the handler
$__handler = new PDOHandler();
//Set the connection data
$__handler->set_connection_data();
//Check if the username or email and password was set
if(isset($_SESSION["user_name"]))
{
    //Get the username from the session
    $__user_name = $_SESSION["user_name"];
    //Get the url being posted
    $__url = $_POST["url"];
    //Pass the data to our handler
    $__results = $__handler->generate_authentication_code($__user_name, $__url);
}
else
{
    //Our error here is that the user is not logged in
    array_push($__results, array("error" => TRUE, "result" => "The user requesting generation of code is not signed in"));
}

//Dump out the array as json
echo json_encode($__results);
?>