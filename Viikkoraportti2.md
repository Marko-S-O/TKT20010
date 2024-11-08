# Viikon tehtävät

-   Keskustelu ohjaajan kanssa 5.11. Tällöin vaihdettiin algoritmit (nyt A\* ja JPS).
-   Määrittelydokumentti päivitetty vastaamaan muutoksia projektin sisällössä.
-   Toteutettu käyttöliittymä, joka renderöi kartan ja reitin.
-   Toteutettu yksinkertainen Dijkstra-algoritmi Tirakirjan pseudokoodin mukaisesti.

# Ohjelman edistyminen

-   Käyttöliittymä toteutettu ja viety GitHubiin. Käyttöliittymä näyttää kartan ja visualisoi reitin.
-   Dijkstrasta tehty nopea versio ja viety GitHubiin. Vaikka se ei ollut toteutettavissa algoritmeissa, päädyin tekemään sen ensin sisään pääsemiseksi aiheeseen ja vertailukohdaksi muille. Testaaminen jäi vielä vähiin, mutta piirtämällä algoritmi käyttöliittymälle se silmämääräisesti näyttäisi toimivan.

# Oppimisreflektio

-   Luulen, että pääsin sisään etsintäalgoritmeihin (TiRa-kurssit käyty 90-luvulla).
-   Tilavaativuus ei ole merkityksetön. Olen ajatellut, että algoritmin tehokkuus yleensä on kriittinen asia ja nykyisissä koneissa riittää muistia. Suurissa verkoissa keskusmuistin rajat tulevat kuitenkin nopeasti vastaan, ellei algoritmeja optimoida myös muistin käytön suhteen.
-   Ensin ajattelin toteuttavani Dijkstran tehokkaasti primitiivityypeillä ja taulukoilla. Jouduin kuitenkin nöyrtymään ja tekemään luokat Node ja Edge ja laskemaan vieruslistat etukäteen, että homma pysyi näpeissä. Toteutus tuskin on kovin optimoitu.

# Haasteet ja avoimet kysymykset

-   Toteutin Dijkstran käyttämällä Javan valmista luokkaa PriorityQueue. Pitääkö järjestetty keko kirjoittaa itse?
-   On yllättävän vaikea hahmottaa eri tekijöiden välisiä priorisointeja toteutusta suunnitellessa: tila- vs. aikavaativuus, toteutuksen kompleksisuus vs. tehokkuus.
-   En löytänyt toimivaa tapaa piirtää renderöintikomponenttiin (JPanel) yksittäisiä pikseleitä virkistämättä koko komponenttia, joten ainakin toistaiseksi etsinnän etenemisen reaaliaikainen visualisointi jäi tekemättä.

# Jatkosuunnitelma

Seuraavalla viikolla

-   Dijkstran testaus ja tämänhetkisen koodin stilisointi
-   Alan toteuttaa A\*-algoritmia ja teen sen niin pitkälle kuin ehdin, toivottavasti toimivaksi asti

# Laajojen kielimallien käyttö

Käytetty seuraaviin tarkoituksiin:

-   Java 2D-pikseligrafiikan toteutuskirjastojen vaihtoehtojen haku kartan esitystavan muututtua
-   Viitteiden etsintä lähdeartikkeleihin A\*:sta ja JPS:stä

# Käytetty työaika

Reilu 20 h
