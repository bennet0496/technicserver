---
title: User Documentation
---
# User Documentation
The technicserver has not much to configure. A fully populated `modpack.properties`
would basically look like
```
#Fri Aug 25 19:16:43 CEST 2017
url=http://api.technicpack.net/modpack/groundbreaking-modpack-42
autoupdate=yes
javaArgs=-server -d64 -Xmx4G -XX:+DisableExplicitGC -XX:+AggressiveOpts
build=latest
disableAnalytics=false
```
Where `url` is the Modpacks API Url, which will show up on the Modpacks page by
pressing `Install this Modpack`.
![getapiurl]({{ site.baseurl }}/assets/img/getapiurl.png){:class="img-responsive"}

The Parameter `autoupdate` specifies weather to automatically update the Modpack
or not. Possible values are `yes`,`true` and `1` for activating autoupdate and
`no`, `false`, `0` (or any other "invalid" value) for deactivating it.  
Use this Parameter with caution. It automatically updates the Pack, by deleting and/or
overriding old (Mod-) files **without further prompting**. But yout world save
will be preserved.  
**Besides this I always recommend taking backups on a regular basis, especially
with enabled autoupdate, because *update might break things*.**:bangbang:

With `javaArgs` you can specify the Parameters passed to the Java Virtual Machine
(JVM) when starting the server. Normally will only configure the amount of RAM you
would like to allocate to Minecraft here. But you can also specify your magical
server-optimizing JVM arguments spooking around the forums.  
You can set the maximum amount of RAM with `-Xmx`, followed by the size you want to
allocate (e.g. `-Xmx4G` _without a space in between_).  
For some more indepth information on RAM amount and Java Arguments, please read
[here]({{ site.baseurl }}/docs/java-and-ram).

Setting the `build` only really makes sense if the Modpack is using the so called
Solder-API, because this will literally force the technicserver downloading the specified
build (ignoring the autoupdate setting). Where `latest` is an alias for the Build marked as latest and `recommended`
is an alias for the Build marked as recommended.  
This only works properly with the Solder-API, because the concepts of builds and
differentiation between a latest and a recommended build only exists with and in
the Solder-API.  
With a non-Solder-Modpack this Setting will be simply ignored. You can identify
a Solder-Enabled Modpack by it's green "Solder-Enabled" Badge inside the
Client-Launcher or on the Modpacks Page blow the title by the words "using Technic Solder"

`disableAnalytics=yes` will disable reporting Software-usage statistics it my Piwik
Server (hosted at German 1and1 Server) on a all-or-nothing basis.  
The following statistics are collected (on startup) if you decide to keep it enabled:
  - technicserver Version
  - the Modpack url
  - the Modpack Name
  - the Modpack Version specified by `build`
  - weather autoupdate is enabled
  - Time taken to initialize the Modpack
  - Your 1-Byte Anonymized IP-Address

The value of this Setting defaults to `no`

## Starting the Server
Starting the Server is archived by the simple Commandline
```bash
java -jar technicserver.jar
```
Do **not** add your additional JVM arguments here :bangbang: especially not the
arguments for increasing the amount of allocated RAM, as it would be wasted, because
the JVM will allocate it but the technicserver won't use it.

### A word on shared hosting
You may be able to use the technicserver with Shared Gameserver Hosting Providers
like Nitrado, gamed!de, 4Netplayers, GPortal or others. But this heavily depends on
what they allow in thier environments.   
To use technicserver the following has to be fulfilled:
  - you need to be able to upload and use your own JAR-File
  - you need to be able to specify a custom startup Commandline (to omit the RAM arguments)
  - it must be possible to run multiple Processes, as the running server is a so called forking Process.
  - it must be possible to download Files via HTTP and HTTPS

And keep in mind that technicserver will add an overhead of about 200 Megs RAM which,
depending on how restricting your provider is, will not be usable by Minecraft.

The only thing left to say is, that I'm not to recommend a special provider fulfilling
all above, because I prefer to Host such Servers my self.  
But feel free to report Hosters you tested working, I would appreciate.

## Updating the Servers
To update the Server (if autoupdate is disabled), simply enable autoupdate for one
run or pass `update` as commandline Parameter like this
```
java -jar technicserver.jar update
```
But as already motioned, do your self a favor and backup at least your world-save
before updating.
