<br>
<br>
<div align="center">
  <h1>Sistem za evidenciju i kontrolu prelaska državne granice</h1> 
</div>
<br>
<br>
<br>

<div style="page-break-before: always;"></div>

Distribuirani sistem koji simulira evidenciju i kontrolu putnika prilikom prelaska državne granice — od upravljanja graničnim terminalima, preko prijave i rada policijskog/carinskog osoblja na terenu, do provjere potjernica i arhiviranja dokumenata.
Sistem se sastoji od devet samostalnih Java aplikacija koje međusobno komuniciraju putem SOAP-a, REST-a, RMI-ja, TCP soketa (TLS) i multicast-a, i predstavlja praktičnu vježbu iz kombinovanja različitih mrežnih tehnologija u jednom sistemu.

## Sadržaj

- [Komponente sistema](#komponente-sistema)
- [Kako sistem funkcioniše](#kako-sistem-funkcioniše)
- [Bezbjednost](#bezbjednost)
- [Serijalizacija i čuvanje podataka](#serijalizacija-i-čuvanje-podataka)
- [Struktura repozitorijuma](#struktura-repozitorijuma)
- [Pokretanje sistema](#pokretanje-sistema)
- [Kredencijali za testiranje](#kredencijali-za-testiranje)
- [Konfiguracija](#konfiguracija)

## Komponente sistema

| Komponenta | Tip | Protokol / tehnologija | Uloga |
|---|---|---|---|
| **Centralni_Registar** | Web servis (server) | SOAP (Apache Axis) + REST (Jersey) | Čuva podatke o terminalima, evidenciju provjerenih putnika, potjernica i priloženih dokumenata |
| **Administratorska_Aplikacija** | JavaFX desktop + web modul | SOAP klijent, REST server i klijent | CRUD terminala, upravljanje korisničkim nalozima, pregled evidencije potjernica/dokumenata |
| **Prelazak_Granice_Servis** | Web servis (server) | SOAP (Apache Axis) | Simulira policijsku i carinsku kontrolu na konkretnom ulazu/izlazu |
| **Chat_Server** | Samostalna Java aplikacija | TLS (secured socket) | Posreduje chat poruke (unicast/multicast/broadcast) između klijentskih aplikacija |
| **File_Server** | Samostalna Java aplikacija | RMI | Prijem, kompresija (ZIP) i čuvanje dokumenata koje osoba prilaže na carini |
| **Registar_Potjernica** | Samostalna Java aplikacija | RMI | Provjerava da li je identifikator osobe na listi potjernica |
| **Klijentska_Aplikacija** | JavaFX desktop (N instanci) | REST, SOAP, RMI, TLS soket, multicast | Rad na konkretnom ulazu/izlazu (policijski ili carinski), chat, promjena lozinke, pregled evidencije |
| **Testna_Aplikacija** | JavaFX desktop | SOAP klijent, multicast | Simulira dolazak i prolazak osobe kroz odabrani ulaz/izlaz terminala |
| **Pocetni_Skup_Podataka** | Java skripta | SOAP, Redis | Popunjava sistem početnim podacima (terminali, korisnički nalozi) radi lakšeg testiranja |
| **Redis** | In-memory baza | — | Čuva korisnička imena i lozinke (heširane) |

## Kako sistem funkcioniše

**Terminali.** Svaki granični terminal ima jedinstven cjelobrojni identifikator, naziv i proizvoljan broj ulaza/izlaza, od kojih svaki ima i policijsku i carinsku kontrolu. Terminalima se upravlja isključivo kroz Administratorsku aplikaciju (dodavanje, izmjena, brisanje, pregled), a podaci se binarno serijalizuju i čuvaju na Centralnom registru, svaki terminal u posebnom fajlu.

**Prijava na klijentsku aplikaciju.** Prilikom pokretanja Klijentske aplikacije korisnik bira identifikator ulaza/izlaza, tip kontrole (policijska ili carinska) i naziv terminala. Prvo se putem SOAP-a provjerava da li terminal postoji na Centralnom registru, a zatim se korisničko ime i lozinka provjeravaju putem REST servisa. Nakon prijave dostupan je meni sa opcijama za odjavu, promjenu lozinke i pregled evidencije provjerenih putnika.

**Chat.** Klijentske aplikacije komuniciraju međusobno preko sigurnog (TLS) soket kanala ka Chat serveru, uz tri vida slanja poruka: pojedinačnom ulazu/izlazu, svim ulazima/izlazima jednog terminala (multicast) i svim ulazima/izlazima u sistemu (broadcast).

**Promjena lozinke.** Za razliku od ostalih korisničkih podataka, promjena lozinke se ne vrši posredno preko Administratorske aplikacije — Klijentska aplikacija ažurira lozinku direktno.

**Simulacija prelaska granice.** Testna aplikacija simulira dolazak osobe na odabrani ulaz/izlaz. Nakon provjere da terminal postoji (SOAP), simulacija prvo prolazi kroz policijsku kontrolu: identifikator osobe se putem RMI-ja provjerava u Registru potjernica. Ako je osoba na potjernici, svim ulazima/izlazima terminala šalje se obavještenje (multicast) o privremenom zatvaranju prelaza, sve dok se osoba ne procesira na tom policijskom ulazu — tada se ostalim ulazima/izlazima šalje obavještenje da je rad nastavljen. Ako je osoba prošla policijsku kontrolu, na carinskom dijelu prilaže dokumente, koji se putem RMI-ja šalju na Fajl server i tamo čuvaju kompresovano (ZIP), radi smanjenja količine prenesenih podataka.

**Administracija.** Administratorska aplikacija, pored upravljanja terminalima i korisničkim nalozima, omogućava i preuzimanje evidencije svih detektovanih osoba sa potjernica i priloženih carinskih dokumenata putem REST servisa.

## Bezbjednost

- **Lozinke** se ne čuvaju u čistom tekstu — heširaju se algoritmom **PBKDF2WithHmacSHA512** (10 000 iteracija, ključ dužine 256 bita) uz jedinstveni salt po korisniku, a rezultat se kodira u Base64 prije čuvanja u Redis-u.
- **Chat komunikacija** ide preko TLS-a (secured socket), uz Java keystore/truststore (`.jks`).
- Sav pristup terminalima, korisnicima i osjetljivim evidencijama ide kroz provjeru identiteta prije izvršenja operacije.

## Serijalizacija i čuvanje podataka

U skladu sa zahtjevom da se implementiraju najmanje četiri načina serijalizacije, terminali na Centralnom registru se serijalizuju ciklično kroz četiri formata (1. terminal → GSON/JSON, 2. → Kryo, 3. → Java native, 4. → XML, 5. → GSON ponovo, itd.), svaki u zasebnom fajlu (`id_XXX__tip_FORMAT.out`).

Ostali podaci se čuvaju odvojeno, u skladu sa njihovom prirodom:

| Podatak | Format | Lokacija |
|---|---|---|
| Terminali | GSON / Kryo / Java native / XML (rotirajuće) | `Centralni_Registar/.../terminals` |
| Evidencija provjerenih putnika | tekstualna datoteka (append), po terminalu | `Centralni_Registar/.../passings` |
| Evidencija potjernica (procesirane osobe) | XML | `Centralni_Registar/.../warrants` |
| Evidencija priloženih dokumenata (metapodaci) | XML | `Centralni_Registar/.../documents` |
| Sami dokumenti osobe | ZIP arhiva + originalni fajlovi | `File_Server/documents/{terminal}#{prolaz}#{vrijeme}#{redni_broj}` |
| Korisnički nalozi (kredencijali) | heširano, u ključ-vrijednost obliku | Redis |

## Struktura repozitorijuma

```
├── Administratorska_Aplikacija/   # JavaFX admin GUI + REST/SOAP web modul
├── Centralni_Registar/            # SOAP + REST web servis, registar terminala i evidencija
├── Chat_Server/                   # TLS chat server (unicast/multicast/broadcast)
├── File_Server/                   # RMI servis za prijem i arhiviranje dokumenata
├── Klijentska_Aplikacija/         # JavaFX klijent za rad na ulazu/izlazu terminala
├── Pocetni_Skup_Podataka/         # Skripta za popunjavanje početnih podataka
├── Prelazak_Granice_Servis/       # SOAP servis za simulaciju policijske/carinske kontrole
├── Registar_Potjernica/           # RMI servis, registar potjernica
├── Testna_Aplikacija/             # JavaFX aplikacija za simulaciju prelaska granice
└── Uputstvo/                      # Specifikacija zadatka, uputstvo za pokretanje, napomene
```

Svaka od aplikacija koje se pokreću samostalno kao Java aplikacije (`Chat_Server`, `File_Server`, `Registar_Potjernica`, `Klijentska_Aplikacija`, `Testna_Aplikacija`, `Pocetni_Skup_Podataka`) sadrži svoj `config.properties` fajl u `resources/properties`, dok se `Centralni_Registar`, `Administratorska_Aplikacija` i `Prelazak_Granice_Servis` pokreću kao web aplikacije na serveru (npr. Apache Tomcat), uz Apache Axis za SOAP dio.

## Pokretanje sistema

Redoslijed pokretanja komponenti je bitan zbog međusobnih zavisnosti:

1. **Redis** — pokrenuti Redis server (podrazumijevano `localhost:6379`)
2. **Centralni_Registar** — deploy na server (npr. Tomcat)
3. **Pocetni_Skup_Podataka** — pokrenuti `PocetniPodaci.java` da bi se popunili početni podaci
4. **Administratorska_Aplikacija** — deploy web dijela na server
5. **Prelazak_Granice_Servis** — deploy na server
6. **Chat_Server** — pokrenuti `ChatServer.java`
7. **File_Server** — pokrenuti `FileServer.java`
8. **Registar_Potjernica** — pokrenuti `PotjerniceServer.java`
9. **Administratorska_Aplikacija** (desktop dio) — pokrenuti `PokretanjeAdministrator.java`
10. **Klijentska_Aplikacija** — pokrenuti `PokretanjeKlijent.java`
11. **Testna_Aplikacija** — pokrenuti `PokretanjeTest.java`

## Kredencijali za testiranje

**Administratorska aplikacija**

| Korisničko ime | Lozinka |
|---|---|
| admin | nimda |

**Klijentska aplikacija**

| Terminal | Prolaz | Korisničko ime | Lozinka | Kontrola |
|---|---|---|---|---|
| AAA | 101 | Ana | anA | policijska |
| AAA | 101 | Ena | anE | carinska |
| AAA | 102 | Ina | anI | policijska |
| CCC | 105 | Tina | aniT | carinska |

**Testna aplikacija**

| Terminal | Prolaz |
|---|---|
| AAA | 101 |

## Konfiguracija

Svi parametri (adrese, portovi, putanje, timeout vrijednosti i sl.) definisani su u `config.properties` fajlovima svake aplikacije, a ne u kodu. Prije pokretanja sistema na drugoj mašini potrebno je provjeriti i po potrebi prilagoditi:

- apsolutnu putanju do direktorijuma sa terminalima u konfiguraciji `Centralni_Registar`-a (podrazumijevano je postavljena na razvojno okruženje autora),
- adrese i portove servisa ako se komponente pokreću na različitim mašinama (podrazumijevano sve radi na `localhost`),
- lozinke za keystore/truststore korišćene u TLS chat komunikaciji.

---

*Projekat je izrađen u okviru predmeta Mrežno i distribuirano programiranje, Elektrotehnički fakultet Banja Luka, 2022. godine.*
