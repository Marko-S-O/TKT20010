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

Aikavaativuus O(n) (log V) - suorituskykytestauksen tulokset (?)

Tilavaativuus?

## A-Star (A*)

Aikavaativuus: O(n) - suorituskykytestauksen tulokset?

## Jump Point Search (JPS)

Aikavaativuus: Sama kuin A, mutta log pienempi?

# Suorituskykyvertailu: luokka PerformanceEvaluator

Algoritmien suorituskykyvertailu on totetettu sitä varten itse kirjoitetulla räätäliluokalla ja sen apuluokilla. Suorituskykyvertailu on kuvattu tarkemmin erillisessa testausdokumentissa. TODO: linkki

# Toiminnalliset testit: JUnit

Testit on toteutettu JUnit-kirjastolla. Testaus on kuvattu tarkemmin erillisessä testausdokumentissa TODO: linkki

# Käyttöliittymä

Käyttöliittymä on toteutettu Java Swing- ja AWD-kirjastolla ja kartan renderöinnin vaatima pikseligrafiikka Java2D-kirjastolla.

# Laajojen kielimallien (LLM) käyttö:

Laajoja kielimalleja on käytetty seuraaviin tarkoituksiin:
- Toteutusvälineiden valinta: pyydetty vertailutietoa tällä hetkellä käytössä olevista Java-alueen toteutusvälineistä kuten Java- ja JDK-versiot, grafiikkakirjastot, testauskirjastot, projektin koostaminen, VSCode pluginit, UML-työkalu 
- Ympäristön konfiguroinnin apu: VSCode Java-kehitysympäristön luominen, Maven
- Tiedonhaku: haettu viitteitä tieteellisiin artikkeleihin ja niitä selittäviin sivustoihin
- Selittäminen: lähdemateriaalin vaikeampien käsitteiden ja konseptien selittäminen luonnollisella kielellä
