# microservices-shopping-app
Shopping app using microservices architecture, currenlty user service and product services are built. Image resizing tasks handled by celery and rabbitmq respectively for user service and product service.

###  User .env:
Don't forget to create .env file for user service;
`MONGO_URL = "mongodb://admin:password@mongo:27017/?retryWrites=true&w=majority"`, local container using mongodb
`JWT_SECRET = "your jwt secret"`

