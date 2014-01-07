
Use the tool
=============
How to configure the tool
-------------


1) Clone code https://github.com/descl/ActivityReport-GeneRatoR
2) Create your templates files in /templates folder (see samples)
3) Edit the /report/report.tex file (this file will not be erased during generation)
4) Copy /config/argrr.properties.sample to /config/argrr.properties and configure it
5) If you use Inria reports add the xml report to /config folder (and edit the config file) (you can found the xml files on http://raweb.inria.fr/rapportsactivite/RA2012/)
6) Add your google credentials:  https://cloud.google.com/console?redirected=true#/project/apps~lateral-boulder-400/apiui/credential copy " Client ID for native application" "Download JSON” to /config/client_secret.json
7) Be sure that you have acces to your MySQL database (activate VPN if needed)
8) You can run generation!


Generation
-------------

In order to run the project:
mvn -DskipTests install


mvn exec:java -pl ARGRR-gDrive-downloader #in order to download the gdrive files
mvn exec:java -pl ARGRR-latex-writer
mvn exec:java -pl ARGRR-gDrive-uploader
