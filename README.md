# MojaKancelaria

## Spis treści
* [Technologie](#technologie)
* [Głowne informacje](#głowne-informacje)
* [Opis Aplikacji](#opis-aplikacji)



## Technologie
<img src="https://www.android.com/static/2016/img/share/andy-sm.png" alt="drawing" height=80px/>
<img src="https://bugfender.com/wp-content/uploads/2017/06/kotlin-featured.png" alt="drawing" height=80px/>
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/SQLite370.svg/1200px-SQLite370.svg.png" alt="drawing" height=80px/>


## Głowne informacje
Aplikacja mobilna tworzona z wykorzystaniem języka Kotlin. W celu komunikacji z bazą SQLite wykorzystany
został framework Room. 


Tworzona przez:
* [Wiktoria Salamon](https://github.com/wikk2207)
* [Maciej Lewandowicz](https://github.com/sasuke5055)


## Opis Aplikacji
Celem projektu było stworzenie aplikacji wspomagającej zarządzanie kancelarią prawną, poprzez dokumentowanie oraz bieżące śledzenie
różnych typów zobowiązań, wpłat, oraz innych istotnych rzeczy, jak awiza i terminy odbiorów. 

Aplikacja umożliwia dodawanie klientów, dla każdego z nich różnych spraw oraz opłat. 


### Nawigacja po aplikacji

W celu łatwiejszej nawigacji po apliakcji zastosowane zostało boczne menu:

![](Images/11.png)

### Klienci sprawy i zobowiązania

Widok klientów:

![](Images/1.png)

![](Images/2.png)

Widok spraw danego klienta:

![](Images/3.png)

Widok zobowiązań przypisanych do danej sprawy:

![](Images/4.png)

Zobowiązania wyróżniają się kolorem, w zależności do statusu: czy są opłacone, częściowo opłacone, nieopłacone, bądź
czy klient zwleka z terminową zapłatą.

![](Images/9.png)

Widok dodawania wpłaty: 

![](Images/7.png)

Wybór zobowiązań do opłacenia:

![](Images/5.png)

![](Images/6.png)


![](Images/8.png)

Istnieje możliwość filtrowania zobowiąząń po ich typie:

![](Images/10.png)

### Raport

Dodatkową funkcjonalnością jest możliwosć wygenerowania raportu dla danej sprawy do pliku pdf oraz wysłanie 
jako załącznika w wiadomości email:

### Archiwum

![](Images/13.png)

W przypadku zakończenia danej sprawy istnieje możliwosć jej archiwizacji. 

![](Images/14.png)

![](Images/15.png)

### Korespondencja

Śledzenie korespondencji polega na dodaniu numeru przesyłki:

![](Images/33.png)

Następnie aktualny status każdej z przesyłek pobierany jest z api poczty.

![](Images/16.png)

# Backup, autobackup oraz ustawienia

Zaimplementowana została możliwosć backupu oraz przywracana bazy danych do pliku. 

![](Images/21.png)


![](Images/23.png)

W zakładce ustawień można dostosować automatyczny backup do swoich potrzeb

![](Images/22.png)








