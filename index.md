# Technicserver
run modpacks from technicpack.net as server with ease

master Branch: [![Build Status](https://travis-ci.org/bennet0496/technicserver.svg?branch=master)](https://travis-ci.org/bennet0496/technicserver) [![codebeat badge](https://codebeat.co/badges/ea46b4f3-083a-4f2c-b65b-587b2436983e)](https://codebeat.co/projects/github-com-bennet0496-technicserver-master) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/eb1c94d78f1e41ca9076d47cf3c3157e)](https://www.codacy.com/app/bennet0496/technicserver?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=bennet0496/technicserver&amp;utm_campaign=Badge_Grade)

[![license](https://img.shields.io/github/license/bennet0496/technicserver.svg?style=flat-square)](https://github.com/bennet0496/technicserver/blob/master/LICENSE.txt) [![Github Releases](https://img.shields.io/github/downloads/bennet0496/technicserver/latest/total.svg?style=flat-square)](https://github.com/bennet0496/technicserver/releases) 


This Project aims to provide an simple wrapper for Minecraft Modpack Servers for Modpacks at technicpack.net. You only need to provide the Technicpack-API link and the technicserver will download the Modpack and convert it to a Minecraft Server. Both single ZIP-File Modpacks and Modpacks using the [Solder-API](http://solder.io/) (which in my opinion is the preferred Method of hosting Modpacks 😀) are supported

Currently you can **only** run Modpacks using the [Forge Modloader](https://files.minecraftforge.net/). 

You are only able to use this Software with shared Gameserver Hosters, if they support custom Minecraft JAR-File, Custom Java Commandlines and if they allow running two Java Processes simultaneously. The Wrapper adds an Runtime Overhead of about 200MB RAM.

Please keep in mind, this project is neighter an offical "Technicpack"/"Technic Platform" (copyrighted by Syndicate LLC) project nor it is an offical Minecraft (copyrighted by Mojang AB) project. Therefore this project is neither affiliated with these companies nor it has special permission.

## Quick start

* Download the current Release form the Github Releases Page https://github.com/bennet0496/technicserver/releases

* Place the JAR-File in the Folder you want to install and run your Server

* Create a File called `modpack.properties` (or start the jar. it will create a skeleton and exit with an error) with at least the following content

  ```properties
  url=http://api.technicpack.net/modpack/your-modpack
  build=latest
  ```

  Replace the `url=...` with the API Url to the desired Modpack. You'll find the API Url by clicking `Install This Modpack` on the Modpack Page

  ![getapiurl](/assets/img/getapiurl.png){:class="img-responsive"}

* Start the Server with `java -jar technicserver.jar`  

  *__Do not__ add your "optimizing" Java JVM Arguments and `nogui` Parameter here :bangbang: Use the `javaArgs` property inside the `modpack.properties`. `nogui` is used automatically* 
