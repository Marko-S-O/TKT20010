# Viikon tehtävät

-   Keskustelu ohjaajan kanssa karttojen esitystavasta
-   Toteutettu JPS
-   Finalisoitu ja optimoitu A\*:a
-   Lisätty käyttöliittymään mahdollisuus ajaa ja nähdä tulokset rinnakkain useista algoritmeista. Lisäksi käyttöliittymän värikoodaus tehty niin, että JPS:n osalta se näyttää hypyt.

# Ohjelman edistyminen

-   Algoritmeista ainakin jonkinlainen versio ja käyttöliittymä toteutettu. Algoritmit laskevat samat tulokset.

# Oppimisreflektio

-   JPS oli tuskallinen ymmärtää ja toteuttaa. Siitä ei oikein löydy kattavia ja hyviä selityksiä ja esimerkkejä netistä. Lopulta paras esitys algoritmista oli itse alkuperäinen paperi.

# Haasteet ja avoimet kysymykset

-   JPS laskee selkeästi hitaammin testatuilla kaupunkikartoilla ja sokkeloilla kuin A, vaikka käsittelee vähemmän soluja. Kun muut hitauskohdat oli optimoitu pois, oli helppo nähdä, että aika kuluu hyppyfunktiossa. Joko asia vain on niin, että tämäntyyppisissä pienehköissä kartoissa taulukoilla toteutettu A on tehokkaampi kuin JPS rekursiivisella hyppypaikan etsinnällä, tai sitten JPS-algoritmini käsittelee enemmän soluja kuin olisi pakko, vaikka laskeekin oikein.
-   Kun purin rekursion, se n. puolitti suoritusajan. Suositusaika oli silti edelleen suurempi kuin A:lla ja se vaikutti käsiteltyjen solmujen määrään, joten palautin rekursion, kun en ollut varma sen oikeasta toiminnasta.

# Jatkosuunnitelma

Seuraavalla viikolla

-   Testiautomaation toteutus JUnitilla
-   Tehokkuusvertailun karttojen valinta ja tehokkuusvertailun toteutus. Tämä vaatinee oman kustomoidun ohjelmansa.

# Käytetty työaika

30+ h
