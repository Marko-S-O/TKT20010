# Viikon tehtävät

-   Toteutettu yksikkötestejä junitilla.
-   Jotta olisi mahdollista testata Moving AI Labin skenaarioita vastaan, pitää käyttää tiukkoja sääntöjä kulmien osalta. Sitä varten toteutettu ohjelmaan jokaiselle pikselille esilaskettava 8-alkioinen boolean-taulukko, joka kertoo, voiko suuntaan siirtyä.
-   Edelle mainitusta ja aikaisemman JPS-version puutteista johtuen kirjoitettu JPS:n oleelliset osat uudestaan. Sain sen vihdoin lauantaina iltapäivällä toimimaan, mutta en ehtinyt kunnolla testata. Mahdollisesti siinä on vielä joitain bugeja, mutta ainakin pääosin se tuntuisi toimivan, pruunaavan solmut oikein ja löytävän nyt oikeat hyppypisteetkin.

# Ohjelman edistyminen

-   Tehty yksikkötestejä, kirjoitettu JPS uudelleen.

# Oppimisreflektio

-   Nyt minusta tuntuu, että tosissaan ymmärrän JPS:n periaatteen ja miksi sen toimivuuteen voi luottaa. Zerowidth.comin JPS-simulaattori oli tässä erityisesti hyödyksi, vaikka se noudattaakin vapaampia kulmasääntöjä.

# Haasteet ja avoimet kysymykset

-   Tämä on tässä kohdassa lähinnä avoin kysymys: minkälaisilla kartoilla JPS oikeasti on tehokkaampi kuin hyvin toteutettu A. Vaikka se ”käsittelee” selkeästi pienemmän määrän soluja kuin A, se kuitenkin oikeasti hyppypisteiden etsinnässä evaluoi pitkiä polkuja rekursiivisesti, ja jokaisessa polun solussa tehdään useita evaluointeja. Googlailin hiukan tätä ja aiheesta löytyi runsaasti mielenkiintoista keskustelua.

# Jatkosuunnitelma

Seuraavalla viikolla

-   JPS:n testaus ja finalisointi
-   Tehokkuusvertailun karttojen valinta ja tehokkuusvertailun toteutus. Tätä varten toteutan oman kustomoidun ohjelmansa.
-   Junit-testien lisääminen
-   Koodin siistiminen katselmointikuntoon
-   Kuluvalla viikolla pysytyin käyttämään tähän rajallisesti aikaa. Ensi viikko on pääosin varattu tälle, joten toivon pääseväni merkittävästi eteenpäin lopputuotosten kanssa.

# Käytetty työaika

15 h ![](media/b47676ec914961293b96862963c100c7.png)
