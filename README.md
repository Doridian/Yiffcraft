[![Yiffcraft][Project Logo]][Website]
What is Yiffcraft?
-------------------
Yiffcraft is a Spoutcraft-based client to enhance functionality especially on the mc.doridian.de server.

Compiling
---------
Yiffcraft requires MCP (Minecraft Coder Pack) for decompiling, deobfuscating, recompiling, and reobfuscating Minecraft's source.
There are a lot of steps involved to compile Yiffcraft, so knowing how to use MCP is a good place to start.

* Download and extract the latest compatible version of Minecraft Coder Pack.  
* Copy the latest complete, unmodified Minecraft bin directory from your .minecraft directory.  
* Place the Minecraft bin directory under MCP's jars directory.  
* Checkout or copy the conf directory from the Spoutcraft GitHub repository to MCP's conf directory.  
* Run MCP's decompile script (.bat for Windows or .sh for Linux).  
* Checkout or copy the lib directory from the Spoutcraft GitHub repository to MCP's lib directory.  
* Copy the latest SpoutcraftAPI and SpoutCommons jar into the lib directory. 
* Checkout or copy the latest src directory from the Yiffcraft GitHub repostiory to MCP's src directory.  
* Run MCP's recompile and reobfuscate scripts (.bat for Windows or .sh for Linux).  
* When tasks are finished, the compiled Yiffcraft class files will be located in reobf.  
* You'll also need to class files from the jars located in the lib directory on the Yiffcraft GitHub repository to run the client.  

**NOTE:** You will need to copy the Minecraft resources folder to the MCP root in order to launch the game using MCP.  
**NOTE:** You will need to copy the res folder to the minecraft.jar along with the compile Spoutcraft source and contents of the SpoutcraftAPI.jar and SpoutCommons.jar for those custom resources to show.

[Project Logo]: https://github.com/Doridian/Yiffcraft-Launcher/raw/master/src/main/java/org/spoutcraft/launcher/spoutcraft.png
[License]: https://github.com/Doridian/Yiffcraft/blob/master/LICENSE.txt
[Website]: http://mc.doridian.de
[GitHub]: https://github.com/Doridian/Yiffcraft
[MCP]: http://mcp.ocean-labs.de/index.php/MCP_Releases
