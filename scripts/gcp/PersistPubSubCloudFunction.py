import base64
import uuid
import json
from datetime import datetime
from google.cloud import datastore

TIME_FORMAT = '%Y-%m-%dT%H:%M:%S.%f'


def unicorn_status_change(event, context):
    """Triggered from a message on a Cloud Pub/Sub topic.
    Args:
         event (dict): Event payload.
         context (google.cloud.functions.Context): Metadata for the event.
    """
    unicorn_status = base64.b64decode(event['data']).decode('utf-8')
    save_unicorn_status('unicorn-221011', unicorn_status)


def save_unicorn_status(project_id, unicorn_status):
    # Instantiates a client
    datastore_client = datastore.Client(project_id)

    # The kind for the new entity
    kind = 'Task'
    # The name/ID for the new entity
    name = str(uuid.uuid4())
    # The Cloud Datastore key for the new entity
    task_key = datastore_client.key(kind, name)

    # Prepares the new entity
    task = datastore.Entity(key=task_key)

    data = json.loads(unicorn_status)

    task.update({
        'name': data['name'],
        'distance': float(data['distance']),
        'healthPoints': int(data['healthPoints']),
        'latitude': float(data['latitude']),
        'longitude': float(data['longitude']),
        'magicPoints': int(data['magicPoints']),
        'statusTime': datetime.strptime(data['statusTime'],
                                        TIME_FORMAT)
    })

    # Saves the entity
    datastore_client.put(task)

    print('Saved {}: {}'.format(task.key.name, task['name']))
