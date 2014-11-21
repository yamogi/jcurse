jcurse
======

This application was created, because of the lack of Linux support of the official Curse(tm) client.

It only supports the game World of Warcraft. Other games are not in my focus, because I don't play them and can't therefore test or develop for it.

Requirements
============

* Only Linux is tested. Other operation systems (Mac, ...) might work too, but they are not tested and I might give no support for that.
	* Windows is prohibit to use, because of the already existing official client.
* a so called Java 8 runtime environment is needed to run this (e.g. openjdk-8-jre). I can't tell you how to install this, because it depends on your distribution. If you can call java -version and a java version like 1.8 is printed, you should be fine.
	* For ubuntu you can use http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html
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

**Add addon in pre stable status**
	
	jcurse add alpha <addon-name>...
	jcurse add beta <addon-name>... 
	
**Set to non release status**

	jcurse set [alpha|beta|release] [addon name, ...

**List all installed addons**

	jcurse list

**Export for later reinstall**

	jcurse export
	
**Force update, if already same version**
Maybe used after addon folder was deleted, but jcurse doesn't know about it
		
	jcurse --force update all
	jcurse --force update <addon-name>...
	jcurse -f update all


Switch WoW directory
---------------------

1. First save all installed addons using

	jcurse export

2. delete /home/user/.jcurse directory
3. Set new wow directory using

	jcurse --set-wow <path>

4. execute in 1 saved command to reinstall all addons again


Changelog
=========

1.0
-----

*New features*
* #11: Change specific addon to other than stable release.
* #17: Force update of addon

*Bugfixes/tasks*
* #28: Java 8 upgrade
* #32: Check and Update documentation
* #37: Fix build on private hudson
* #35: Invalid cookie header, if alpha/beta is used
* #23: Prohibit using client on Windows.
* #36: Make code coverage working again (private site)
* #30: Refactoring to separate classes.

* #19: Recognize addon version using some kind of id if possible
 

0.2
-----

* issue #18 Update peformance boost using parallel download
* issue #26 Using apache httpclient library for accessing curse website. Default Java framework is not reliabel regarding setting the "user agent".
* issue #24: Other logging for adding

0.1
-----
* initial stable release