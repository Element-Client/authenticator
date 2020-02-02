<?php
//Include the system header
include 'libs/system_header.php';
//Initiate an empty results array
$__results = array();
//Initiate an empty PDO Handler
$__handler = new PDOHandler();
//Set the connection data
$__handler->set_connection_data();
//Check if the user is logged in
if(isset($_SESSION["user_name"]))
{
    //Get the user name
    $__user_name = $_SESSION["user_name"];
    //avoid there being no user
    try
    {
        //Push the username to the handle
        $__results = $__handler->get_all($__user_name);
    }catch(PDOException $err)
    {
        //Push an error, user does not exist
        array_push($__results, array("error" => TRUE, "reason" => "User $__user_name does not exist"));
    }
}
else
{
    array_push($__results, array("error" => True, "result" => "Not logged in"));
}

//Output the results
echo json_encode($__results, JSON_UNESCAPED_SLASHES);
?>