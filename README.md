jcurse
======

This application was created, because of the lack of Linux support of the official Curse(tm) client.

It only supports the game World of Warcraft. Other games are not in my focus, because I don't play them and can't therefore test or develop for it.

Requirements
============

* Only Linux is tested. Other operation systems (Windows, Mac, ...) might work too, but they are not tested and I might give no support for that.
	* For Windows especially, because for that the official curse client exists. I even consider prohibit this client to stop working on windows.
* a so called Java 7 runtime environment is needed to run this (e.g. openjdk-7-jre). I can't tell you how to install this, because it depends on your distribution. If you can call java -version and a java version like 1.7 is printed, you should be fine.
* World of Warcraft installed in your Linux file system. Installing it in NTFS file system may result in a unexpected behavior of WoW and this client.
* The directory names for your addons are case-sensitiv and have to be "Interface/AddOns" in your WoW directory. Otherwise this client will not work!

How To
======

First time configuration
------------------------

1. Download the most current stable version from https://bitbucket.org/keiki/jcurse/downloads
2. Uncompress it to your favourite directory. To make this right this should either be /opt/ or in your user directory.
3. Set your WoW directory
	./jcurse --set-wow /home/user/.wine/drive_c/Program\ Files\ \(x86)/World\ of\ Warcraft/

Use it
------

Each addon has a short name on the curse site. So if you want to download Deadly Boss Mods the url of the addon is http://www.curse.com/addons/wow/deadly-boss-mods . So the name to use in this client is deadly-boss-mods .

**Adding addons**

	jcurse add <addon1-name> <addon2-name> <...>

**Removing addons**

	jcurse remove <addon1-name> <addon2-name> <...> 

**Update all addons**

	jcurse update all
	
**Update specific addons**

	jcurse update <addon1-name> <addon2-name> <...>

**List all installed addons**

	jcurse list

**Export for later reinstall**

	jcurse export




Changelog
=========

0.2
-----

* #18 Update peformance boost using parallel download
* #26 Using apache httpclient library for accessing curse website. Default Java framework is not reliabel regarding setting the "user agent".
* #24: Other logging for adding

0.1
-----
* initial stable release
