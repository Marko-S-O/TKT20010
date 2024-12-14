# Ohjelman yleiskuvaus

Ohjelma muodostuu kolmesta pääosasta:

1.  Reitinhakualgoritmit ja siihen liittyvä karttaluokka ja apuluokat
2.  Tehokkuusvertailun suorittava luokka ja sen apuluokat
3.  Käyttöliittymäluokat

Tärkeimmät luokat ja yhteydet on esitetty seuraavassa kaaviossa. ![Class structure](https://github.com/Marko-S-O/TKT20010/blob/main/pathfinding-app.jpg)

Seuraavassa käsitellään ylemmällä tasolla ohjelman rakennetta ja ratkaisuja. Luokkien tarkat dokumentaatiot löytyvät JavaDoc-tiedostoista projektin GitHub-hakemistosta [javadoc](https://github.com/Marko-S-O/TKT20010/tree/main/javadoc).

# Kartta: luokka GridMap

Karttoina on käytetty pelkästään Moving AI Labin testikarttoja (.map), niihin liittyviä testiskenaarioita (.map.scen) sekä edellisten kanssa yhdenmukaisessa formaatissa olevia yksinkertaisia testikarttoja.

Toteutuksessa on pyritty tehokkuuteen maksimoimalla primitiivityyppien ja taulukoiden käyttö. Kartan sisäisenä esitystapana käytetään kaksiulotteista boolean-taulukkoa. Jokaiselle alkiolle luodaan kartan latauksen yhteydessä kahdeksanalkioinen siirtymätaulukko, joka kertoo kunkin suunnan osalta, voiko siihen siirtyä. Tämä kuvataan kolmiulotteisena boolean taulukkona [x][y][direction], jossa koordinaatit ja direction vakioitu integer-koodi siirtymissuunnalle.

Kartoissa sovelletaan tiukkoja kääntymissääntöjä: diagonaalisessa liikkeessä pitää molempien vierekkäisten (horisontaalinen ja vertikaalinen) solmujen olla vapaita, että liike on sallittu. Tämä ratkaisu vaihtui kesken projekti, jotta Moving AI Labin testiskenaarioiden hyödyntäminen olisi mahdollista.

# Reitinhakualgoritmit: luokat Pathfinder, DijkstraPathfinder, AStarPathfinder ja JPSPathfinder

## Dijkstra

Dijkstra on toteutettu [Tirakirjan](https://raw.githubusercontent.com/hy-tira/tirakirja/master/tirakirja.pdf) mukaisella tavalla, joten pahimman tapauksen aikavaativuus on O(n + m log n), missä n on solmujen määrä ja m kaarten määrä.

Käytännössä suorituskykytestauksessa 1024\*1024-kokoisisilla kartoilla käsiteltiin keskimäärin n. 785 000 solmua.

## A-Star (A\*)

Pahimman tapauksen teoreettinen aikavaativuus A\*:lla on sama kuin Dijkstralla eli O(n + m log n).

Käytännön testauksessa saavutettiin kuitenkin selkeä ero Dijkstraan: suorituskykytestauksessa käsiteltyjen (prioriteettijonosta käsittelyyn otettujen käsittelemättömien) solmujen määrä oli keskimäärin n. 31% Dijkstran solmujen määrästä ja suoritusaika n. 47% Dijkstrasta.

## Jump Point Search (JPS)

JPS:n teoreettinen pahimman tapauksen aikavaativuus sille sopimattomalla kartalla (esim. monimutkainen sokkelo) on sama kuin Dijkstran ja A\*:n.

Käytännössä kaupunkikartoilla kuitenkin saavutettiin merkittävästi nopeampia suoritusaikoja: keskimäärinen ratkaisuaika suorituskykytestauksessa oli 15% Dijkstrasta ja 32% A\*:sta.

# Suorituskykyvertailu: luokka PerformanceEvaluator

Algoritmien suorituskykyvertailu on toteutettu sitä varten itse kirjoitetulla räätäliluokalla ja sen apuluokilla. Suorituskykyvertailu on kuvattu tarkemmin erillisessä [Testiraportissa](https://github.com/Marko-S-O/TKT20010/blob/main/Testiraportti.md).

# Toiminnalliset testit: JUnit

Testit on toteutettu JUnit-kirjastolla. Testaus on kuvattu tarkemmin erillisessä [Testiraportissa](https://github.com/Marko-S-O/TKT20010/blob/main/Testiraportti.md)

# Käyttöliittymä

Käyttöliittymä on toteutettu Java Swing- ja AWD-kirjastolla ja kartan renderöinnin vaatima pikseligrafiikka Java2D-kirjastolla. Käyttöliittymä on pelkästään toteutusaikainen työkalu ja lopputuotokset tuotetaan ajamalla ohjelmia suoraan komentoriviltä. Tämän vuoksi käyttöliittymään on toteutettu virheenkäsittelyä ainoastaan rajallisesti.

# Laajojen kielimallien (LLM) käyttö:

Laajoja kielimalleja on käytetty seuraaviin tarkoituksiin:

-   Toteutusvälineiden valinta: pyydetty vertailutietoa tällä hetkellä käytössä olevista Java-alueen toteutusvälineistä kuten Java- ja JDK-versiot, grafiikkakirjastot, testauskirjastot, projektin koostaminen, VSCode pluginit, UML-työkalu
-   Toteutusympäristön konfiguroinnin apu: VSCode Java-kehitysympäristön luominen, Mavenin hyödyntäminen.
-   Tiedonhaku: haettu viitteitä tieteellisiin artikkeleihin ja niitä selittäviin sivustoihin
-   Selittäminen: lähdemateriaalin vaikeampien käsitteiden ja konseptien selittäminen luonnollisella kielellä
