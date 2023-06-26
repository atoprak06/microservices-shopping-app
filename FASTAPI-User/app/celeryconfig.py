import dotenv
import os

dotenv.load_dotenv()

broker_url = 'amqp://guest:guest@rabbitmq:5672//'
result_backend = os.getenv("MONGO_URL")
mongodb_backend_settings = {
    'database': 'celery',
    'taskmeta_collection': 'celery_collection',
}
