call mvn install
call mvn install:install-file -Dfile=src/main/resources/archetype-resources/src/main/resources/MoCFramework-1.0-final.jar -DgroupId=MoC -DartifactId=MoCFramework -Dversion=1.0-final -Dpackaging=jar
echo Archetype installation complete! You can now create a new maven project from the following archetype: MoCChallengeTemplate & pause