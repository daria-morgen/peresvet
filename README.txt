Проект "Пересвет"(«Зенит»)

Структура.

Делится на 2 под проекта:
1) Зенит «Марс»
2) Зенит «Земля»

Зенит «Марс»
Основная задача модуля гарантировано отправлять сообщения от пользователя на «Землю».

Зенит «Земля»
Основные задачи принимать сообщения с «Марса», эмулировать задержку сообщений при отправки c «Марса» на «Землю».


cd mars/front

npm install

npm run build

cd mars

mvn clean install

cd earth/front

npm install

npm run build

cd earth

mvn clean install

java -jar ./target/earth-0.0.1-SNAPSHOT.jar -Dperiods.path=./target/classes/periods.json

http://localhost:8081/

java -jar ./target/mars-0.0.1-SNAPSHOT.jar

http://localhost:8082/
