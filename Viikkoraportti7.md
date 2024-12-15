# Viikon tehtävät

-   Tapaaminen ohjaajan kanssa, jonka perusteella
    -   Tehty koodiin sovitut korjaukset /optimoinnit A\*/Dijkstraan
    -   Tehty uudelleen suorituskykytestaus ja päivitetty testiraportti
-   Peer review 2 tehty
-   Käyttöohjeesta ja toteutusdokumentista tehty ensimmäiset versiot
-   Tutkin Java Flight Recorderilla JPS:n CPU-ajan käyttöä. Se arvioi, että metodi jump vie n. 95% CPU-ajasta. Lisäksi Node-olioita tehtiin rekursiivisessa hypyssä valtavia määriä.
    -   Tämän perusteella optimoitu JPS:ää niin, että nodet luodaan vasta löytynyttä hyppypistettä varten. Tämä paransi JPS:n suorituskykyä joitain kymmeniä prosentteja. Uusi suorituskykytestaus vaaditaan vielä tämän vuoksi.
    -   Edit 15.12.: edellä mainitun muutoksen jälkeen rekursion purkaminen oli triviaalia. Rekursio purettu, mikä muuttaa JPS:n ja A:n suhdetta niin, että JPS:n keskimääräinen suoritusaika on alle viidennes A:n ajasta.

# Ohjelman edistyminen

-   Ohjelmaa korjattu / optimoitu
-   Saatu peer review 2 kommentit 14.12. Katselmoijalle täydet pisteet: hän oli todella perehtynyt ohjelmaan ja jopa ajanut sitä, minkä perusteella tuli hyviä kommentteja.

# Oppimisreflektio

-   Läpikäytyjen asioiden kautta Java-koodaamiseen kiinni pääsemiseksi vuosien tauon jälkeen iterointi ohjelman kanssa on varmasti hyödyksi

# Haasteet ja avoimet kysymykset

-   Yliopiston käyttäjätunnukseni ei toimi tuntemattomasta syystä. Tämän vuoksi en voinut ilmoittautua demoon. Yritän maanantaina aamupäivällä saada sen taas auki heldeskin avulla.
-   Mitä ohjelmassa ja dokumenteissa vielä tarvitsee tehdä hyväksyttävään suoritukseen?

# Jatkosuunnitelma

Seuraavalla viikolla

-   Demo
-   JPS-koodin profilointi, optimointi
-   Suorituskykytestaus optimoidulle ohjelmalle
-   Peer review 2:n kommenttien huomioiminen ohjelmaan

# Käytetty työaika

n. 15 h
