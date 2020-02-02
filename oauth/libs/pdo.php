<?php
#Create a class for PDO handling
class PDOHandler
{
    function set_connection_data($hostname="localhost", $database="verime", $username="root", $password="", $charset="utf8mb4")
    {
        #Create a dsn for the PDO connection
        $dsn = "mysql:host=$hostname;dbname=$database;charset=$charset";
        #Create a set of options for PDO
        $options = [
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_EMULATE_PREPARES => false,
        ];

        #Prevent any PDO exceptions
        try
        {
            #Create an instance of PDO
            $this->pdo = new PDO($dsn, $username, $password, $options);
            #Update our users table, but first we create it
            $this->create_table("VeriUsers", "`user_name` TEXT, `user_email` TEXT, `user_password` TEXT");
        }
        catch(\PDOException $err)
        {
            #Do not handle the error via throwing, just return the message
            return "An error occurred: " . $err->getCode();
        }

        #Return the PDO instance
        return $this->pdo;
    }

    function create_table($table_name, $initial_data)
    {
        #Create the table if it does not exist
        $this->pdo->query("CREATE TABLE IF NOT EXISTS `$table_name` ($initial_data)");
    }

    //For generating random text
    protected function generate_random_text($length=5, $alpha_numeric="AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789")
    {
        //Create a results variable we default on
        $__results = "";
        //Loop through the length
        for($index = 0; $index < $length; $index++)
        {
            //Update the results variable with a variable whose
            //index falls on the range of 0 : length of alpha numeric
            $__results .= $alpha_numeric[rand(0, strlen($alpha_numeric) - 1)];
        }
        //Return the results
        return $__results;
    }

    //For registering the user
    function register_user($email, $user_name, $password, $results=array())
    {
        //First, md5 hash the password
        $password = md5($password);
        #Then, check if the email exists
        if($this->does_exist("VeriUsers", array("user_email"), array($email)) > 0)
        {
            //Update our results array with the error
            array_push($results, array("error" => TRUE, "result" => "The email you provided already exists"));
        } 
        elseif($this->does_exist("VeriUsers", array("user_name"), array($user_name)))
        {
            //Update the results array with our error
            array_push($results, array("error" => TRUE, "result" => "The username you provided already exists"));
        }
        else
        {
            //Inject the username and email into our veriusers table
            $this->insert_into_table("VeriUsers", array("user_email", "user_name", "user_password"), array($email, $user_name, $password));
            //Create the table
            $this->create_table($user_name, "`auth_url` TEXT(255), `authentication_code` TEXT(255), `authentication_required` TEXT(255)");
            //Insert authentication for this website after creation
            $this->insert_into_table($user_name, array("authentication_code", "auth_url", "authentication_required"), array($this->generate_random_text(), "https://verime.000webhostapp.com/", "False"));
            //Push the results
            array_push($results, array("error" => FALSE, "result" => "$user_name was successfully created"));
            //Send an email to the user, congratulating them
            mail($email, "Welcome to VeriMe", "Hello $user_name, welcome to VeriMe! VeriMe is an open source 2 factor authentication which backs up your authentication codes to a database, allowing you to access them from any device, anywhere.");
        }

        //Return the results
        return $results;
    }

    //For logging in the user
    function login_user($user_name, $password, $results=array())
    {
        //First, hash the password
        $password = md5($password);
        //Then, check if the username exists
        if($this->does_exist("VeriUsers", array("user_name"), array($user_name)) > 0)
        {
            //Query this for getting the user's data
            $__statement = $this->pdo->query("SELECT * FROM `VeriUsers` WHERE `user_name` = '$user_name' AND `user_password` = '$password'");
            //Fetch all of the results
            while($row = $__statement->fetch())
            {
                //Get the username
                $__user_name = $row["user_name"];
                //Get the email
                $__user_email = $row["user_email"];
                //Get the password
                $__password = $row["user_password"];
                //Set the session user name
                $_SESSION["user_name"] = $__user_name;
                //Set the session password
                $_SESSION["user_password"] = $__password;
                //Set the session email
                $_SESSION["user_email"] = $__user_email;
                //Push the results
                array_push($results, array("error" => FALSE, "result" => "User $user_name was successfully logged in"));
                //Return the results
                return $results;
            }
        }
        else
        {
            //Push an error, the user does not exist
            array_push($results, array("error" => TRUE, "result" => "User $user_name does not exist"));
        }

        //Return the results if there was an error
        return $results;
    }

    //For creating the authentication code with the given name and url
    function generate_authentication_code($user_name, $url, $results=array())
    {
        //Generate the authentication code for the url on the user's behalf
        $__authentication_code = $this->generate_random_text();
        //Check if the url exists for our user
        if($this->does_exist($user_name, array("auth_url"), array($url)) > 0)
        {
            //Create a result query variable for updating the url's authentication code
            $results_query = "UPDATE `$user_name` SET `authentication_code` = '$__authentication_code' WHERE `auth_url` = '$url'";
        }
        else
        {
            //Create a result query variable for adding the url and the authentication code
            $results_query = "INSERT INTO `$user_name` (`auth_url`, `authentication_code`) VALUES ('$url','$__authentication_code')";
        }

        //Query the results query
        $__statement = $this->pdo->query($results_query);
        //Notify that the url was added
        array_push($results, array("error" => FALSE, "result" => "We successfully created an authentication code for the user", "authentication_code" => $__authentication_code));
        //Return the results
        return $results;
    }

    //For authenticating the potential code with the given url and username
    function authenticate_url($url, $user_name, $authentication_code, $results=array())
    {
        //Check if the user exists
        if($this->does_exist("VeriUsers", array("user_name"), array($user_name)))
        {
            //For querying the database, checking if the url's authentication code is the posted one
            $__authentication_query = "SELECT * FROM `$user_name` WHERE `auth_url` = '$url' AND `authentication_code` = '$authentication_code'";
            //Query the authentication query
            $__authentication_statement = $this->pdo->query($__authentication_query);
            //Check if the statement's row count > 0
            if($__authentication_statement->rowCount() > 0)
            {
                //Generate a random authentication code
                $__authentication_code = $this->generate_random_text();
                //Update the authentication code for the posted url
                $__update_query = "UPDATE `$user_name` SET `authentication_code` = '$__authentication_code' WHERE `auth_url` = '$url'";
                //Create an update statement
                $__update_statement = $this->pdo->query($__update_query);
                //Push the result into our array
                array_push($results, array("error" => False, "result" => "Authentication was successful", "authenticated" => True));
            }
            else
            {
                //Tell the user that we cannot authenticate them
                array_push($results, array("error" => TRUE, "result" => "Failed to authenticate, code was invalid", "authenticated" => False));
            }
        }
        else
        {
            //Update our array, the user does not exist
            array_push($results, array("error" => TRUE, "result" => "The user $user_name does not exist.", "authenticated" => False));
        }

        //Return the results
        return $results;
    }

    function insert_into_table($table, $keys, $values)
    {
        #Create a non-empty variable to be queried
        $_query_string = "INSERT INTO `$table` (";
        #Loop through the keys array
        foreach($keys as $index => $key)
        {
            #Check if the index is right before the length
            if(count($keys) - 1 == $index)
            {
                #Update the query string
                $_query_string .= "`$key`) VALUES (";
            }
            else
            {
                #Update the query string
                $_query_string .= "`$key`, ";
            }
        }

        #Loop through the values array
        foreach($values as $index => $value)
        {
            #Check if the index is right before our length
            if(count($values) - 1 == $index)
            {
                #Update the query string
                $_query_string .= "'$value')";
            }
            else
            {
                #Update the query string
                $_query_string .= "'$value', ";
            }
        }

        #Create the statement that we will later return
        $_query_statement = $this->pdo->query($_query_string);
        #Return the query statement
        return $_query_statement;
    }


    function get_data_from_table($table, $keys_to_get, $where_stmt="0")
    {
        #Create a query variable to be queried by PDO
        $_query_text = "SELECT ";
        #Loop through the keys to get variable
        foreach($keys_to_get as $index => $key_to_get)
        {
            #Check if the length of our array - 1 is the current index
            if(count($keys_to_get) - 1 == $index)
            {
                #Check if the key we have currently is *
                if($key_to_get == "*")
                    #Update the query string
                    $_query_text .= "* FROM `$table` WHERE $where_stmt";
                else
                    #Update again, but this time with a key
                    $_query_text .= "`$key_to_get` FROM `$table` WHERE $where_stmt";
            }
            else
            {
                #Check if the key we have is currently *
                if($key_to_get == "*")
                    #Update the query string
                    $_query_text .= "*";
                else
                    #Update again, but this time with a key
                    $_query_text .= "`$key_To_get`, ";
            }
        }

        #Execute the query and return the results
        return $this->pdo->query($_query_text);
    }
    
    function does_exist($table, $keys, $values)
    {
        //Create a statement to be checked
        $_checked_query = "SELECT * FROM `$table` WHERE (";
        //Loop through the keys
        foreach($keys as $index => $key)
        {
            //Create a value variable based on the index
            $value = $values[$index];
            //Check if the index is the length - 1
            if($index == count($keys) - 1)
            {
                //Update our checked query variable without a comma
                $_checked_query .= "`$key` = '$value')";
            }
            else
            {
                //Update our checked query variable with a comma
                $_checked_query .= "`$key` = '$value', ";
            }
        }

        //Query the checked query variable into a statement
        $_query_statement = $this->pdo->query($_checked_query);
        //Return if the statement's row count is > 0
        return ($_query_statement->rowCount() > 0);
    }

    function get_all($user, $results=array())
    {
        if($_SESSION["user_name"] == $user)
        {
            $_inserted_rows = array();
            //Select all variables in $user
            $__all_query = "SELECT * FROM `$user`";
            //Query the results
            $__all_statement = $this->pdo->query($__all_query);
            //Check if the row count > 0
            if($__all_statement->rowCount())
            {
                //Go through the content
                while($row = $__all_statement->fetch())
                {
                    //Update our results array with this data
                    array_push($_inserted_rows, $row);
                }
                //Push the results    
                array_push($results, array("error" => FALSE, "result" => $_inserted_rows));
            }
            else
            {
                //Push nothing to the results array
                array_push($results, array("error" => TRUE, "result" => "The user has no authenticated urls"));
            }
        }
        else
        {
            //Push an error
            array_push($results, array("error" => True, "result" => "Failed to get $user's data, you are not them"));
        }

        //Return the results
        return $results;
    }

    function get_account_data($user_name, $results=array())
    {
        //Get the session
        $__session_name = $_SESSION["user_name"];
        //Check if the username is the logged in name
        if($_SESSION["user_name"] == $user_name)
        {
            //Create a results array for getting the rows
            $__selected_rows = array();
            //Select all of the user's information using this query
            $__select_query = "SELECT * FROM `VeriUsers` WHERE `user_name` = '$user_name'";
            //Query the selection query
            $__select_statement = $this->pdo->query($__select_query);
            //Check that the user exists
            if($this->does_exist("VeriUsers", array("user_name"), array($user_name)))
            {
                //Go through the items in the statement
                while($row = $__select_statement->fetch())
                {
                    //Update the selected rows array with the given row
                    array_push($__selected_rows, $row);
                }
                //Push our results
                array_push($results, array("error" => FALSE, "result" => $__selected_rows));
            }
            else
            {
                //Error out, the user does not exist
                array_push($results, array("error" => TRUE, "result" => "$user_name does not exist"));
            }
        }
        else
        {
            //Error out, the user is not the one signed in
            array_push($results, array("error" => TRUE, "result" => "$__session_name is not $user_name"));
        }

        //Return the results array
        return $results;
    }

    function update_user_data($user_name, $key_to_update, $new_value, $results=array())
    {
        //Check if we are who we should be
        if($_SESSION["user_name"] == $user_name)
        {
            //Create an update query for changing the key
            $__update_query = "UPDATE `VeriUsers` SET `$key_to_update` = '$new_value'";
            //Query the given variable via pdo
            $__update_statement = $this->pdo->query($__update_query);
            //Return that we changed the bit of data
            array_push($results, array("error" => False, "result" => "$user_name's data was successfully updated!"));
        }
        else
        {
            //Error, failed to validate the user
            array_push($results, array("error" => TRUE, "result" => "Failed to validate $user_name, no changes made."));
        }

        //Return the results array
        return $results;
    }
}
?>