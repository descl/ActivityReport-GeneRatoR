#ActivityReport-GeneRatoR

#### An activity report generator
Tool wich help research teams to extract datas from SI to LaTeX files in order to generate Activity reports.


## How to use the tool

###Configuration



1.  Clone code https://github.com/descl/ActivityReport-GeneRatoR
1.  Create your templates files in /templates folder (see samples)
1.  Edit the /report/report.tex file (this file will not be erased during generation)
1.  Copy /config/argrr.properties.sample to /config/argrr.properties and configure it
1.  If you use Inria reports add the xml report to /config folder (and edit the config file) (you can found the xml files on http://raweb.inria.fr/rapportsactivite/RA2012/)
1.  Add your google credentials:  https://cloud.google.com/console?redirected=true#/project/apps~lateral-boulder-400/apiui/credential copy " Client ID for native application" "Download JSON” to /config/client_secret.json
1.  Be sure that you have acces to your MySQL database (activate VPN if needed)
1.  You can run generation!


###Generation


1.  mvn -DskipTests install
2.  mvn exec:java -pl ARGRR-gDrive-downloader #in order to download the gdrive files
3.  mvn exec:java -pl ARGRR-latex-writer
4.  mvn exec:java -pl ARGRR-gDrive-uploader


##Authors
[Christophe Desclaux](http://www.desclaux.me) (I3S, CNRS), creation of the tool)

##License
[MIT License](http://en.wikipedia.org/wiki/MIT_License) (c) Christophe Desclaux
