Lien Documentation: https://docs.google.com/document/d/1L7JdT9pLBdm2S3O_-1rLtZrQ5BlIsWOXoYmFL7pKXF0/edit?usp=sharing

Lien de l'analyse : https://docs.google.com/document/d/1DNiaSmyQsH4207_-vhl-QyiKKXY_9secjkvoN7SsYyE/edit?usp=sharing

Lien drive du schema draw.io:    https://drive.google.com/file/d/1KKcqL-26bj0nKQGtYIeUH9LqxpUEczlr/view?usp=sharing

javac -d bin src/requests/Requetes.java src/connectivite/DBConfig.java src/connectivite/DBConnection.java src/requests/Transaction.java src/requests/Constants.java src/requests/GlobalVaraibles.java src/requests/Requetes.java src/interfaceGraphique/AuctionApp.java src/interfaceGraphique/ManageSalesWindow.java src/interfaceGraphique/ManageSalesRoomWindow.java src/EndTask/*.java 

Pour tester l'interface Graphique (l'application)
java -cp "bin:lib/ojdbc6.jar" AuctionApp

Pour tester si les insertions marchent : 
java -cp "bin:lib/ojdbc6.jar" requests.MainRequetes
