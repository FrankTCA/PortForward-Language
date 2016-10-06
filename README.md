# PortForward Language

The portforward language is used to efficently run server side scripting with java. It also includes the ability to run javascript functions from the server or vise-versa. It includes a large library of things to do, but you can also use just the java library to run from the scripts. You can do a ton of stuff. As it uses java, you can have full control over the server computer, however is also from a safe standpoint. For the protection of the clients, you cannot run java on their computers, however you are able to run a javascript function using an AJAX library and a handler script that comes with the portforward server. PortForward is completely open source, as you see from the GitHub project, so feel free to make any changes you wish to the source as long as it follows the liscense.

# PortForward Database

The portforward database is a database which can be used by your scripts, and also some files are used by the server (aka. the banned users file). The database is written solely in JSON, instead of SQL, as JSON is more secure than SQL, and is able to run much faster, as it only requires one connection to a server.

# PortForward Configurations

All configurations for the PortForward server / api is written in JSON and is located in the database. There is multiple files for each configuration, and you can add configurations through scripts. Configurations also give the ability to distribute your scripts to the general public. You can also sell your scripts if you wish to, however they must comply with the liscense if they are distributed.

# PortForward API

The API is included with the server jar file. The API is written in Java, for Java. When a script is specified to run according to the configuration in the .pfar file, the server will run that script. The scripts have full control over the server computer. However, what the scripts can do to the client is limited to JavaScript, so if you do want to do anything that could possibly be risky, you don't have to worry about losing your users, because it is your server.

# PortForward JavaScript API

PortForward includes a JavaScript API for clients. The API is mainly used for the server to communicate with clients and includes the ability to run JavaScript functions from a server-side script. It mainly uses AJAX to communicate with the server. There is also many other useful functions included with the API.
