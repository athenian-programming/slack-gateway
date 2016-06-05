
deploy:
	mvn heroku:deploy

logs:
	heroku logs -t

dashboard:
	mvn heroku:dashboard