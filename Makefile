
all: cleanDB setupDB simuGraphique
# all : simuGraphique

cleanDB:
	javac -d bin -sourcepath src src/connectivite/CleanDatabase.java

setupDB:
	javac -d bin -sourcepath src src/connectivite/SetupDatabase.java


simuGraphique:
	javac -d bin -sourcepath src src/interfaceGraphique/*.java


exeCleanDB:
	java -cp bin:lib/ojdbc6.jar connectivite.CleanDatabase

exeSetupDB:
	java -cp bin:lib/ojdbc6.jar connectivite.SetupDatabase

exeSimuGraphique:
	java -cp bin:lib/ojdbc6.jar AuctionApp



clean:
	rm -rf bin/*





