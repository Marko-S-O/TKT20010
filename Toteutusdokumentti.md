# Ohjelman yleiskuvaus

Ohjelma muodostuu kolmesta pääosasta:
1.  Reitinhakualgoritmit ja siihen liittyvä karttaluokka ja apuluokat
2.  Tehokkuusvertailun suorittava luokka ja sen apuluokat
3.  Käyttöliittymäluokat

Tärkeimmät luokat ja yhteydet on esitetty seuraavassa kaaviossa.
![Class structure](https://github.com/Marko-S-O/TKT20010/blob/main/pathfinding-app.jpg)

Seuraavassa käsitellään ylemmällä tasolla ohjelman suunnitteluratkaisuja. Luokkien tarkat dokumentaatiot löytyvät JavaDoc-tiedostoista GitHub-hakemistosta [javadoc](https://github.com/Marko-S-O/TKT20010/tree/main/javadoc).

# Kartta: luokka GridMap

Karttoina on käytetty pelkästään Moving AI Labin testikarttoja (.map), niihin liittyviä testiskenaarioita (.map.scen) sekä edellisten kanssa yhdenmukaisessa formaatissa olevia yksinkertaisia testikarttoja. 

Toteutuksessa on pyritty tehokkuuteen maksimoimalla primitiivityyppien ja taulukoiden käyttö. Kartan sisäisenä esitystapana käytetään kaksiulotteista boolean-taulukkoa. Jokaiselle alkiolle luodaan kartan latauksen yhteydessä 8-alkioinen kuljettavuustaulukko, joka kertoo kunkin suunnan osalta, voiko siihen siirtyä. Tämä kuvataan kolmiulotteisena boolean taulukkona [x][y][direction], jossa x ja y ovat pikselin koordinaatit ja direction integer-koodi siitymissuunnalle. 

Kartoissa sovelletaan tiukkoja kääntymissääntöjä: diagonaalisessa liikkeessä pitää molempien vierekkäisten (horisontaalinen, vertikaalinen) solmujen olla vapaita, että liike on sallittu. Tämä ratkaisu muuttui kesken projekti, jotta Moving AI Labin testiskenaarioiden hyödyntäminen olisi mahdollista.

# Reitinhakualgoritmit: luokat Pathfinder, DijkstraPathfinder, AStarPathfinder ja JPSPathfinder

Myös reitinhakualgoritmeissa on pyritty maksimoimaan primitiivityyppien, taulukoiden ja valmiiksi vakioitujen tietojen käyttö tehokkuuden saavuttamiseksi. 

## Dijkstra

TODO: aikavaativuuden analyysi 

## A-Star (A*)

TODO: aikavaativuuden analyysi 

## Jump Point Search (JPS)

TODO: aikavaativuuden analyysi ja vertaaminen A-Stariing

# Suorituskykyvertailu: luokka PerformanceEvaluator

Algoritmien suorituskykyvertailu on totetettu sitä varten itse kirjoitetulla räätäliluokalla ja sen apuluokilla. Suorituskykyvertailu on kuvattu tarkemmin erillisessa [Testiraportissa](https://github.com/Marko-S-O/TKT20010/blob/main/Testiraportti.md).

# Toiminnalliset testit: JUnit

Testit on toteutettu JUnit-kirjastolla. Testaus on kuvattu tarkemmin erillisessä [Testiraportissa](https://github.com/Marko-S-O/TKT20010/blob/main/Testiraportti.md)

# Käyttöliittymä

Käyttöliittymä on toteutettu Java Swing- ja AWD-kirjastolla ja kartan renderöinnin vaatima pikseligrafiikka Java2D-kirjastolla. Käyttöliittymä on pelkästään toteutusaikainen työkalu ja lopputuotokset tuotetaan ajamalla ohjelmia suoraan komentoriviltä.

# Laajojen kielimallien (LLM) käyttö:

Laajoja kielimalleja on käytetty seuraaviin tarkoituksiin:
-   Toteutusvälineiden valinta: pyydetty vertailutietoa tällä hetkellä käytössä olevista Java-alueen toteutusvälineistä kuten Java- ja JDK-versiot, grafiikkakirjastot, testauskirjastot, projektin koostaminen, VSCode pluginit, UML-työkalu 
-   Toteutusympäristön konfiguroinnin apu: VSCode Java-kehitysympäristön luominen, Maven
-   Tiedonhaku: haettu viitteitä tieteellisiin artikkeleihin ja niitä selittäviin sivustoihin
-   Selittäminen: lähdemateriaalin vaikeampien käsitteiden ja konseptien selittäminen luonnollisella kielellä
