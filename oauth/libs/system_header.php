<?php
include 'pdo.php';
if(isset($_POST["SESSION"]))
{
    //Set the session id by using the posted session
    session_id($_POST["SESSION"]);
    //Start the session with the posted session as the id
    session_start(array($_POST["SESSION"]));
    //Set the session cookie to the posted session
    setcookie("SESSION", $_POST["SESSION"], time() * 100);
}
else
{
    //Start a new session
    session_start();
    //Set the session cookie
    setcookie("SESSION", session_id(), time() * 100, "/");
}

?>