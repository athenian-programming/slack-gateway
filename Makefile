default:
	mvn clean package

# Requires that the client is logged in with "heroku login" from the shell
deploy:
	mvn heroku:deploy

logs:
	heroku logs -t

dashboard:
	mvn heroku:dashboard

versioncheck:
	mvn versions:display-dependency-updates versions:display-plugin-updates

tree:
	mvn dependency:tree
