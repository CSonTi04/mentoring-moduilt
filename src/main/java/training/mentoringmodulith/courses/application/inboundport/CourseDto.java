package training.mentoringmodulith.courses.application.inboundport;

//DTO-ban ne legyen VO, hogy a json serializálás ne legyen probléma
//Ami aggregate-en belül van, az ne mehessen ki egyből
//próbáljunk meg azonosítani gyakran használatos típusokat, DDD-sek nagoyn típusosak
//Ami üzleti fogalomként jelenik meg az látszódjon osztálynak, de extremizált, de működhet
//Típusok,amik áthatják az egész domain -> pl szig számra használva?
//Igazái dátum vs "gáznap" -> ami 8-tól 8-ig tart, itt a gáznap nem biztos, hogy VO
//Gondoljunk rá mintha egy 3rd party lib, megoszott típusa lenne

//Mi legyen az agggregate-ek és a bounded context-ek között használt típusokkal?
//Használhatjuk-e ugyanazt?
//Nem, mivan, ha két microservice van, de más nyelven íródott?

//Course code felmerülhet már az igényekben
//VO-t definíció szerintem a DDD-ben nem engedjük ki az application rétegen kívülre, csináljunk rá DTO-t
//De vannak bizonyos esetek, mint a pl a gáznap, tt lehet érdemes átgondolni a határokat
public record CourseDto(
        String code,
        String title
) {
}
