---
title: Technicserver - ein Minecraft Server Wrapper
layout: default-de
---
![FIXME](https://img.shields.io/badge/-FIXME-yellow.svg?logo=data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8%2F9hAAABNklEQVQ4T6WTPUvDUBSG33MzODn4MYbcJC3N4CIu4i4o%2Fgknp06CbkUQdycn%2F4M4CIIgrrqJi4PUJo0Zi0O7iPaeIwk0ik1irXe7cJ%2FnfPBewgSn5rrbZMyuUWqJRG46cbwFwKQo%2Fcb7jntAhMP0HRvcf2C4niTJ64irFDQcxzekngBYKWAIXhRF0beiVqWgrnVTQCcjgBUth2H4kN6DIJgdvr2fVwpq2msBcvRVkfZZ4YKZ%2B0roTBHWqgWOtwOS07I9MdApFdS1XhHQNYC5MgFBmoWCCWAB0%2FHzS7g3JiiCWXigoFogWQRUn8RctuP4cSwHZbBFtNHudm%2BLRsk7mAbOO5gWzgT%2FgTOBp92eAhbytAkPqmb%2BuQfytb4j0Gr2Wf4IZx3Ytj0%2FY1lXzNKwFG2WbbssTJ%2FUx5KdaUkUvAAAAABJRU5ErkJggg%3D%3D&colorA=aaaaaa&colorB=f1c40f&style=flat-square)
# Technicserver
run modpacks from technicpack.net as server with ease

[![Travis](https://img.shields.io/travis/bennet0496/technicserver.svg?style=flat-square)](https://travis-ci.org/bennet0496/technicserver) [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg?style=flat-square)](https://www.gnu.org/licenses/gpl-3.0) [![Github Releases](https://img.shields.io/github/downloads/bennet0496/technicserver/latest/total.svg?style=flat-square)](https://github.com/bennet0496/technicserver/releases)


Dieses Projekt soll einen Minecraft Server Wrapper für Modpacks, welche auch technicpack.net gehosted sind, bieten. Das einzige was man dazu benötigt, ist oder Technicpack-API Link und der Technicserver kümmert sich um den Download und die Konvertierung selbst. Es werden sowohl Modpacks mit einzelner ZIP, als auch Modpacks mit der [Solder-API](http://solder.io) unterstützt.

Derzeit können nur Modpacks genutzt werden, welche den [Forge Modloader](https://files.minecraftforge.net/) nutzen. Das sollte aber bei 99,999% der Fall sein.

Die Nutzung von bei Gameserver Hostern ist nur dann möglich, wenn dieser eigene Minecraft JAR-Datein, eine eigene Start Komandozeile und 2 gleichezeitige Java Prozesse unterstützt. Der Wrapper benötigt zur Laufzeit ca. 200MB zusätzlichen Arbeitsspeicher.

Dieses Projekt ist weder ein offizielles "Technicpack"/"Technic Platform" (geschützt durch Syndicate LLC) noch ein offizielles Minecraft (geschützt durch Mojang AB) Project. Demzufolge ist es mit keiner der Firmen affliert noch hat eine besondere Erlaubniss.

## Schnelleinstieg

* Lade die aktuelle Version auf der Github Releases Seite https://github.com/bennet0496/technicserver/releases herunter

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
