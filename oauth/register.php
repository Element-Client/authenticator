<?php
include 'libs/system_header.php';

$results = array();

//Initiate the PDO class
$_handler = new PDOHandler();
//Set the connection data
$_handler->set_connection_data();

if(isset($_POST["user_name"]))
{
    //Get the user name
    $user_name = $_POST["user_name"];
    //Get the email
    $email = $_POST["user_email"];
    //Get the password
    $password = $_POST["user_password"];
    //Register the user
    $results = $_handler->register_user($email, $user_name, $password);
}
else
{
    //Notify that nothing was passed to the site
    array_push($results, array("error" => TRUE, "result" => "No data was passed to the server"));
}

//Encode the results
echo json_encode($results);
?>