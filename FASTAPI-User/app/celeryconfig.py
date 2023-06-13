broker_url='amqp://guest:guest@rabbitmq:5672//'
result_backend = "mongodb+srv://nodeTutorial00:tiRMxCEpHM5kN37v@cluster0.t4hqdav.mongodb.net/?retryWrites=true&w=majority"
mongodb_backend_settings = {
    'database': 'celery',
    'taskmeta_collection': 'celery_collection',
}