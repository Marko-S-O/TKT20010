# Johdanto

## Dokumentin tarkoitus

Tämä dokumentti on määrittelydokumentti kurssia TKT20010 Tekoäly ja algoritmit varten. Kyseessä syksyn 2024 2. implementaatio (28.10.-20.12.2024). Dokumentissa kuvataan toteutuksen tärkeimmät ominaisuudet, toiminnallisuudet ja toteutusvälineet kurssin vaatimusten mukaisessa laajuudessa.

Tämä on dokumentin toinen iteraatio, jossa on ohjaajan kanssa 1. kurssiviikolla käydyn keskustelun perusteella päivitetty toteutettavat algoritmit, kartan esitystapa sekä jälkimmäisen muutoksen myötä käyttöliittymän toteutukseen käytettävät välineet.

## Projektin yleiskuvaus

Projektin aihe on ”Verkot ja polunetsintä”. Projektissa toteutetaan algoritmit:

1.  A-star (A\*)
2.  Jump point search (JPS)

Algoritmeja sovelletaan 2D-ruudukoina kuvattaviin verkkoihin. Mikäli mahdollista, testaamisessa pyritään fokusoimaan reaalimaailman kartta-aineistojen tyyppiseen materiaaliin.

Toteutuksessa tuotetaan ensin käyttöliittymän perustoiminnallisuudet karttojen ja algoritmien suorittamisen hallintaan, sen jälkeen A\*-algoritmi riittävälle tasolle testausta varten ja lopuksi JPS. Mahdollisesti ennen varsinaisten algoritmien toteutusta toteutetaan asioiden omaksumisen tukemiseksi ja vertailukohdaksi Djikstran algoritmi.

## Opinto-ohjelma

Suoritan kurssia erillisten opintojen suoritusoikeudella. En kuulu mihinkään opinto-ohjelmaan.

# Ohjelmien määrittely

## Toteutettavat algoritmit

### A-star

Alkuperäinen A\* -algoritmin idea kuvattiin Hart et al. (1968) artikkelissa. A\* voidaan pitää Dijkstran edelleen kehitettynä versiona, jossa hyödynnetään heuristista funktiota. Tämän vuoksi lienee hyödyllistä toteuttaa alkuun yksinkertainen Dijkstran funktio.

A\*:n tehokkuus riippuu kriittisellä tavalla heuristisesta funktiosta. Jotta algoritmi toimisi järkevästi, heuristinen funktio ei koskaan saa yliarvioida jäljellä olevaa matkaa. Oletuksena on, että heuristisena funktiona käytetään lyhintä kulkuetäisyyttä 2D-ruudukossa (joka ei ole sama kuin euklidinen etäisyys). Paras heuristinen funktio riippuu kuitenkin kartasta ja voi olla erilainen labyrintille kuin kaupunkikartalle. Red Blog Gamesin sivulla (2015) on yksi mahdollisesti hyödynnettävissä oleva esitys heuristisista funktioista.

Pahimman tapauksen aikavaativuus A\*:lle on O(E+V logV), mikäli voidaan olettaa, että heuristinen funktio täyttää edellä mainitun ehdon.

Algoritmien ymmärtämiseen löytyy verkosta lukuisia esityksiä, mm. Red Blob Gamesin (2014-2023) ja Wikipedian selitykset.

### Jump point search

JPS alkuperäiskuvaus on Harabor & Gastrienin artikkelissa (2011). JPS:ää voidaan pitää A:n optimointina nimenomaan ruudukoissa tapahtuvaan reitinhakuun, joten oletus on, että tässä tapauksessa suurimmalla osalla kartoista JPS:n pitäisi toimia tehokkaammin kuin A\*.

JPS on myös riippuvainen tehokkaasta heuristisesta funktiosta. Pätevällä heuristisella funktiolla JPS on myös teoreettisesti tehokkaampi kuin A\*: sen pahimman tapauksen aikavaativuus on O(E+V).

Koska JPS on nimenomaan 2D-ruudukoihin optimoitu algoritmi, siitä löytyy lukuisia selittäviä sivustoja verkosta. Yksi tällainen on esimerkiksi Zerowidthin sivustolla oleva (2014).

## Kartta

Kartan kuvaamiseen on ainakin kaksi peruslähestymistapaa: kaksiulotteinen ruudukko, jossa ruutu on joko vapaa tai liikkumisen estävä, tai solmujen ja kaarten kuvaaminen niin, että kaareen liittyvä paino liitetään kaaren yhteyteen. Tämän työn kartta toteutetaan kaksiulotteisena ruudukkona kuvattuna karttana, jossa ruudukon solut ovat aina samankokoisia ja neliön muotoisia. Näinollen yksi ruutu muodosta aina solun, josta on mahdollinen kaari kahdeksaan suuntaan. Kaaren paino suorassa vertikaalisessa tai horisontaalisesta on yksi ja vinottain siirtyessä √2.

## Toiminnallisuudet

Alustavat toiminnallisuudet on listattu alla Epiceinä ja Featureina. Toiminnallisuudet on määritelty harjoitustyön toteuttamisen näkökulmasta sisältäen kaikki tuotettavat lopputuotokset, ei normaalista ohjelmiston toiminnallisen määrittelyn näkökulmasta.

-   **Epic 1. Validointi ja ohjeet**
    -   *Feature 1.1 Testimateriaalien hankkiminen*
        -   Tuotos: testeissä käytettävät kartat
    -   *Feature 1.2 Testisuunnitelman tuottaminen*
        -   Tuotos: testausdokumentin suunnitelmaosuus
    -   *Feature 1.3 Validoinnin toteutus ja dokumentointi*
        -   Tuotos: testausdokumentin testitulosten osuus
    -   *Feature 1.4 Käyttöohjeen tuottaminen*
        -   Tuotos: käyttöohje
-   **Epic 2. Käyttöliittymä**
    -   *Feature 2.1 Lataa kartta*
        -   Toiminnot: Valitsee tiedoston valintadialogin kautta, lataa kartan ja visualisoi sen. Mikäli kartta on suurempi kuin käytettävissä oleva tila näytöllä, käytetään skrollattavaa komponenttia.
        -   Tuotos: käyttöliittymä, joka pystyy lataamaan kartan tiedostosta ja visualisoimaan kartan
    -   *Feature 2.2 Laske reitti*
        -   Toiminnot: laskee reitin valitusta alkupisteestä valittuun loppupisteeseen valitulla kartalla ja algoritmilla
        -   Tuotos: käyttöliittymä, joka pystyy suorittamaan reitin laskemisen halutuilla parametreilla
    -   *Feature 2.3 Visualisoi reitti*
        -   Toiminnot: visualisoi sisäisessä esitysmuodossa olevan, algoritmin laskeman reitin kartalle käyttöliittymässä
        -   Tuotos: käyttöliittymä, joka pystyy visualisoimaan reitin
    -   *Feature* 2.3 Tallenna tulokset
        -   Tuotos: käyttöliittymä, joka pystyy tallentamaan laskennan tulokset niin, että ne ovat jatkohyödynnettävissä
-   **Epic 3. A-star**
    -   *Feature 3.1 A-star algoritmin perustoteutus*
        -   Toiminnot: algoritmi saa parametrina kartan sekä alku- ja loppupisteen ja laskee jollain tavoin reitin niiden välillä
        -   Tuotos: toimiva mutta ei lopullinen versio algoritmista
    -   *Feature 3.2 Optimointi*
        -   Toiminnot: algoritmi saa parametrina kartan sekä alku- ja loppupisteen ja laskee reitin niiden välillä
        -   Tuotos: lopullinen versio algoritmista, jossa heuristinen funktio sekä ajan- ja tilankäyttö on optimoitu
-   **Epic 4. Jump point search**
    -   *Feature* 4.1 Algoritmin perustoteutus
        -   Tuotos: ajettavissa oleva versio algoritmista
    -   *Feature* 4.2 Fringe search viimeistely
        -   Tuotos: lopullinen versio algoritmista, jossa heuristinen funktio sekä ajan- ja tilankäyttö on optimoitu

# Toteutusvälineet

Toteuskieli on Java

-   Java, versio 23
-   Käyttöliittymä toteutetaan Java AWT-, Java2D- ja Swing-kirjastoilla. Testaaminen tapahtuu JUnitilla..

Toteutusympäristö

-   Windows 11 desktop PC
-   OpenJDK 23.0
-   VSCode 1.94 IDE + VSCode Extension Pack for Java (Microsoft) + Maven 3.9.9

Muita kieliä mahdollista harjoitustyön vertaisarviointia varten

-   Python: opintojen myötä kohtalaisesti kokemusta mutta ei ammatillista kokemusta
-   JavaScript: jonkin verran kokemusta ammatillisesta historiasta ja opinnoista
-   Fortran77: runsaasti ammatillista kokemusta mutta vuosikymmenten takaan

# Viitteet

Peter E. Hart, Nils J. Nilsson, Bertram Raphael (1968). *A Formal Basis for the Heuristic Determination of Minimum Cost Paths*. IEEE Transactions on Systems Science and Cybernetics, Volume 4, Issue 2, pages 100-107. DOI: 10.1109/TSSC.1968.300136. Noudettu 8.11.2024 osoitteesta [https://users.cecs.anu.edu.au/\~dharabor/data/papers/harabor-grastien-aaai11.pdf](https://users.cecs.anu.edu.au/~dharabor/data/papers/harabor-grastien-aaai11.pdf)

Harabor, D., & Grastien, A. (2011). Online Graph Pruning for Pathfinding On Grid Maps. *Proceedings of the AAAI Conference on Artificial Intelligence*, *25*(1), 1114-1119. <https://doi.org/10.1609/aaai.v25i1.7994>

Red Blog Games sisällöntuottajat (2014-2023), *Introduction to the A\* Algorithm.* Haettu 8.11.2024 osoitteesta <https://www.redblobgames.com/pathfinding/a-star/introduction.html>

Wikipedian sisällöntuottajat (n.d.), *A\* search algorithm*. Wikipedia, avoin tietosanakirja. Haettu 8.11.2024 osoitteesta [https://en.wikipedia.org/wiki/A\*_search_algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm)

Red Blog Games (2015), *Improving Heuristics.* Haettu 8.11.2024 osoitteesta <https://www.redblobgames.com/pathfinding/heuristics/differential.html>

Zerowidth (2015), *A Visual Explanation of Jump Point Search.* Haettu 8.11.2024 osoitteesta <https://zerowidth.com/2013/a-visual-explanation-of-jump-point-search/>
