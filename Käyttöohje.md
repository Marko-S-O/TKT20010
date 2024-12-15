# Toiminnot

Tässä dokumentissa kuvataan seuraavat toiminnot

1.  Ohjelman käyttöönotto ja ajaminen
2.  Reitinhakurajapintojen käyttö
3.  Käyttöliittymätoiminnot
4.  Suorituskykyvertailun suorittaminen
5.  JUnit-testien ajaminen

# Ohjelman käyttööntto ja ajaminen

**Ohjelman asentaminen**

Ohjelman saa käyttöön lataamalla GitHubista jar-tiedoston [pathfinder.jar](https://github.com/Marko-S-O/TKT20010/blob/main/pathfinder-app/pathfinder.jar) haluamaansa paikalliseen hakemistoon.

**Karttojen lataaminen**

Käytetyt kaupunkikartat ja niihin liittyvät skenaariotiedostot saa ladattua Moving AI Labin sivuilta osoitteesta <https://movingai.com/benchmarks/mapf/> .

**Hakemistojen konfigurointi**

Käytetyt hakemistot voi konfiguroida projektin ajohakemistossa olevaan tiedostoon config.properties. Tämä sisältää kaksi hakemistonimeä:

-   MAP_DIRECTORY: karttojen hakemisto, jota käyttävät sekä käyttöliittymä että suorituskykytestaus
-   SCENARIO_DIRECTORY: kuormitustestauksen skenaariotiedoston hakemisto, käytetään sekä suorituskykytestauksessa että käyttöliittymässä

Esimerkkitiedosto [config.properties](https://github.com/Marko-S-O/TKT20010/blob/main/pathfinder-app/config.properties) löytyy GitHubista.

**Ohjelman ajaminen**

Ohjelman ajaminen edellyttää Java-versiota 23 tai uudempaa.

-   Käyttöliittymän saa ajettua komennolla *java -cp pathfinder.jar com.orasaari.PathfinderUI*
-   Suorituskykytestauksen saa ajettua komenolla *java* *-cp pathfinder.jar com.orasaari.PerformanceEvaluator*

# Reitinhakurajapintojen käyttö

Reitinhakualgoritmeja on mahdollista käyttää myös muista ohjelmista. Reitinhakualgoritmien luokat ovat

-   *com.orasaari.DijkstraPathfinder*
-   *com.orasaari.AStarPathfinder*
-   *com.orasaari.JPSPathfinder*

Reitinhaku kaikilla algoritmeilla tapahtuu yhdenmukaisella rajapinnalla, joka on määritelty yliluokassa Pathfinder:

-   *findPath(GridMap map, int startX , int startY, int goalX, int goalY)*

Rajapinnan parametrin map kartan pitää olla valmiiksi ladattu, mikä tapahtuu luomalla kartta konstruktorilla

-   *GridMap(String filename)*

# Käyttöliittymätoiminnot

Käyttöliittymä käynnistetään suorittamalla luokka PathfinderUI (ks. luku *Ohjelman käyttöönotto ja ajaminen*). Käyttöliittymällä voidaan

-   Suorittaa reitinhaku valitulla kartalla, alku- ja loppupisteillä sekä algoritmeilla ja visualisoida tulokset ja reitit
-   Ajaa algoritmien suorituskykyvertailu valitulla skenaariotiedostolla, algoritmeilla ja iteraatiomäärällä sekä tallentaa tulokset csv-tiedostoihin

![UI image](https://github.com/Marko-S-O/TKT20010/blob/main/ui.jpg)

Reitinhaku tapahtuu seuraavasti:

1.  Klikkaa *Select Map File* valitaksesi karttatiedoston levyltä. Tiedoston tulee olla Moving AI Labin .map -formaatissa.
2.  Valittuasi karttatiedoston klikkaa painiketta *Load Map*. Käyttöliittymä visualisoi kartan.
3.  Kirjoita reitinhaun alku- ja maalipisteiden koordinaatit kenttiin *Start X, Y* ja *Finish X, Y*.
4.  Valitse käytettävät algoritmit 1-3 kpl valintalaatikoista *Dijkstra*, *A-Star* ja *JPS*.
5.  Klikkaa painiketta *Find Path*. Käyttöliittymä käynnistää reitinhaun ja visualisoi eri algoritmien reitit eri väreillä. JPS:n hypyt visualisoidaan myös omalla värillään.

Huomioitavaa: käyttöliittymä on tarkoitettu pelkästään toteutuksen apuvälineeksi ja siinä on rajoitetut tarkistukset virhesyötteille.

## Suorituskykyvertailun suorittaminen käyttöliittymästä

Tehokkuusvertailu voidaan ajaa myös käyttöliittymällä. Tämä tapahtuu seuraavasti:

1.  Tallenna haluamasi skenaariot skenaariotiedostoon Moving AI Labin skenaarioformaatissa.
2.  Klikkaa painiketta *Select Scenario File* valitaksesi kohdassa 1 luotu skenaariotiedoston levyltä.
3.  Kirjoita haluttu iteraatiomäärä per skenaario ja algoritmi kenttään *Iterations.*
4.  Klikkaa painiketta *Run Performance Evaluation* tehokkuusvertailun suorittamiseksi. Riippuen skenaarioista ja iteraatioiden määrästä tämä voi kestää pidempäänkin.
5.  Kun evaluaatio on ajettu, ohjelma avaa dialogin, jossa näytetään tulosten yhteenveto.
6.  Dialogin painikkeella *Save to CSV File* tulokset on mahdollista tallentaa CVS-tiedostoihin. Tiedostot luodaan hakemistoon, josta ohjelma on käynnistetty. Tiedostonimet ovat:
-   *evaluation_summary.csv*: yhteenvetotiedot
-   *evaluation_details.csv*: tarkat tiedot jokaisesta evaluaatiota varten suoritetusta reitinhausta

# Suorituskykyvertailun suorittaminen

Suorituskykyvertailu on mahdollista suorittaa kolmella tavalla:

-   Käyttöliittymästä (käsitelty kohdassa "Käyttöliittymätoiminnot")
-   Komentoriviltä ajamalla luokka PerformanceEvaluator
-   JUnit-testinä ajamalla testiluokka PerformanceEvaluatorTest

## Ajaminen komentoriviltä

Ajaminen komentoriviltä tapahtuu seuraavasti:

1.  Ks. hakemistojen konfigurointi kohdassa *Ohjelman käyttöönotto ja ajaminen.*
2.  Editoi käytettävät skenaariot *performance-evaluation.scen* esim. tekstieditorilla. Tiedostojen tulee olla Moving AI Labin skneaariotiedostojen formaatissa (\*.map.scen). Esimerkkitiedosto löytyy GitHubista: <https://github.com/Marko-S-O/TKT20010/blob/main/pathfinder-app/data/test/performance-evaluation.scen>
3.  Jos et halua ajaa kaikkia algoritmeja tai oletusmäärää iteraatioita, editoi luokan *PerformanceEvaluator* main-metodiin käytettävät algoritmit ja iteraatioiden määrä.
4.  Testien tulokset tallentuvat automaattisesti tiedostoihin *evaluation_summary.csv* ja *evaluation_details.csv*, jotka tehdään oletushakemistoon.

## Ajaminen JUnit-testinä

Ajaminen tapahtuu seuraavasti

1.  Editoi käytettävät skenaariot tiedostoon performance-evaluation.scen. Tiedostojen tulee olla Moving AI Labin skneaariotiedostojen formaatissa (\*.map.scen) ja hakemistossa, johon vakio MapUtils.SCENARIO_DIRECTORY osoittaa.
2.  Suorita ajo Maven-komennolla mvn test -Dtest=PerformanceEvaluatorTest.
3.  Testien tulokset tallentuvat automaattisesti tiedostoihin evaluation_summary.csv ja evaluation_details.csv, jotka tehdään maven-projektin juurihakemistoon.

# JUnit-testien ajaminen

Kaikki JUnit-testit suoritetaan Maven-komennolla *mvn validate*.

Yksittäisen testin saa suoritettua komennolla *mvn test -Dtest=[luokan nimi]*, esim. *mvn -Dtest=JPSPathfinderTest*
