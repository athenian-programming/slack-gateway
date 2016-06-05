
deploy:
	mvn heroku:deploy

logs:
	heroku logs -t

dashboard:
	mvn heroku:dashboard

versioncheck:
	mvn versions:display-dependency-updates versions:display-plugin-updates
