# Toiminnot

Tässä dokumentissa kuvataan seuraavat toiminnot

0.  Ohjelman käyttöönotto
1.  Reitinhakurajapintojen käyttö
2.  Käyttöliittymätoiminnot
3.  Suorituskykyvertailun suorittaminen
4.  JUnit-testien ajaminen

# Ohjelman asentaminen

Ohjelman voi ottaa käyttöön kahdella tavalla:
-   Asentamalla Maven-projektin omaan Java-ohjelmointiympäristöönsä, jossa on asennettuna tarpeelliset välineet Maven-projektin hallintaan ja Java-ohjelmien ajamiseen.
-   Lataamalla jar-tiedoston Githubista ja suorittamalla haluamansa luokan siitä. 
-   TODO: jar-tiedoston generointi, vieminen githubiin ja linkkaaminen tähän

## Maven-projektin käyttöönotto

TODO: Maven-projektin asennusohje

## JAR-tiedoston ajaminen

TODO: eri osien käynnistämiskomennot jar-tiedostosta

# Reitinhakurajapintojen käyttö

Kaikki reitinhaku kaikille toteutetuille algoritmeille ohjelmaa komentoriviltä, testeissä tai käyttöliittymältä käytettäessä noudattaa yhdenmukaista, abstraktissa yliluokassa Pathfinder määriteltyä rajapintaa: 
-   findPath(GridMap map, int startX , int startY, int goalX, int goalY);

Parametrin map sisältämän kartan pitää olla valmiiksi ladattu, mikä tapahtuu luomalla kartta konstruktorilla
-   GridMap(String filename)

# Käyttöliittymätoiminnot

Käyttöliittymä käynnistetään suorittamalla luokka PathfinderUI. Käyttöliittymällä voidaan
-   Suorittaa reitinhaku valitulla kartalla, alku- ja loppupisteillä sekä algoritmeilla ja visualisoida tulokset ja reitit
-   Ajaa algoritmien suorituskykyvertailu valitulla skenaariotiedostolla, algoritmeilla ja iteraatiomäärällä sekä tallentaa tulokset csv-tiedostoihin
TODO: linkitä kuva käyttöliittymäst

## Yksittäisen reitin haku

Reitinhaku tapahtuu seuraavasti:
1.  Klikkaa "Select Map File" valitaksesi karttatiedoston levyltä. Tiedoston tulee olla Moving AI Labin .map -formaatissa.
2.  Valittuasi karttatiedoston klikkaa Load Map. Käyttöliittymä visualisoi kartan.
3.  Kirjoita reitinhaun alku- ja maalipisteiden koordinaatit kenttiin Start X, Y ja Finish X, Y.
4.  Valitse käytettävät algoritmit 1-3 kpl valintalaatikoista "Dijkstra", "A-Star" ja "JPS".
5.  Klikkaa painiketta "Find Path". Käyttöliittymä käynnistää reitinhaun ja visualisoi eri algoritmien reitit eri väreillä. JPS:n hypyt visualisoidaan myös omalla värillään.

Huomioitavaa: käyttöliittymä on tarkoitettu pelkästään toteutuksen apuvälineeksi ja siinä on rajoitetut tarkistukset virhesyötteille.

## Suorituskykyvertailun suorittaminen käyttöliittymästä

Tehokkuusvertailu voidaan ajaa myös käyttöliittymällä. Tämä tapahtuu seur aavasti:
1.  Tallenna haluamasi skenaariot esim. tekstieditorilla skenaariotiedostoon Moving AI Labin skenaarioformaatissa.
2.  Klikkaa painiketta "Select Scenario File" valitaksesi skenaariotiedoston levyltä.
3.  Kirjoita haluttu iteraatiomäärä per skenaario ja algoritmi kenttään "Iterations".
4.  Klikkaa painiketta "Run Performance Evaluation" tehokkuusvertailun suorittamiseksi. Riippuen skenaarioista ja iteraatioiden määrästä tämä voi kestää pidempäänkin.
5.  Kun evaluaatio on ajettu, ohjelma avaa dialogin, jossa näytetään tulosten yhteenveto.
6.  Dialogin painikkeella "Save to CSV File" tulokset on mahdollista tallentaa CVS-tiedostoihin. Tiedostot luodaan hakemistoon, josta ohjelma on käynnistetty. Tiedostonimet ovat
-   evaluation_summary.csv: yhteenvetotiedot (samat kuin dialogissa)
-   evaluation_details.csv: tarkat tiedot jokaisesta evaluaatiota varten suoritetusta reitinhausta

# Suorituskykyvertailun suorittaminen

Suorituskykyvertailu on mahdollista suorittaa kolmella tavalla:
-   Käyttöliittymästä (käsitelty kohdassa "Käyttöliittymätoiminnot")
-   Komentoriviltä ajamalla luokka PerformanceEvaluator
-   JUnit-testinä ajamalla testiluokka PerformanceEvaluatorTest

## Ajaminen komentoriviltä

Ajaminen komentoriviltä tapahtuu seuraavasti:
1.  Editoi käytettävät skenaariot tiedostoon esim. tekstieditorilla. Tiedostojen tulee olla Moving AI Labin skneaariotiedostojen formaatissa (*.map.scen)
2.  Editoi luokan PerformanceEvaluator metodiin main käytettävät algoritmit, iteraatioiden määrä ja tiedostonimi.
3.  Testien tulokset tallentuvat automaattisesti tiedostoihin evaluation_summary.csv ja evaluation_details.csv, jotka tehdään oletushakemistoon.

## Ajaminen JUnit-testinä

Ajaminen tapahtuu seuraavasti
1.  Editoi käytettävät skenaariot tiedostoon performance-evaluation.scen esim. tekstieditorilla. Tiedostojen tulee olla Moving AI Labin skneaariotiedostojen formaatissa (*.map.scen) ja hakemistossa, johon vakio MapUtils.SCENARIO_DIRECTORY osoittaa.
2.  Suorita ajo Maven-komennolla mvn test -Dtest=PerformanceEvaluatorTest. 
3.  Testien tulokset tallentuvat automaattisesti tiedostoihin evaluation_summary.csv ja evaluation_details.csv, jotka tehdään maven-projektin juurihakemistoon.

# JUnit-testien ajaminen

Kaikki JUnit-testit suoritetaan Maven-komennolla mvn validate. 

Yksittäisen testin saa suoritettua komennolla mvn test -Dtest=[luokan nimi], esim. mvn -Dtest=JPSPathfinderTest 

