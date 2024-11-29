# Testauksen yleiskuva

Testaus jakautuu kahteen osaan

1.  Funktionaalinen testaus:
    -   Testataan riittävällä määrällä erityyppisiä skenaarioita kattavasti reitinhakualgoritmien toimivuus.
    -   Testataan algoritmien käyttämät apumetodit riittävällä tasolla.
2.  Suorituskykytestaus
    -   Testataan riittävällä määrällä skenaarioita ja iteraatioita (300\*9) algoritmien reitinhaun nopeus niin, että niitä voidaan luotettavasti vertailla.
    -   Suorituskykytestiskenaarioiden yhteydessä tarkistetaan aina, että haettu reitti löytyi ja se oli oikean pituinen, jolloin niillä lisätään merkittävästi funktionaalisen testauksen kattavuutta.

Kaikki algoritmien funktionaalinen ja suorituskykytestaus tehdään Moving AI Labin skenaarioita (<https://www.movingai.com/benchmarks/>) vasten.

Kaikki testit toteutetaan JUnitilla, jolloin ne on alusta saakka automatisoitu ja liitettävissä Maven-projektin koostamiseen. Testauksen kattavuuden analyysiin käytettiin JaCoCo-kirjastoa (https://www.jacoco.org/jacoco/).

Käyttöliittymäosuuksia ei organisoidusti testata. Käyttöliittymä on tässä työssä ainoastaan kehitysaikainen apuväline ja kaikki lopputuotokset tuotetaan ilman sitä.

# Funktionaalinen testaus

## Suoritetut testit

Algoritmien testit on toteutettu valittuja Moving AI Labin skennarioita vastaan ja ne on jaettu neljään kategoriaan:

1.  Eri liikepääsuuntiin tapahtuva reitinhaku
2.  Lähtöpisteet kartan eri kulmissa ja reunoilla
3.  Maalipisteet kartan kulmissa ja reunoilla
4.  Erikoistapaukset kuten tyhjä kartta sekä vierekkäiset ja samat maali- ja lähtöpisteet.

Testitapaukset on listattu seuraavassa taulukossa:

| **ID**                                   | **Selitys** | **Kartta**         | **X(0)**                             | **Y(0)** | **X(g)** | **Y(g)** | **Etäisyys** |
|------------------------------------------|-------------|--------------------|--------------------------------------|----------|----------|----------|--------------|
|                                          |             |                    |                                      |          |          |          |              |
| **Testikartat eri pääliikesuuntiin**     |             |                    |                                      |          |          |          |              |
| 1.1                                      | ↗           | Berlin_2_256.map   | 2                                    | 249      | 253      | 16       | 370.9432175  |
| 1.2                                      | →           | NewYork_1_256.map  | 9                                    | 123      | 240      | 117      | 250.0538239  |
| 1.3                                      | ↘           | Sydney_1_256.map   | 1                                    | 1        | 216      | 234      | 340.8010819  |
| 1.4                                      | ↓           | London_1_256.map   | 133                                  | 12       | 129      | 248      | 262.1248917  |
| 1.5                                      | ↙           | Moscow_0_256.map   | 247                                  | 4        | 7        | 247      | 361.0559158  |
| 1.6                                      | ←           | Paris_2_256.map    | 253                                  | 128      | 4        | 125      | 275.0954544  |
| 1.7                                      | ↖           | Boston_1_256.map   | 249                                  | 254      | 4        | 9        | 363.2691193  |
| 1.8                                      | ↑           | Denver_1_256.map   | 120                                  | 254      | 118      | 2        | 261.9411255  |
|                                          |             |                    |                                      |          |          |          |              |
| **Aloituspisteet reunoilla ja kulmissa** |             |                    |                                      |          |          |          |              |
| 2.1                                      | (255,0)     | Milan_0_256.map    | 255                                  | 0        | 176      | 66       | 106.3380951  |
| 2.2                                      | (255, x)    | Berlin_0_256.map   | 255                                  | 17       | 253      | 108      | 95.97056274  |
| 2.3                                      | (255,255)   | Berlin_0_256.map   | 255                                  | 255      | 50       | 26       | 336.1736649  |
| 2.4                                      | (x,255)     | Berlin_0_256.map   | 125                                  | 255      | 47       | 181      | 157.3969696  |
| 2.5                                      | (0,255)     | Denver_0_256.map   | 0                                    | 255      | 254      | 102      | 340.2203461  |
| 2.6                                      | (0,x)       | Berlin_0_256.map   | 0                                    | 186      | 88       | 232      | 239.2203461  |
| 2.7                                      | (0,0)       | Denver_0_256.map   | 0                                    | 0        | 221      | 195      | 316.4163055  |
| 2.8                                      | (x,0)       | Boston_0_256.map   | 1                                    | 0        | 17       | 138      | 190.3086578  |
|                                          |             |                    |                                      |          |          |          |              |
| **Päätepisteet reunoilla ja kulmissa**   |             |                    |                                      |          |          |          |              |
| 3.1                                      | (255,0)     | Moscow_0_256.map   | 10                                   | 188      | 255      | 0        | 324.0437225  |
| 3.2                                      | (255, x)    | Denver_1_256.map   | 231                                  | 115      | 255      | 111      | 25.65685425  |
| 3.3                                      | (255,255)   | Shanghai_0_256.map | 131                                  | 20       | 255      | 255      | 325.9188309  |
| 3.4                                      | (x,255)     | London_1_256.map   | 224                                  | 240      | 240      | 255      | 22.21320343  |
| 3.5                                      | (0,255)     | Moscow_2_256.map   | 243                                  | 23       | 0        | 255      | 356.0853531  |
| 3.6                                      | (0,x)       | Boston_2_256.map   | 104                                  | 174      | 0        | 188      | 109.7989899  |
| 3.7                                      | (0,0)       | Paris_2_256.map    | 31                                   | 157      | 0        | 0        | 396.2741699  |
| 3.8                                      | (x,0)       | NewYork_0_256.map  | 70                                   | 87       | 52       | 0        | 94.45584412  |
|                                          |             |                    |                                      |          |          |          |              |
| **Erikoistapaukset**                     |             |                    |                                      |          |          |          |              |
| 4.1                                      |             | Tyhjä testikartta  | Yhden solmun liike horisontaalisesti |          |          |          |              |
| 4.2                                      |             | Tyhjä testikartta  | Yhden solmun liike vertikaalisesti   |          |          |          |              |
| 4.3                                      |             | Tyhjä testikartta  | Yhden solmun liike diagonaalisesti   |          |          |          |              |
| 4.4                                      |             | Tyhjä testikartta  | Start = goal                         |          |          |          |              |
| 4.5                                      |             | Tyhjä testikartta  | Navigointi tyhjällä kartalla         |          |          |          |              |
| 4.6                                      | Ei reittiä  | Berlin_0_256.map   | 1                                    | 1        | 200      | 254      |              |

## Testitulokset

Kaikki edellisen kappaleen testit ja apumetodien testit suoritettiin onnistuneesti. Lisäksi kaikki suorituskykytestauksen 300 skenaariota suoritettiin toiminnallisessa mielessä onnistuneesti (maali löytyi ja laskettu matka oikea).

## Testauksen kattavuus

Testauksen kattavuus reitinhakualgoritmeille on 100% ja brancheille 93-97%. Tarkemmat tiedot ovat alla olevassa taulukossa ja yksityiskohtaiset kattavuusraportit hakemistossa TestResults. Oletus on, että käytetyt apuluokat- ja metodit tulevat testatuksi kattavasti algoritmien kautta, kun algoritmeja testataan sadoilla monimutkaisilla skenaarioilla.

![Coverage result](https://github.com/Marko-S-O/TKT20010/blob/main/TestResults/coverage.jpg)

# Suorituskykytestaus

## Metodologia

Suorituskykytestaus tehtiin Movin AI Labin valmiilla skenaarioilla

-   Kartat: kaikki 1024\*1024 kaupunkikartat, 30 kpl
-   Skenaariot: 10 viimeistä (käytännössä pisintä) skenaariota kunkin kaupungin skenaariotiedostossa
-   Iteraatiot: 9 iteraatiota per skenaario ja algoritmi, mikä tekee yhteensä 2700 evaluaatiota. Jokaiseen iteraatiokierrokseen vaihdettiin algoritmien suoritusjärjestys.

Testien toteutuskoodi löytyy [GitHubin hakemistosta](https://github.com/Marko-S-O/TKT20010/tree/main/pathfinder-app/src/test/java/com/orasaari).

## Testitulokset

Tarkat testitulokset ovat hakemistossa [TestResults](https://github.com/Marko-S-O/TKT20010/tree/main/TestResults) tiedostoissa evaluation_details.csv ja evaluation_summary.csv. Alla yhteenveto. Tuloksista on helppo havaita, että kaupunkikartoilla JPS on täysin ylivoimainen.

| **Algoritmi** | **Evalu-aatiot** | **Maali löytyi** | **Oikea reitin pituus** | **Aika yhteensä (ms)** | **Keskim. aika / iteraatio** | **Keskim. solmuja reitillä** | **Keskim. avattuja solmuja** |
|---------------|------------------|------------------|-------------------------|------------------------|------------------------------|------------------------------|------------------------------|
| Dijkstra      | 2700             | 2700             | 2700                    | 336021                 | 124                          | 1192                         | 784871                       |
| A-Star        | 2700             | 2700             | 2700                    | 232686                 | 86                           | 1192                         | 242253                       |
| JPS           | 2700             | 2700             | 2700                    | 52225                  | 19                           | 148                          | 1452                         |
