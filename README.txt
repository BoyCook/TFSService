=== Text File Synchroniser (service):
A synchroniser for text based files that reside in multiple (VCS) repositories, available over HTTP(S).

=== (Very simple) Architecture

						------------
						-TFS Client-
						------------
		      groupId  /            \
		   artefactId /              \ url
		  version    /                \
			   		/                  \
		-------------         ---------------------------
		-TFS Service-         -Files available over http-
		-------------         ---------------------------

=== Overview

There are two parts to TFS. A repository (REST service) that contains a list of files and their locations, and a (ruby script) client that allows users to export, update (etc) files.

Files are uniquely identified with the maven style groupId/artefactId/version concept. When a file is identified, it can be downloaded via the URL stored against it. An example of file XML is:

<file id="23">
	<id>23</id>
	<groupId>org.cccs.jslibs</groupId>
	<artefactId>jsMap</artefactId>
	<version>1.0.0.0</version>
	<extension>js</extension>
	<url>
		https://github.com/BoyCook/JSLibs/raw/master/jquery.collapsible/lib/jquery.collapsible.js
	</url>
</file>

When a file is exported TFS stores some meta-data on the filesystem in a '.tfs' directory (like SVN and Git do). This allows users to do things like upgrade versions and remove files.
